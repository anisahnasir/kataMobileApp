package com.kata.kidspe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;
import java.util.Random;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StartKuiz extends AppCompatActivity {

    private static final String ARRAYIMAGE = "ImageAdded";
    public ImageView micQuiz, quizCorrect, quizWrong, next;
    public TextView namaGambar, testSuara, scoreNumber;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference ref = firebaseDatabase.getReference().child("Image");
    public String gambar, nombor, idRandom;
    public SpeechRecognizer speechRecognizer;
    public static final Integer REQUEST_CODE = 1;
    public static final String RESULTQUIZ = "Score";
    public ArrayList<String> imageAdded = new ArrayList<>();
    public int quizScore, questionNumber, randomNo;
    public WebView gambarKuiz;
    public boolean dahJawab = false;
    private LottieAnimationView lottieAnimationView;
    public static int totalQuestionQuiz = 10;
    public int totalImages = 47;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_kuiz);


//        assert getSupportActionBar() != null;   //null check
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        final MediaPlayer correctSound = MediaPlayer.create(this, R.raw.hooray);
        final MediaPlayer wrongSound = MediaPlayer.create(this, R.raw.salah);
        final WebView wb;
        gambarKuiz = (WebView) findViewById(R.id.gambarKuiz);
        // gambarKuiz = findViewById(R.id.gambarKuiz);
        micQuiz = findViewById(R.id.micQuiz);
        quizCorrect = findViewById(R.id.quizCorrect);
        quizWrong = findViewById(R.id.quizWrong);
        //next = findViewById(R.id.next);
        //namaGambar =  findViewById(R.id.namaGambar);
        testSuara = findViewById(R.id.testSuara);
        scoreNumber = findViewById(R.id.scoreNumber);
        //namaGambar.setText(Utils.IMAGE_SELECTED_NAME);
        lottieAnimationView = findViewById(R.id.animationMic);


        String id = createRandomID();

