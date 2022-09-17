package com.kata.kidspe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.kata.kidspe.Utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ViewImageActivity extends AppCompatActivity {

    public ImageView image1, mic, listenBtn;
    public TextView textView, scoreView, passView;
    public static final Integer REQUEST_CODE = 1;
    public EditText editText;
    TextToSpeech tts;
    private SpeechRecognizer speechRecognizer;
    FirebaseDatabase  database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef = database.getReference();
    public static final String SCORE = "Score";
    String categoryName = Utils.CATEGORY_SELECTED;
    String imageName = Utils.IMAGE_SELECTED_NAME;
    private LottieAnimationView lottieAnimationView, lottieCorrect, lottieWrong;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        final MediaPlayer correctSound = MediaPlayer.create(this, R.raw.betul);
        final MediaPlayer wrongSound = MediaPlayer.create(this, R.raw.salah);
        listenBtn = findViewById(R.id.listenBtn);
        editText = (EditText)findViewById(R.id.editText);
        // talkBtn = (Button) findViewById(R.id.talkBtn);
        mic = findViewById(R.id.mic);
        scoreView = findViewById(R.id.scoreView);
        passView = findViewById(R.id.passView);
        lottieAnimationView = findViewById(R.id.animationMic);


        SharedPreferences imageScore = getSharedPreferences(SCORE, MODE_PRIVATE);
        boolean adaKeTak = imageScore.contains(imageName);

        if (adaKeTak)
        {
            int imgScore = imageScore.getInt(imageName, 0); //0 is the default value.
            //Toast.makeText(ViewImageActivity.this,Integer.toString(imgScore), Toast.LENGTH_SHORT).show();
            scoreView.setText(String.valueOf(imgScore));
        } else
        {
            SharedPreferences.Editor editor = getSharedPreferences(SCORE, MODE_PRIVATE).edit();
            editor.putInt(imageName,0);
            editor.apply();
            scoreView.setText("0");

        }




        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

        listenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = Utils.IMAGE_SELECTED_NAME;
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


            }
        });


        //editText.setText(" ");

//        talkBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                try {
//                    //Start the Activity and wait for the response//
//                    //startActivityForResult(intent, REQUEST_CODE);
//                   startActivityForResult(intent, REQUEST_CODE);
//                } catch (ActivityNotFoundException a) {
//                }
//
//            }
//        });

        initialize();

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                editText.setHint("Getting ready to listen");
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.animate();
                lottieAnimationView.setProgress(0);
                lottieAnimationView.pauseAnimation();
                lottieAnimationView.playAnimation();

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setHint("Recording...");

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                editText.setText("Stopped listening");
                speechRecognizer.stopListening();
                lottieAnimationView.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                mic.setImageResource(R.drawable.ic_baseline_mic_24);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                editText.setText(data.get(0));
                String result2 = data.get(0);
                if (result2.equalsIgnoreCase(Utils.IMAGE_SELECTED_NAME)) {
                    //Toast.makeText(getApplicationContext(),"Betul!",Toast.LENGTH_LONG).show();
                    updateScore();
                    correctSound.start();




                }
                else if (result2.equalsIgnoreCase(Utils.DIGIT_SELECTED))
                {
                    //Toast.makeText(getApplicationContext(),"Betul!",Toast.LENGTH_LONG).show();
                    updateScore();
                    correctSound.start();


                }
                else {
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

//        mic.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    speechRecognizer.stopListening();
//                }
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    mic.setImageResource(R.drawable.ic_baseline_mic_24);
//                    speechRecognizer.startListening(speechRecognizerIntent);
//                }
//                return false;
//            }
//        });


        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                speechRecognizer.startListening(speechRecognizerIntent);
            }
        });


    }

    public void updateScore()
    {

//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                score = snapshot.child("Image").child(Utils.CATEGORY_ID).child("score").getValue(int.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        SharedPreferences imageScore = getApplicationContext().getSharedPreferences(SCORE, MODE_PRIVATE);
        int imgScore = imageScore.getInt(imageName, 0); //0 is the default value.
        imgScore = 1;
        SharedPreferences.Editor editor = imageScore.edit();
        editor.putInt(imageName, imgScore);
        editor.apply();

        scoreView.setText(String.valueOf(imgScore));

//        mDatabaseRef.child("Image").child(Utils.CATEGORY_ID).child("score").setValue(score).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(ViewImageActivity.this,"Your score is updated", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void initialize() {

        image1 = (ImageView) findViewById(R.id.image1);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(Utils.IMAGE_SELECTED_NAME);
        Picasso.get().load(Utils.selected_image.getImageLink()).into(image1);


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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


//    @Override
//    protected void onPause() {
//        if(tts!=null){
//            tts.stop();
//            tts.shutdown();
//        }
//        super.onPause();
//    }

}