package com.kata.kidspe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class Welcome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_welcome);
        final MediaPlayer bgmusic = MediaPlayer.create(this, R.raw.welcome);
        bgmusic.start();
        RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.mainlayout);
        rlayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(Welcome.this);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }

        });


    }


}