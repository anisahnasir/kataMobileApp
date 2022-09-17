package com.kata.kidspe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kata.kidspe.CategoryViewHolder.ItemViewHolder;
import com.kata.kidspe.Model.ImageItem;
import com.kata.kidspe.Utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ListItemActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    Query query;

    FirebaseRecyclerOptions<ImageItem> options;
    FirebaseRecyclerAdapter<ImageItem, ItemViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewItem);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        query = FirebaseDatabase.getInstance().getReference("Image").orderByChild("categoryID").equalTo(Utils.CATEGORY_ID);

        options = new FirebaseRecyclerOptions.Builder<ImageItem>().setQuery(query,ImageItem.class).build();


        adapter = new FirebaseRecyclerAdapter<ImageItem, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position, @NonNull final ImageItem model) {

                Picasso.get().load(model.getImageLink()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageview, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                            Picasso.get().load(model.getImageLink()).error(R.drawable.ic_baseline_terrain_24).into(holder.imageview);
                    }
                });

                holder.imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.CATEGORY_ID = adapter.getRef(position).getKey();
                        Utils.CATEGORY_SELECTED = model.categoryID;
                        Utils.IMAGE_SELECTED_NAME = model.name;
                        Utils.selected_image = model;
                        Utils.DIGIT_SELECTED = model.digit;
                        Intent i = new Intent(getApplicationContext(),ViewImageActivity.class);
                        startActivity(i);

                    }
                });

            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item,parent,false);
                int height = parent.getMeasuredHeight() / 2;
                v.setMinimumHeight(height);
                return new ItemViewHolder(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);




    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!= null)
        {
            adapter.startListening();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(adapter!= null)
        {
            adapter.startListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adapter!= null)
        {
            adapter.stopListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!= null)
        {
            adapter.stopListening();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}