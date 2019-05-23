package com.Arslan.Majid.Alladin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.Arslan.Majid.Alladin.R;
public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    //Images for slider
    public int[] lst_images = {
            R.drawable.edit,
            R.drawable.bar_chart,
            R.drawable.healthy
    };
    //Titles for slider
    public String[] lst_title = {
            "Take Notes of Your Daily Vitals",
            "Check Monthly Graphs",
            "Stay Healthy"

    };
    //Description for slider
    public String[] lst_description = {
            "Note down your vitals like BP, Heart Rate, Temperature and access it on a single click.",
            "Check the monthly or week wise graph against your vitals.",
            "We prefer your health first."
    };

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst_title.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide, container, false);

        ImageView slideimage = (ImageView) view.findViewById(R.id.slideImage);
        TextView slideTitle = (TextView) view.findViewById(R.id.slideTitle);
        TextView slideDesc = (TextView) view.findViewById(R.id.slideDesc);

        slideimage.setImageResource(lst_images[position]);
        slideTitle.setText(lst_title[position]);
        slideDesc.setText(lst_description[position]);

        container.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
