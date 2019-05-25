package com.Arslan.Majid.Alladin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Arslan.Majid.Alladin.adapter.ServiceViewHolder;
import com.Arslan.Majid.Alladin.entities.Servicesfinder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class FindService extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;

    public FindService() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.content_main, container, false);


        recyclerView = view.findViewById(R.id.recyclermenu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        mRootRef = FirebaseDatabase.getInstance().getReference().child("User");
        FirebaseRecyclerOptions<Servicesfinder> options = new FirebaseRecyclerOptions.Builder<Servicesfinder>().setQuery(
                mRootRef , Servicesfinder.class
        ).build();

        FirebaseRecyclerAdapter<Servicesfinder , ServiceViewHolder> adapter = new FirebaseRecyclerAdapter<Servicesfinder, ServiceViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ServiceViewHolder holder, int position, @NonNull Servicesfinder model) {

                holder.UserName.setText(model.getUser_name());
                holder.UserPhone.setText(model.getUser_phone());
                holder.UserRole.setText( model.getUser_role());

            }

            @NonNull
            @Override
            public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_find ,viewGroup , false);

                ServiceViewHolder holder = new ServiceViewHolder(view);

                return holder;

            }
        };

        recyclerView.setAdapter(adapter);
       adapter.startListening();

    }
}