//        SharedPreferences pref = getSharedPreferences(RESULTQUIZ, MODE_PRIVATE);
//        boolean adaKeTak = pref.contains("questionNumber");
//        if (adaKeTak)
//        {
//            questionNumber = pref.getInt("questionNumber", 0);
//            quizScore = pref.getInt("quizScore", 0); //0 is the default value.
//            //Toast.makeText(ViewImageActivity.this,Integer.toString(imgScore), Toast.LENGTH_SHORT).show();
//        } else
//        {
//            SharedPreferences.Editor editor = getSharedPreferences(RESULTQUIZ, MODE_PRIVATE).edit();
//            editor.putInt("quizScore",0);
//            editor.putInt("questionNumber",1);
//            editor.apply();
//
//        }

        updateQuestionNumber();

        DatabaseReference myRef = ref.child(id).child("gifLink");
        DatabaseReference myRefName = ref.child(id).child("name");
        DatabaseReference myRefNo = ref.child(id).child("digit");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = snapshot.getValue(String.class);
                //Picasso.get().load(url).into(gambarKuiz);

                gambarKuiz.setBackgroundColor(Color.TRANSPARENT);
                gambarKuiz.setBackgroundColor(0);
                gambarKuiz.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                gambarKuiz.loadUrl(url);
                gambarKuiz.getSettings().setLoadWithOverviewMode(true);
                gambarKuiz.getSettings().setUseWideViewPort(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRefName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gambar = dataSnapshot.getValue().toString();
                //namaGambar.setText(gambar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefNo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!= null){
                    nombor = dataSnapshot.getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StartKuiz.this, "problematic shit", Toast.LENGTH_SHORT).show();
            }
        });


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                testSuara.setHint("Getting ready to listen");
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.animate();
                lottieAnimationView.setProgress(0);
                lottieAnimationView.pauseAnimation();
                lottieAnimationView.playAnimation();
            }

            @Override
            public void onBeginningOfSpeech() {
                testSuara.setHint("Recording...");

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                testSuara.setText("Stopped listening");
                speechRecognizer.stopListening();
                lottieAnimationView.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                micQuiz.setImageResource(R.drawable.ic_baseline_mic_24);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                testSuara.setText(data.get(0));
                String result2 = data.get(0);
                dahJawab = true;
                if (result2.equalsIgnoreCase(gambar)) {

                    quizCorrect.setVisibility(View.VISIBLE);
                   // Toast.makeText(getApplicationContext(),"Betul!",Toast.LENGTH_LONG).show();
                    updateScore();
                    correctSound.start();

                }
                else if (result2.equalsIgnoreCase(nombor))
                {

                    quizCorrect.setVisibility(View.VISIBLE);
                    updateScore();
                    //Toast.makeText(getApplicationContext(),"Betul!",Toast.LENGTH_LONG).show();
                    correctSound.start();
                }
                else {
                    quizWrong.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(),"Salah!",Toast.LENGTH_LONG).show();
                    wrongSound.start();
                }



            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        micQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dahJawab){
                testSuara.setText("");
                speechRecognizer.startListening(speechRecognizerIntent);
                }
            }
        });


    }

    public String createRandomID(){
        Random rand = new Random();
        boolean checkSame = true;
        //something is wrong here. Caused by: java.lang.IndexOutOfBoundsException: Invalid index 49, size is 49
        // Generate random integers in range 0 to 49
        // int randomNo = rand.nextInt(10);
        randomNo = rand.nextInt(totalImages);
        ArrayList<String> imageID = new ArrayList<>();
        imageID.add("01");
        imageID.add("02");
        imageID.add("03");
        imageID.add("04");
        imageID.add("05");
        imageID.add("06");
        imageID.add("07");
        imageID.add("08");
        imageID.add("09");
        imageID.add("10");
        for (int i = 11; i < totalImages+1; i++) {
            imageID.add(String.valueOf(i));
        }
        imageID.remove(11);
        imageID.remove(14);


        idRandom = imageID.get(randomNo);
        SharedPreferences pref = getSharedPreferences(ARRAYIMAGE, MODE_PRIVATE);
        Log.d("pref:", String.valueOf(pref.getAll()));
        while(checkSame) {
            boolean adaKeTak = pref.contains(idRandom);
            if (adaKeTak) {
                idRandom = imageID.get(randomNo);
                //Toast.makeText(ViewImageActivity.this,Integer.toString(imgScore), Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = getSharedPreferences(ARRAYIMAGE, MODE_PRIVATE).edit();
                editor.putInt(idRandom, 0);
                editor.apply();
                checkSame = false;

            }
            randomNo = rand.nextInt(totalImages);
        }
//        while(checkSame) {
//            idRandom = imageID.get(randomNo);
//            if (!imageAdded.contains(idRandom)) {
//                imageAdded.add(idRandom);
//                checkSame = false;
//
////                for (String s : imageAdded){
////                    Log.e("My array list content: ", s);
////                } ///check if they masuk array or not (done checked)
//
//            }
//        }


//        idRandom = imageID.get(randomNo);
//
//        SharedPreferences.Editor editor = getSharedPreferences(ARRAYIMAGE, MODE_PRIVATE).edit();
//        editor.putInt("imageID", 0);
//        editor.apply();
        Log.d("random", idRandom);
        return idRandom;
        //kena igt number dah amik and masuk another array
    }

    public void updateScore(){
        // retrieve
        SharedPreferences pref = getSharedPreferences(RESULTQUIZ, MODE_PRIVATE);
        quizScore = pref.getInt("quizScore", 0);
        quizScore = quizScore + 1;
        SharedPreferences.Editor editor = getSharedPreferences(RESULTQUIZ, MODE_PRIVATE).edit();
        editor.putInt("quizScore",quizScore);
        editor.apply();

    }

    public void updateQuestionNumber(){

        SharedPreferences pref = getSharedPreferences(RESULTQUIZ, MODE_PRIVATE);
        questionNumber = pref.getInt("questionNumber", 0);
        questionNumber = questionNumber + 1;
        Log.e("My question number ", String.valueOf(questionNumber));
        SharedPreferences.Editor editor = getSharedPreferences(RESULTQUIZ, MODE_PRIVATE).edit();
        editor.putInt("questionNumber",questionNumber);
        editor.apply();
        scoreNumber.setText("No. " + String.valueOf(questionNumber) );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_CODE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        quizCorrect.setVisibility(View.INVISIBLE);
        quizWrong.setVisibility(View.INVISIBLE);
        //testSuara = null;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }


    public void nextQuestion(View view) {
        if(!dahJawab)
        {
            Snackbar mySnackbar = Snackbar.make(view, "Anda belum menjawab soalan", Snackbar.LENGTH_LONG);
            mySnackbar.show();

        }
        else if (questionNumber == totalQuestionQuiz )
        {
            Intent intent = new Intent(getApplicationContext(),FinishKuiz.class);
            startActivity(intent);
        }
        else {

            Intent i = new Intent(getApplicationContext(), StartKuiz.class);
            startActivity(i);

        }
//        if(!testSuara.getText().toString().isEmpty()) { //validation to see if the user input suara ke tak lg
//            Intent i = new Intent(getApplicationContext(), StartKuiz.class);
//            startActivity(i);
//        }
    }



    @Override
    public void onBackPressed() {
    }

    @Override
    public AssetManager getAssets() {
        return getResources().getAssets();
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