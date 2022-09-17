package com.kata.kidspe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import static com.kata.kidspe.StartKuiz.RESULTQUIZ;

public class FinishKuiz extends AppCompatActivity {

    TextView finalResult;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_kuiz);
        finalResult = findViewById(R.id.finalResult);
        final MediaPlayer applauseSound = MediaPlayer.create(this, R.raw.applause);
        applauseSound.start();
        SharedPreferences pref = getSharedPreferences(RESULTQUIZ, MODE_PRIVATE);
        int quizScore = pref.getInt("quizScore", 0);
        finalResult.setText(String.valueOf(quizScore) + "/10 ");
        lottieAnimationView = findViewById(R.id.animationStar);
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
            }


            @Override
            public void onAnimationEnd(Animator animator) {
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onAnimationCancel(Animator animator) {

            }


            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }



    @Override
    public void onBackPressed() {
        Intent i = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);



                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}