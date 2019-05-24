package com.Arslan.Majid.Alladin;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.Arslan.Majid.Alladin.entities.Users;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    private EditText inputPhone, inputPassword;
    private FirebaseAuth auth;
    private boolean isFound;
    private DatabaseReference dbUser;
   private FirebaseDatabase user;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private TextView txtSignUp;
    private TextView frgtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        dbUser = FirebaseDatabase.getInstance().getReference("User");

        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        frgtPass = (TextView) findViewById(R.id.frgtPass);
        frgtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        inputPhone = (EditText) findViewById(R.id.loginphone);
        inputPassword = (EditText) findViewById(R.id.loginpassword);
        btnLogin = (Button) findViewById(R.id.signin);
        mDialog = new ProgressDialog(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = inputPhone.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(phone.isEmpty()){
                    Toast.makeText(Login.this, "You must provide Phone Number ", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(Login.this, "You must provide password", Toast.LENGTH_SHORT).show();
                }else{
                    logInUsers(phone, password);
                }
            }
        });
    }
    private void logInUsers(final String phone, final String password){
        mDialog.setMessage("Please wait...");
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
//        dbUser.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    for (DataSnapshot ds :dataSnapshot.getChildren()){
//                        Users users = ds.getValue(Users.class);
//
//                        if(users.getUser_phone().equals(phone) && Users.getUser_password().equals(password))
//                        {
//                             isFound = true;
//                             Users.user_phone = inputPhone.getText().toString();
//                             Intent intent = new Intent(Login.this , MainActivity.class);
//                             startActivity(intent);
//                             finish();
//                        }
//                        else {
//                            isFound = false;
//                        }
//                        if(!isFound){
//                            Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_SHORT).show();
//                        }
//
//                        }
//                    }
//                }
//

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        PhoneAuthProvider auth = PhoneAuthProvider.getInstance();

        auth.verifyPhoneNumber(inputPhone.getText().toString(), 60, TimeUnit.SECONDS, Login.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mDialog.dismiss();
                        Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);

                        finish();

                    }
                });
            }


            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.i("dxdiag", e.getMessage());

            }
        });
    }
}




    /*setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin(inputEmail.getText().toString(), inputPassword.getText().toString());
            }
        });

    }

    private void signin(final String username, final String pass) {
        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users login = dataSnapshot.child(username).getValue(Users.class);
                if (login.getUsername().equals(username)) {
                    if (!username.isEmpty()) {
                       // Users login = dataSnapshot.child(username).getValue(Users.class);
                        if (login.getPassword().equals(pass)){
                            Toast.makeText(Login.this,"login",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Login.this,"failed",Toast.LENGTH_LONG).show();

                        }
                    }
                    else {
                        Toast.makeText(Login.this,"Not Registered member",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
*/