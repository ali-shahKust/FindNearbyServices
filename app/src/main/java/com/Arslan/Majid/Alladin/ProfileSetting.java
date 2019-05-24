package com.Arslan.Majid.Alladin;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ProfileSetting extends Fragment {
    private ImageView iv;
    private Button editprofile,changepassword;
    private TextView name, phone,gender,age,blood;

    public ProfileSetting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile_setting, container, false);



        editprofile=(Button)view.findViewById(R.id.editProfile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        changepassword=(Button)view.findViewById(R.id.ChangePassword);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);

            }
        });

        return view;
    }
}