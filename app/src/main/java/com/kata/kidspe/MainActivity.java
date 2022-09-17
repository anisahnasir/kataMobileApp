package com.kata.kidspe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import static com.kata.kidspe.ViewImageActivity.SCORE;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference categoryReference;
    FirebaseRecyclerOptions<CategoryItem> options;
    FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        final MediaPlayer haiwan = MediaPlayer.create(this, R.raw.newhaiwan);
        final MediaPlayer nombor = MediaPlayer.create(this, R.raw.nombor);
        final MediaPlayer warna = MediaPlayer.create(this, R.raw.warna);
        final MediaPlayer buah = MediaPlayer.create(this, R.raw.buah);
        final MediaPlayer badan = MediaPlayer.create(this, R.raw.badan);
        final MediaPlayer kuiz = MediaPlayer.create(this, R.raw.kuiz);

        categoryReference = FirebaseDatabase.getInstance().getReference().child("Category");
//        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
//        bottomNav.setOnNavigationItemSelectedListener(navListener);


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
                        Log.d("myTag", "This is model.getname " + Utils.CATEGORY_ID);
                        if (Utils.CATEGORY_ID.equals("01"))
                        {

                            haiwan.start();
                            Log.d("myTag", "masuk if firsts tatement ");
                            Intent i = new Intent(MainActivity.this, AnimalCategories.class);
                            startActivity(i);
                        }
                        else if(Utils.CATEGORY_ID.equals("02"))
                        {
                            nombor.start();
                            Intent i = new Intent(MainActivity.this, ListItemActivity.class);
                            startActivity(i);
                        }
                        else if(Utils.CATEGORY_ID.equals("03"))
                        {
                            warna.start();
                            Intent i = new Intent(MainActivity.this, ListItemActivity.class);
                            startActivity(i);
                        }
                        else if(Utils.CATEGORY_ID.equals("04"))
                        {
                            buah.start();
                            Intent i = new Intent(MainActivity.this, ListItemActivity.class);
                            startActivity(i);
                        }
                        else if(Utils.CATEGORY_ID.equals("05"))
                        {
                            badan.start();
                            Intent i = new Intent(MainActivity.this, ListItemActivity.class);
                            startActivity(i);
                        }
                        else if (Utils.CATEGORY_ID.equals("09"))
                        {
                            kuiz.start();
                            Intent i = new Intent(MainActivity.this, Kuiz.class);
                            startActivity(i);

                        }
                        else
                        {
                            Log.d("myTag", "masuk second else");
                            Intent i = new Intent(MainActivity.this, ListItemActivity.class);
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


//    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    Fragment selectedFragment = null;
//
//                    switch (item.getItemId()) {
//                        case R.id.home:
//                            ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
//                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(i, option.toBundle());
//                            break;
//                        case R.id.quiz:
//                            ActivityOptions option2 = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
//                            //have to make validation?? score cukup ke tak to answer quiz
//                            Intent j = new Intent(getApplicationContext(), Kuiz.class);
//                            startActivity(j, option2.toBundle());
//                            break;
////                        case R.id.result:
////                            ActivityOptions option3 = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
////                            Intent k = new Intent (getApplicationContext(), Rekod.class);
////                            startActivity(k,option3.toBundle());
////                            break;
//                        default:
//
//                    }
//
//                    return true;
//                }
//            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reset, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                alertDialog.setTitle("Adakah anda mahu menetapkan semula skor?");
                alertDialog.setCancelable(false);

                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //moveTaskToBack(false);
                        SharedPreferences imageScore = getApplicationContext().getSharedPreferences(SCORE, MODE_PRIVATE);
                        imageScore.edit().clear().commit();

                    }
                });

                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.show();



                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    //    public void logout(View view) {
//        //this method will remove session and open login screen
//        SessionManager sessionManager = new SessionManager(MainActivity.this);
//        sessionManager.removeSession();
//
//        moveToLogin();
//
//    }
//
//    public void moveToLogin(){
//        Intent intent = new Intent(MainActivity.this,Login.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
/*
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
    protected void onStop() {
        super.onStop();
        if(adapter!= null)
        {
            adapter.stopListening();
        }

    }

 */
}