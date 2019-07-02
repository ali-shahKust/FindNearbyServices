package com.Arslan.Majid.Alladin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Arslan.Majid.Alladin.Prevalent.Prevalent;
import com.Arslan.Majid.Alladin.entities.Users;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.Arslan.Majid.Alladin.entities.Users;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.Arslan.Majid.Alladin.entities.Users.phone;

public class Login extends AppCompatActivity {
    private EditText inputPhone, inputPassword;
    private FirebaseAuth auth;
    private boolean isFound;
    private DatabaseReference dbUser;
    private DatabaseReference mDatabase;
    private FirebaseDatabase user;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private ProgressBar mDialog;
    private Button btnSignup, btnLogin, btnReset;
    private TextView txtSignUp;
    private TextView frgtPass;
    private String parentDbName = "Users";
    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // System.err.println("this is Current user : "+ currentUser);

//
//        SharedPreferences prefs = getSharedPreferences("mydata", MODE_PRIVATE);
//        String restoredText = prefs.getString("username", "");
//        if (!restoredText.equals("")){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//        }


        loadingBar = new ProgressDialog(this);
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        statusCheck();

        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });


        inputPhone = (EditText) findViewById(R.id.loginphone);
        inputPassword = (EditText) findViewById(R.id.loginpassword);
        btnLogin = (Button) findViewById(R.id.signin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputPhone.getText().toString();
                final String password = inputPassword.getText().toString();

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {

                    loadingBar.setTitle("Logging In");
                    loadingBar.setMessage("Please wait while we check your credentials.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    loginUser(inputPhone, inputPassword);

                }
            }
        });


    }

    private void loginUser(final EditText inputPhone, final EditText inputPassword) {

        mAuth.signInWithEmailAndPassword(inputPhone.getText().toString(), inputPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    loadingBar.dismiss();

                    final FirebaseUser current_user_id = mAuth.getCurrentUser();
                    String uid = current_user_id.getUid();

                    System.err.println("Current user is : " + current_user_id);


                    Log.e("Current user is : ", current_user_id.getEmail());
                    final String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    try {

                    Log.e("token",deviceToken);
                    Log.e("email", current_user_id.getEmail());


                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    AndroidNetworking.get("https://translationchatapp.herokuapp.com/createUserToken/"+current_user_id.getEmail()+"/"+deviceToken)

                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // do anything with response
                                    Log.e("res", response.toString());
                                }
                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                }
                            });

                    mUserDatabase.child(uid).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Intent mainIntent = new Intent(Login.this, MainActivity.class);
                            //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            getLocation();
                            finish();






                        }
                    });


                } else {

                    loadingBar.hide();

                    String task_result = task.getException().getMessage().toString();

                    Toast.makeText(Login.this, "Error : " + task_result, Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void getLocation() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(Login.this);
        client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();



                    FirebaseUser current_user_id = mAuth.getCurrentUser();
                    final String uid = current_user_id.getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("latitude", String.valueOf(location.getLatitude()));
                    userMap.put("longitude", String.valueOf(location.getLongitude()));
                    ref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Location Updated successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            if (!task.isSuccessful()) {
                                task.getException();
                            }


                        }

                    });


                } else {
                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, final int id) {
                        Toast.makeText(Login.this, "Enable Gps First", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(Login.this , Welcome.class);
                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}






