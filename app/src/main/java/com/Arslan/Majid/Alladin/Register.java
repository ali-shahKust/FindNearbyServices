package com.Arslan.Majid.Alladin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;



import com.Arslan.Majid.Alladin.entities.Users;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    private TextView signin;
    private EditText fullname, phone, age, password, confirmpassword;
    double lat,log;
    private Spinner role;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Button signup;
    GoogleMap mGoogleMap;
    DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbUser = FirebaseDatabase.getInstance().getReference("User");

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
        confirmpassword = (EditText) findViewById(R.id.registerconfirmpassword);
        role = (Spinner) findViewById(R.id.registerrole);
        signup = (Button) findViewById(R.id.register);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUser();
            }
        });

    }

    private void AddUser() {
        final String name1 = fullname.getText().toString();
        String Phone = phone.getText().toString();
        String pass = password.getText().toString();
        String age1 = age.getText().toString();
        final String role1 = role.getSelectedItem().toString();


        if (!TextUtils.isEmpty(Phone)){


            PhoneAuthProvider auth = PhoneAuthProvider.getInstance();
         auth.verifyPhoneNumber(phone.getText().toString(), 60, TimeUnit.SECONDS, Register.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
             @Override
             public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                 HashMap<String, String> map = new HashMap<String, String>();
                 map.put("user_name", name1);
                 map.put("user_phone", phone.getText().toString());
                 map.put("user_role", role1);
                 map.put("user_age", age.getText().toString().trim());
                 dbUser.push()
                         .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()) {
                             Toast.makeText(Register.this, "CALLED", Toast.LENGTH_LONG).show();
                             Intent intent = new Intent(Register.this,MainActivity.class);
                             startActivity(intent);
                             finish();
                         } else {
                             Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     }
                 });


             }

             @Override
             public void onVerificationFailed(FirebaseException e) {

                 e.getMessage();

             }
         });

        }
        else {
            Toast.makeText(this,"Enter Phone Number please",Toast.LENGTH_LONG).show();
        }
    }

}

