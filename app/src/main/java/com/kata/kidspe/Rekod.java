package com.kata.kidspe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Rekod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekod);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =

            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.home:
                            ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(Rekod.this);
                            Intent i = new Intent (getApplicationContext(), MainActivity.class);
                            startActivity(i,option.toBundle());
                            return true;
                        case R.id.quiz:
                            ActivityOptions option2 = ActivityOptions.makeSceneTransitionAnimation(Rekod.this);
                            //have to make validation?? score cukup ke tak to answer quiz
                            Intent j = new Intent (getApplicationContext(), Kuiz.class);
                            startActivity(j,option2.toBundle());
                            return true;
//                        case R.id.result:
//                            ActivityOptions option3 = ActivityOptions.makeSceneTransitionAnimation(Rekod.this);
//                            Intent k = new Intent (getApplicationContext(), Rekod.class);
//                            startActivity(k,option3.toBundle());
//                            return true;
                        default:

                    }

                    return true;
                }
            };
}