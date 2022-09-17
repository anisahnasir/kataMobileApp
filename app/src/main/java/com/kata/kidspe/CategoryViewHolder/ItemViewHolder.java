package com.kata.kidspe.CategoryViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kata.kidspe.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageview;
    public TextView textView;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        imageview =  (ImageView)itemView.findViewById(R.id.imageViewItem);
        //textView = (TextView)itemView.findViewById(R.id.textViewImage);

    }


}
