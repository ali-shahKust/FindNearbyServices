package com.Arslan.Majid.Alladin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Arslan.Majid.Alladin.InterFace.ItemClickListener;

public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public ImageView image;

    public ItemClickListener itemClickListener;
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);

       // image = itemView.findViewById(R.id.myImageview);
    }
    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }


    @Override
    public void onClick(View view) {

    }
}
