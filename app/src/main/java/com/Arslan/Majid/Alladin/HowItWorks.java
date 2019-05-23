package com.Arslan.Majid.Alladin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.Arslan.Majid.Alladin.adapter.SliderAdapter;


public class HowItWorks extends AppCompatActivity {
    private ViewPager viewPager;
    private SliderAdapter slideAdapter;
    private Button btnL, btnG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_works);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //for slider
        slideAdapter = new SliderAdapter(this);
        viewPager.setAdapter(slideAdapter);

        //for tab dots
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
        btnL = (Button) findViewById(R.id.btnL);
        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HowItWorks.this, Login.class);
                startActivity(intent);
            }
        });
        btnG = (Button) findViewById(R.id.btnGetStarted);
        btnG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HowItWorks.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
