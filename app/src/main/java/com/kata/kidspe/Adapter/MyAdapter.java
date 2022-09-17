package com.kata.kidspe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.kata.kidspe.Model.ImageSwipe;
import com.kata.kidspe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends PagerAdapter {

    Context context;
    List<ImageSwipe> imageSwipeList;
    LayoutInflater inflater;

    public MyAdapter(Context context, List<ImageSwipe> imageSwipeList) {
        this.context = context;
        this.imageSwipeList = imageSwipeList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageSwipeList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        //Inflate view
        View view = inflater.inflate(R.layout.view_pager_item,container,false);

        //View
        ImageView image_swipe = (ImageView)view.findViewById(R.id.image_slide);
        TextView image_title = (TextView)view.findViewById(R.id.image_title);
        TextView image_description = (TextView)view.findViewById(R.id.image_desc);
        FloatingActionButton btn_mic = (FloatingActionButton)view.findViewById(R.id.btn_mic);

        //Set Data
        Picasso.get().load(imageSwipeList.get(position).getImage()).into(image_swipe);
        image_title.setText(imageSwipeList.get(position).getName());
        image_description.setText(imageSwipeList.get(position).getDescription());

        //Event
        btn_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Test mic button", Toast.LENGTH_SHORT).show();

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Image clicked",Toast.LENGTH_SHORT).show();
            }
        });

        container.addView(view);
        return view;
    }
}
