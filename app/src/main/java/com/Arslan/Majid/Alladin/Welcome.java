package com.Arslan.Majid.Alladin;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

public class Welcome extends AppCompatActivity {
    private TextView title;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        AndroidNetworking.initialize(getApplicationContext());

        FirebaseApp.initializeApp(getApplicationContext());
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        imageView = (ImageView) findViewById(R.id.imgLogo);

        title = (TextView) findViewById(R.id.logoText);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Mafakanev.otf");
        title.setTypeface(typeFace);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);

        title.startAnimation(animation);
        imageView.startAnimation(animation);

        final Intent intent = new Intent(this, Login.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}

