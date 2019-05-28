package com.Arslan.Majid.Alladin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.Arslan.Majid.Alladin.*;

import com.Arslan.Majid.Alladin.entities.Servicesfinder;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    Context context;
    List<Servicesfinder> mList;

    public ServiceAdapter(Context context, List<Servicesfinder> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_find,viewGroup,false);
        ViewHolder v = new ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Servicesfinder servicesfinder = mList.get(i);
        viewHolder.txtUsername.setText(servicesfinder.getUser_name());
        viewHolder.txtService.setText(servicesfinder.getUser_role());
        viewHolder.txtPhone.setText(servicesfinder.getUser_phone());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUsername,txtService,txtPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUsername = itemView.findViewById(R.id.usernameview);
            txtService = itemView.findViewById(R.id.role);
            txtPhone = itemView.findViewById(R.id.usernumview);
        }
    }
}
