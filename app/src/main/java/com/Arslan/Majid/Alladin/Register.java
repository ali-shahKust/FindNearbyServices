package com.Arslan.Majid.Alladin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;



import com.Arslan.Majid.Alladin.entities.Users;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    private TextView signin;
    private EditText fullname, phone, age, password, confirmpassword;
    double lat, log;
    private Spinner role;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Button signup;
    GoogleMap mGoogleMap;
    private DatabaseReference mDatabase;
    DatabaseReference dbUser;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mProgress = new ProgressDialog(this);
        dbUser = FirebaseDatabase.getInstance().getReference("Users");

        signin = (TextView) findViewById(R.id.txtSignIn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        fullname = (EditText) findViewById(R.id.fullname);
        phone = (EditText) findViewById(R.id.registerphone);
        age = (EditText) findViewById(R.id.registerage);
        password = (EditText) findViewById(R.id.registerpassword);
        //confirmpassword = (EditText) findViewById(R.id.registerconfirmpassword);
        role = (Spinner) findViewById(R.id.registerrole);
        signup = (Button) findViewById(R.id.register);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(fullname.getText().toString()) || !TextUtils.isEmpty(phone.getText().toString()) || !TextUtils.isEmpty(password.getText().toString())) {

                                    }

                String device_token = FirebaseInstanceId.getInstance().getToken();
                String fullname1 = fullname.getText().toString();
                String phone1 = phone.getText().toString();
                String password1 = password.getText().toString();
                String age1 = age.getText().toString();
                String role1 = role.getSelectedItem().toString();
                                    mProgress.setTitle("Registering User");
                                    mProgress.setMessage("Please wait while we create your account !");
                                     mProgress.setCanceledOnTouchOutside(false);
                                    mProgress.show();
                register_user(fullname, phone, password);

                                }
                            });
                }

    private void register_user(final EditText fullname, final EditText phone, final EditText password) {




        mAuth.createUserWithEmailAndPassword(phone.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    getPermissions();

                } else {

                    mProgress.hide();
                    Toast.makeText(Register.this, "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();
                    String e ;


                }

            }
        });

    }




//    private void CreateAccount() {
//        String fullname1 = fullname.getText().toString();
//        String phone1 = phone.getText().toString();
//        String password1 = password.getText().toString();
//        String age1 = age.getText().toString();
//       final  String role1 = role.getSelectedItem().toString();
//
//
//        if (TextUtils.isEmpty(fullname1)) {
//            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(phone1)) {
//            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(password1)) {
//            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
//        } else {
//            mProgress.setTitle("Create Account");
//            mProgress.setMessage("Please wait, while we are checking the credentials.");
//            mProgress.setCanceledOnTouchOutside(false);
//            mProgress.show();
//
//
//            ValidatephoneNumber(fullname1, phone1, password1);
//        }
//    }
//    private void ValidatephoneNumber(final String fullname1, final String phone1, final String password1) {
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if (!(dataSnapshot.child("Users").child(phone1).exists())) {
//                    HashMap<String, String> map = new HashMap<String, String>();
//                    map.put("user_name", fullname1);
//                    map.put("user_phone", phone1);
//                    map.put("user_role", role.toString().trim());
//                    map.put("user_age", age.getText().toString().trim());
//                    map.put("user_password", password1);
//
//
//                    RootRef.child("Users").child(mCurrentUser.getUid()).setValue(map)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//
//
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(Register.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
//                                        mProgress.dismiss();
//
//                                        Intent intent = new Intent(Register.this, MainActivity.class);
//                                        startActivity(intent);
//
//
//                                    } else {
//                                        mProgress.dismiss();
//                                        Toast.makeText(Register.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                } else {
//                    Toast.makeText(Register.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
//                    mProgress.dismiss();
//                    Toast.makeText(Register.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(Register.this, MainActivity.class);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//
//        });
//    }


    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            String[] arr = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

            ActivityCompat.requestPermissions(Register.this, arr, 69);

        } else {
            getLocation();
        }
    }

    private void getLocation() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(Register.this);
        client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();




                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();
                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);




                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("user_name", fullname.getText().toString());
                    userMap.put("user_phone",phone.getText().toString());
                    userMap.put("user_role", role.getSelectedItem().toString());
                    userMap.put("user_age", age.getText().toString().trim());
                    userMap.put("user_password", password.getText().toString());
                    userMap.put("device_token", device_token);
                    userMap.put("latitude", String.valueOf(location.getLatitude()));
                    userMap.put("longitude", String.valueOf(location.getLongitude()));

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                mProgress.dismiss();

                                Intent mainIntent = new Intent(Register.this, MainActivity.class);
                               // mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();

                            }

                            if(!task.isSuccessful()){
                                task.getException();
                            }

                        }
                    });



                } else {
                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 69 && grantResults.length > 0) {
            for (int perm: grantResults) {
                if (perm == PackageManager.PERMISSION_DENIED) {
                    return;
                }
            }
            getLocation();
        }
    }
}

