package com.Arslan.Majid.Alladin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.Arslan.Majid.Alladin.Prevalent.currentOnlineUser;
import com.Arslan.Majid.Alladin.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class ProfileSetting extends Fragment {
    private ImageView imageview;
    private Button editprofile;
    private EditText editName;
    private Uri imageUri;
    private String saveCurrentDate, saveCurrentTime, ProductRandomKey, downloadImgUrl;
    private static final int GallaryPick = 1;

    private StorageReference productImagesRef;

    private DatabaseReference mRootRef;

    private ProgressDialog mprogressBar;

    private FirebaseAuth mAuth;
    boolean exists = false;


    public ProfileSetting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_profile_setting, container, false);
        checkImage();
//getting Button and Txt ID
        editprofile = (Button) view.findViewById(R.id.editProfile);
        editName = (EditText) view.findViewById(R.id.editName);
        imageview = (ImageView) view.findViewById(R.id.getProfileImg);

        FirebaseUser current_user_id = mAuth.getCurrentUser();
        final String uid = current_user_id.getUid();
        final DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference().child("Profile_image").child(uid);
        imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

try {
   String getimage = dataSnapshot.child("image").getValue().toString();
    Picasso.get().load(getimage).placeholder(R.drawable.profile).into(imageview);
}
catch (Exception error){
    error.getMessage();

}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String username = dataSnapshot.child("user_name").getValue().toString();
//                     String getimage = dataSnapshot.child("image").getValue().toString();
                editName.setText(username);


                // Picasso.get().load(getimage).placeholder(R.drawable.profile).into(myImageview);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                OpenGalleryImage();

            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateImage();
                ValidateName();
            }
        });


        return view;
    }

    private void ValidateName() {
        FirebaseUser current_user_id = mAuth.getCurrentUser();
        final String uid = current_user_id.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("user_name", editName.getText().toString());

        ref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Name Updated successfully.", Toast.LENGTH_SHORT).show();

                }
                if (!task.isSuccessful()) {
                    task.getException();
                }


            }

        });

    }

    private void ValidateImage() {
        if (imageUri == null) {
            if (exists) {
                //TODO: update name only
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Select an image first", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!editName.getText().toString().trim().equals("")) {
                StoreProductInformation();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void OpenGalleryImage() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GallaryPick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GallaryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageview.setImageURI(imageUri);

        }
    }

    private void StoreProductInformation() {


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd , yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //Image link for Product
        // ProductRandomKey = saveCurrentDate+saveCurrentTime;
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Users").child("user_image");
        FirebaseUser current_user_id = mAuth.getCurrentUser();
        final String uid = current_user_id.getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference("Profile_image").child(uid);
        final StorageReference filepath = productImagesRef.child(uid + ".jpg");
        // imageUri.getLastPathSegment()+ProductRandomKey

        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImgUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }


                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImgUrl = task.getResult().toString();


                            Toast.makeText(getActivity(), "Getting Url of Image", Toast.LENGTH_SHORT).show();

                            mRootRef.setValue(downloadImgUrl);

                            SaveProductInfo();


                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfo() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("image", downloadImgUrl);

        mRootRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Toast.makeText(getActivity(), "Image Upload Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                    // mprogressBar.dismiss();
                } else {
                    //mprogressBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(getActivity(), "Error " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void checkImage() {
        FirebaseDatabase.getInstance().getReference("Profile_image").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    exists = true;
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        String image_url = i.getValue(String.class);
                        if (image_url != null && image_url.equals("")) {
                            Picasso.get().load(imageUri).into(imageview);
                        }
                    }
                } else {
                    exists = false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}