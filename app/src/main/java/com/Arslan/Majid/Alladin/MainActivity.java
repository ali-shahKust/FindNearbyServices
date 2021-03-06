package com.Arslan.Majid.Alladin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Arslan.Majid.Alladin.Prevalent.Prevalent;
import com.Arslan.Majid.Alladin.entities.Servicesfinder;
import com.Arslan.Majid.Alladin.entities.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private    DrawerLayout drawer;
    private Fragment fragment;
    private DatabaseReference mRootref;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new Dashboard());
        tx.commit();

        mRootref = FirebaseDatabase.getInstance().getReference("User");
        //DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("User");


        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Dashboard");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        final TextView userNametxt = headerView.findViewById(R.id.UsernameTxt);


        final TextView userPhoneNumber = headerView.findViewById(R.id.PhoneNumberTxt);

        final ImageView myImageview = headerView.findViewById(R.id.myImageview);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        FirebaseUser current_user_id = mAuth.getCurrentUser();
        final String uid=current_user_id.getUid();

        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String username = dataSnapshot.child("user_name").getValue().toString();
                    String phonenum = dataSnapshot.child("user_phone").getValue().toString();
//                   String getimage = dataSnapshot.child("image").getValue().toString();
                    userNametxt.setText(username);
                    System.err.println("User name is " + username);
                    userPhoneNumber.setText(phonenum);

                   // Picasso.get().load(getimage).placeholder(R.drawable.profile).into(myImageview);



        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference().child("Profile_image").child(uid);
        imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

try {
    String getimage = dataSnapshot.child("image").getValue().toString();
    Picasso.get().load(getimage).placeholder(R.drawable.profile).into(myImageview);

}
catch (Exception err){
    err.getMessage();
}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
        @Override
    public void onBackPressed() {
         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass;
        switch (item.getItemId()) {


            case R.id.dashboard:
                fragmentClass = Dashboard.class;
                break;
            case R.id.findservice:
                fragmentClass = FindService.class;
                break;
            case R.id.profilesetting:
                fragmentClass = ProfileSetting.class;
                break;
            case R.id.logout:

                fragmentClass = Logout.class;


                break;
            case R.id.contact:
                fragmentClass = ContactUs.class;
                break;
            default:
                fragmentClass = Dashboard.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        drawer.closeDrawers();
        return true;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.nav_header_main, container, false);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser== null) {
            Intent intent = new Intent(MainActivity.this , Login.class);
            startActivity(intent);
            finish();
        }
    }


//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser== null){
//            Intent intent = new Intent(MainActivity.this, Login.class);
//            startActivity(intent);
//            finish();
//        }
//    }
}
