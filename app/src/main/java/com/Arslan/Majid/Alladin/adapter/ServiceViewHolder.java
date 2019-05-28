package com.Arslan.Majid.Alladin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.*;
//import com.Arslan.Majid.Alladin.R;
import com.Arslan.Majid.Alladin.R;

import com.Arslan.Majid.Alladin.InterFace.ItemClickListener;

public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    public TextView UserName , UserRole, UserPhone;
    public ItemClickListener itemClickListener;

    public ServiceViewHolder(@NonNull View itemView) {

        super(itemView);

        UserName = (TextView) itemView.findViewById(R.id.usernameview);
        UserPhone = (TextView) itemView.findViewById(R.id.usernumview);
        UserRole= (TextView) itemView.findViewById(R.id.role);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view , getAdapterPosition() , false);
    }


}
