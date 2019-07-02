package com.Arslan.Majid.Alladin;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Arslan.Majid.Alladin.directionhelpers.FetchURL;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Dashboard extends Fragment implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private FirebaseAuth mAuth;
    private static final int PERMISSIONS_REQUEST = 100;

    private Polyline currentPolyline;
    LatLng latLng;
    ArrayList<MyModel> myModelArrayList = new ArrayList<>();

    public Dashboard() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


        mAuth = FirebaseAuth.getInstance();

        //  MapFragment  mapFragment = (MapFragment) .findFragmentById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getUsersLocation();
        getLocation();


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        connectionResult.getErrorMessage();

    }

    private void getUsersLocation() {
        final ArrayList<String> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mMap.clear();
                    for (DataSnapshot i : dataSnapshot.getChildren()) {

                        try{
                            Log.e("dbuser", i.toString());
                        } catch (Exception e){
                            e.printStackTrace();
                        }



                        MyModel obj = i.getValue(MyModel.class);

                        myModelArrayList.add(obj);


                        System.err.println("data is " + obj.user_role + "  " + obj.user_name);


                    }

                    for (int i = 0; i < myModelArrayList.size(); i++) {

                        final MyModel myModel = myModelArrayList.get(i);



                        MarkerOptions options = new MarkerOptions().position(new LatLng(Double.parseDouble(myModel.latitude),
                                Double.parseDouble(myModel.longitude))).title(myModel.user_name).snippet(myModel.user_number);

                        switch (myModel.user_role) {
                            case "Electrician": {
                                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.electrition));
                                break;
                            }
                            case "Mechanic": {
                                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.mechan));
                                break;
                            }
                            case "Vehicle Recovery": {
                                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.viechel));
                                break;
                            }
                            case "Puncture Master": {
                                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.puncher));
                                break;
                            }
                            default: {
                                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.boy));
                                break;
                            }

                        }

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(options.getPosition()));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                        Marker marker = mMap.addMarker(options);


                        marker.showInfoWindow();


                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(final Marker marker) {


                                // load detail view of contacts

                                try {
                                    Log.e("email", myModel.user_phone);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                                if (myModel.user_phone!= mAuth.getCurrentUser().getEmail()) {

                                    AndroidNetworking.post("https://translationchatapp.herokuapp.com/sendPushNotification/")
                                            .addBodyParameter("user_id", myModel.user_phone)
                                            .addBodyParameter("message", "Your services are needed!")
                                            .addBodyParameter("lat", myModel.latitude)
                                            .addBodyParameter("lng", myModel.longitude)
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

                                }

                                final CharSequence[] items = {"Call", "SMS"};
                                String var = marker.getTitle();

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("User Name : " + var);

                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {

                                        if (items[item].equals("Call")) {
                                            // invoke call functionality
                                            onCall();

                                        }

                                        if (items[item].equals("SMS")) {
                                            onSms();
                                            // invoke sms functionality

                                        }
                                    }

                                    private void onSms() {
                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                        String var = marker.getSnippet();
                                        sendIntent.setData(Uri.parse("sms:" + var));
                                        startActivity(sendIntent);
                                    }

                                    private void onCall() {
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                        String var = marker.getSnippet();
                                        callIntent.setData(Uri.parse("tel:" + var));
                                        Intent chooser = Intent.createChooser(callIntent, "Phone");
                                        startActivity(chooser);
                                    }






                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                                alert.setCanceledOnTouchOutside(false);


                                return false;
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getLocation() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getActivity());
        client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();



                    final FirebaseUser current_user_id = mAuth.getCurrentUser();
                    final String uid = current_user_id.getUid();

                    try {
                        Log.e("uid", uid);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("aladin", "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    // Get new Instance ID token
                                    String token = task.getResult().getToken();




                                    Log.d("token",token);
                                    // Log and toast
                                  /*  String msg = getString(R.string.msg_token_fmt, token);

                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();*/
                                }
                            });





                    FirebaseMessaging.getInstance().subscribeToTopic(uid)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = "Subscribed";
                                    if (!task.isSuccessful()) {
                                        msg = "Subscription failed";
                                    }
                                    Log.e("sub", msg);
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });




                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("latitude", String.valueOf(location.getLatitude()));
                    userMap.put("longitude", String.valueOf(location.getLongitude()));
                    ref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Location Updated successfully.", Toast.LENGTH_SHORT).show();

                            }
                            if (!task.isSuccessful()) {
                                task.getException();
                            }


                        }

                    });


                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Subscribe
    public void onLatLngReceived(NotificationEvent notificationEvent){

            new FetchURL(getActivity()).execute(getUrl(latLng, notificationEvent.latLng, "driving"), "driving");


    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_directions);
        return url;
    }



    @Subscribe
    public void drawLines(LineEvent lineEvent){
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline(lineEvent.polylineOptions);
    }

}





class MyModel {
    public String latitude, longitude, user_role, user_name, user_number, user_phone;

}