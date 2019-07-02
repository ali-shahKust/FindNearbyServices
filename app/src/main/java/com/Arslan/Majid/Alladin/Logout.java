package com.Arslan.Majid.Alladin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;


public class Logout extends Fragment {

   private FirebaseAuth mauth = FirebaseAuth.getInstance();

   String uid = mauth.getCurrentUser().getUid();


    public Logout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view= inflater.inflate(R.layout.fragment_logout, container, false);
        Intent intent = new Intent(getActivity(), Login.class);
      startActivity(intent);
      mauth.signOut();
      getActivity().finish();

        return view;
    }
}