package com.Arslan.Majid.Alladin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.concurrent.TimeUnit;

import static com.Arslan.Majid.Alladin.entities.Users.phone;

public class Login extends AppCompatActivity {
    private EditText inputPhone, inputPassword;
    private FirebaseAuth auth;
    private boolean isFound;
    private DatabaseReference dbUser;
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

        mAuth.signInWithEmailAndPassword(inputPhone.getText().toString(),inputPassword.getText().toString() ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    loadingBar.dismiss();

                    FirebaseUser current_user_id = mAuth.getCurrentUser();
                    String uid = current_user_id.getUid();

                    System.err.println("Current user is : "+ current_user_id);
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    mUserDatabase.child(uid).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Intent mainIntent = new Intent(Login.this, MainActivity.class);
                            //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
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
}







//    private void LoginUser() {
//        String Phonenum = inputPhone.getText().toString();
//        String password = inputPassword.getText().toString();
//
////        SharedPreferences.Editor editor = getSharedPreferences("mydata", MODE_PRIVATE).edit();
////        editor.putString("username", Phonenum);
////        editor.putString("pass", password);
////        editor.apply();
//
//        phone = Phonenum;
//        Users.password = password;
//
//        if (TextUtils.isEmpty(Phonenum)) {
//            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
//        } else {
//            loadingBar.setTitle("Login Account");
//            loadingBar.setMessage("Please wait, while we are checking the credentials.");
//            loadingBar.setCanceledOnTouchOutside(false);
//            loadingBar.show();
//
//
//            AllowAccessToAccount(Phonenum, password);
//        }
//    }

//
//    private void AllowAccessToAccount(final String Phonenum, final String password) {
////        if (chkBoxRememberMe.isChecked()) {
////            Paper.book().write(Prevalent.UserPhoneKey, phone);
////            Paper.book().write(Prevalent.UserPasswordKey, password);
////        }
//
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(parentDbName).child(Phonenum).exists()) {
//                    Users usersData = dataSnapshot.child(parentDbName).child(Phonenum).getValue(Users.class);
//
//                    if (usersData.getPhone().equals(Phonenum)) {
//                        if (usersData.getPassword().equals(password)) {
//                            loadingBar.dismiss();
//
//                                Toast.makeText(Login.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
//                                 System.err.println("Phone Number is " + Phonenum);
//                                 System.err.println("Password is " + password);
//                                 Intent intent = new Intent(Login.this, MainActivity.class);
//                                //Prevalent.currentOnlineUser = usersData;
//                                startActivity(intent);
//                                finish();
//
//                        } else {
//                            loadingBar.dismiss();
//                            Toast.makeText(Login.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                } else {
//                    Toast.makeText(Login.this, "Account with this " + Phonenum + " number do not exists.", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//}

//    private void SignInUser(String email, String pass) {
//    }
//}



//    private void logInUsers(final String phone, final String password){
//        mDialog.setMessage("Please wait...");
//        mDialog.show();
//        mDialog.setCanceledOnTouchOutside(false);
//        PhoneAuthProvider auth = PhoneAuthProvider.getInstance();
//
//        auth.verifyPhoneNumber(inputPhone.getText().toString(), 60, TimeUnit.SECONDS, Login.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        mDialog.dismiss();
//                        Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Login.this, MainActivity.class);
//                        startActivity(intent);
//
//                        finish();
//
//                    }
//                });
//            }
//
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//
//                Log.i("dxdiag", e.getMessage());
//
//            }
//        });
//    }

//    private void AllowAccessToAccount(final String phone, final String password) {
//        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Users login = dataSnapshot.child(phone).getValue(Users.class);
//                if (login.equals(phone)) {
//                    if (!phone.isEmpty()) {
//                        //Users login = dataSnapshot.child(phone).getValue(Users.class);
//                        if (login.getPassword().equals(password)){
//                            Toast.makeText(Login.this,"login",Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Login.this,MainActivity.class);
//                            startActivity(intent);
//                            loadingBar.dismiss();
//                        }
//                        else {
//                            Toast.makeText(Login.this,"failed",Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//                    else {
//                        Toast.makeText(Login.this,"Not Registered member",Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });