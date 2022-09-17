package com.kata.kidspe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kata.kidspe.CategoryViewHolder.CategoryViewHolder;
import com.kata.kidspe.Model.CategoryItem;
import com.kata.kidspe.Utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class AnimalCategories extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference categoryReference;
    FirebaseRecyclerOptions<CategoryItem> options;
    FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_categories);
        final MediaPlayer haiwanliar = MediaPlayer.create(this, R.raw.haiwanliar);
        final MediaPlayer haiwanladang = MediaPlayer.create(this, R.raw.haiwanladang);
        final MediaPlayer haiwanpeliharaan = MediaPlayer.create(this, R.raw.haiwanpeliharaan);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        categoryReference = FirebaseDatabase.getInstance().getReference().child("AnimalTypes");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button



        options = new FirebaseRecyclerOptions.Builder<CategoryItem>().setQuery(categoryReference, CategoryItem.class).build();
        adapter = new FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position, @NonNull final CategoryItem model) {

                Picasso.get().load(model.getImageLink()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(model.getImageLink()).error(R.drawable.ic_baseline_terrain_24).into(holder.imageView);

                    }
                });

                //holder.textView.setText(model.getName());

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.CATEGORY_ID = adapter.getRef(position).getKey();
                        Utils.CATEGORY_SELECTED = model.getName();
                        if (Utils.CATEGORY_ID.equals("06"))
                        {

                            haiwanliar.start();
                            Log.d("myTag", "masuk if firsts tatement ");
                            Intent i = new Intent(getApplicationContext(), ListItemActivity.class);
                            startActivity(i);
                        }
                        else if(Utils.CATEGORY_ID.equals("07"))
                        {
                            haiwanladang.start();
                            Intent i = new Intent(getApplicationContext(), ListItemActivity.class);
                            startActivity(i);
                        }
                        else if(Utils.CATEGORY_ID.equals("08"))
                        {
                            haiwanpeliharaan.start();
                            Intent i = new Intent(getApplicationContext(), ListItemActivity.class);
                            startActivity(i);
                        }

                    }
                });


            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item, parent, false);
                return new CategoryViewHolder(v);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }




    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}