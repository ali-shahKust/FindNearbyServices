package com.Arslan.Majid.Alladin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Arslan.Majid.Alladin.entities.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    DatabaseReference dbUser;
    FirebaseDatabase user;
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

        inputEmail = (EditText) findViewById(R.id.loginemail);
        inputPassword = (EditText) findViewById(R.id.loginpassword);
        btnLogin = (Button) findViewById(R.id.signin);
        mDialog = new ProgressDialog(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(email.isEmpty()){
                    Toast.makeText(Login.this, "You must provide email", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(Login.this, "You must provide password", Toast.LENGTH_SHORT).show();
                }else{
                    logInUsers(email, password);
                }
            }
        });
    }
    private void logInUsers(String email, String password){
        mDialog.setMessage("Please wait...");
        mDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if(!task.isSuccessful()){
                    //error loging
                    Toast.makeText(Login.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                   Intent intent= new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}/*setOnClickListener(new View.OnClickListener() {
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