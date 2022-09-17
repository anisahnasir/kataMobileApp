package com.kata.kidspe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.kata.kidspe.Adapter.MyAdapter;
import com.kata.kidspe.Listener.FirebaseHelper;
import com.kata.kidspe.Model.ImageSwipe;
import com.kata.kidspe.Transformer.DepthPageTransformer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SlideImage extends AppCompatActivity implements FirebaseHelper {

    ViewPager viewPager;
    MyAdapter adapter;

    DatabaseReference images;

    FirebaseHelper firebaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_image);

        images = FirebaseDatabase.getInstance().getReference("Image");

        //Init Event
        firebaseHelper = this;

        loadImage();

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());


    }

    private void loadImage() {
        images.addValueEventListener(new ValueEventListener() {
            List<ImageSwipe> imageSwipeList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot imageSnapshot:snapshot.getChildren())
                    imageSwipeList.add(imageSnapshot.getValue(ImageSwipe.class));
                firebaseHelper.onFirebaseLoadSuccess(imageSwipeList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                firebaseHelper.onFirebaseLoadFailed(error.getMessage());

            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<ImageSwipe> imageSwipeList) {
        adapter = new MyAdapter(this, imageSwipeList);
        viewPager.setAdapter(adapter);


    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(this,""+message,Toast.LENGTH_SHORT).show();

    }
}