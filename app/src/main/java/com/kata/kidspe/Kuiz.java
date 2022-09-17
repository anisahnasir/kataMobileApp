package com.kata.kidspe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import static com.kata.kidspe.StartKuiz.RESULTQUIZ;
import static com.kata.kidspe.ViewImageActivity.SCORE;

public class Kuiz extends AppCompatActivity {

    public int totalImages = 47;
    public int totalScore = 47;
    public int scoreNeededForEveryWord = 1;// for testing purpose, change back to 0 later
    public TextView totalScoreView, totalNumberOfWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuiz);

        totalScoreView = findViewById(R.id.totalScore);
        totalNumberOfWords = findViewById(R.id.totalNumberOfWords);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        checkEligibilityQuiz();
        totalScoreView.setText(String.valueOf(totalScore));
        totalNumberOfWords.setText(String.valueOf(totalImages));
        //Toast.makeText(getApplicationContext(), "Skor terkini: " + String.valueOf(totalScore), Toast.LENGTH_LONG).show();

    }

    public void startQuiz(View view) throws InterruptedException {
        //kena edit later

        if (totalScore == totalImages) {
            SharedPreferences.Editor editor = getSharedPreferences(RESULTQUIZ, MODE_PRIVATE).edit();
            SharedPreferences.Editor editor2 = getSharedPreferences("ImageAdded", MODE_PRIVATE).edit();
            //SharedPreferences.Editor editor2 = getSharedPreferences(ARRAYIMAGE, MODE_PRIVATE).edit();
            editor2.clear();
            editor2.apply();
            editor.putInt("questionNumber", 0);
            editor.putInt("quizScore", 0);
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), StartKuiz.class);
            startActivity(intent);
        }
        else {

            // mySnackbar.show();
            MediaPlayer cukupkanSkor = MediaPlayer.create(this, R.raw.oops);
            cukupkanSkor.start();
            Toast.makeText(getApplicationContext(),"Oops! Adik perlu cukupkan skor terlebih dahulu sebelum mengambil kuiz ini",Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);

        }


    }


    public void checkEligibilityQuiz() {


        SharedPreferences imageScore = getApplicationContext().getSharedPreferences(SCORE, MODE_PRIVATE);
        //int imgScore = imageScore.getInt("ayam", 0); //0 is the default value.
        String[] images = {"ayam", "kucing", "anjing", "lembu", "kambing", "ular", "gajah", "satu", "dua", "tiga", "empat",
                "lima", "kuda", "enam", "tujuh", "lapan", "sembilan", "sepuluh", "hitam", "putih", "kuning", "jingga", "merah", "merah jambu",
                "biru", "hijau", "coklat", "anggur", "durian", "pisang", "rambutan", "nanas", "buah naga", "mangga",
                "kiwi", "delima", "kelapa", "mata", "telinga", "hidung", "mulut", "lidah", "rambut", "tangan", "kaki",
                "lutut", "kuku"};
        boolean eligible = false;
        for (int i = 0; i < images.length; i++) {
            int imgScore = imageScore.getInt(images[i], 0);
            Log.e("My total score:", String.valueOf(totalScore));
            if (imgScore >= scoreNeededForEveryWord) {
                totalScore = totalScore + 1;
            }
        }

        //Toast.makeText(getApplicationContext(), "Images array length: " + String.valueOf(images.length), Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}



//        SharedPreferences imageScore = getApplicationContext().getSharedPreferences(SCORE, MODE_PRIVATE);
//        int imgScore = imageScore.getInt(imageName, 0);



