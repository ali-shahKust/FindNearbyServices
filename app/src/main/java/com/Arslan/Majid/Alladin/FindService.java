package com.Arslan.Majid.Alladin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Arslan.Majid.Alladin.adapter.ServiceAdapter;
import com.Arslan.Majid.Alladin.adapter.ServiceViewHolder;
import com.Arslan.Majid.Alladin.entities.Servicesfinder;
import com.Arslan.Majid.Alladin.entities.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FindService extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;

    ServiceAdapter adapter;
    List<Servicesfinder> mList;

    public FindService() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.content_main, container, false);


        mUserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = view.findViewById(R.id.recyclermenu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mList = new ArrayList<>();
        adapter = new ServiceAdapter(getContext(),mList);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Servicesfinder> options =
                new FirebaseRecyclerOptions.Builder<Servicesfinder>()
                        .setQuery(mUserRef, Servicesfinder.class)
                        .build();


        FirebaseRecyclerAdapter<Servicesfinder, ServiceViewHolder> adapter =
                new FirebaseRecyclerAdapter<Servicesfinder, ServiceViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ServiceViewHolder holder, int position, @NonNull Servicesfinder model)
                    {
                        holder.UserName.setText(model.getUser_name());
                        holder.UserPhone.setText(model.getUser_phone());
                        holder.UserRole.setText(model.getUser_role());
                    }

                    @NonNull
                    @Override
                    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_find, parent, false);
                        ServiceViewHolder holder = new ServiceViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        //         DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot.exists()){
//                    mList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()){
////                    mList.clear();
//                    ds.getKey();
//
//                   Servicesfinder obj = ds.getValue(Servicesfinder.class);
//                    mList.add(obj);
//
//                    System.err.println("values :"+ ds.getValue().toString());
//
////                    Servicesfinder.user_name = ds.getValue().toString();
////                    Servicesfinder.user_phone = ds.getValue().toString();
////                    Servicesfinder.user_role = ds.getValue().toString();
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//
//
//
//
//                }
//
//                }else {
//                    mList.clear();
//                    Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



    }
}
