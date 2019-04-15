package com.jigurd.trashball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static final String USERPREFS = "UserPref";
    private String name = "Anonymous"; //player name
    private int minimum = 20; //minimum acceleration needed to launch throwEvent

    private SensorManager sensorManager;
    private Sensor sensor;

    //effectively a lock on whether a throw event is happening
    boolean isThrowing = false;

    //displays last height
    private TextView heightView;

    private ArrayList<Double> accels;
    private Timer timer;

    private TextView lblHighScore;
    private ArrayList<Score> highScores;



    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up sensor manager
        sensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            Toast.makeText(getApplicationContext(), "FATAL ERROR: Could not find accelerometer.", Toast.LENGTH_LONG).show();
        }

        //set up timer
        timer = new Timer();

        //initialize lists
        accels = new ArrayList<>();
        highScores = new ArrayList<>();

        //initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.ding);

        //initialize views
        heightView = findViewById(R.id.heightView);
        lblHighScore = findViewById(R.id.lblHighScore);

        //set up buttons
        Button prefBtn = findViewById(R.id.prefBtn);
        prefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(i);
            }
        });

        Button highScoresBtn = findViewById(R.id.highScoresBtn);
        highScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this, HighScoresActivity.class);
                ScoreComparator c = new ScoreComparator();

                //this is technically slightly inefficient but it's a score table with on the order
                //of 20 elements tops so I don't care.
                Collections.sort(highScores, c);
                Collections.reverse(highScores);

                i.putExtra("LIST", highScores);

                startActivity(i);
            }
        });

        //initialize sharedPreferences with default
        SharedPreferences.Editor uPrefs = getSharedPreferences(USERPREFS, MODE_PRIVATE).edit();
        uPrefs.putInt("minimum", minimum);
        uPrefs.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_GAME);

        final SharedPreferences uPrefs = getSharedPreferences(USERPREFS, MODE_PRIVATE);
        minimum = uPrefs.getInt("minimum", 30);
        name = uPrefs.getString("name", "Anonymous");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //if we are not in a throw event
        if (!isThrowing){
            //record acceleration
            final double accel= Math.sqrt(event.values[0] * event.values[0] + event.values[1]*event.values[1]
                    + event.values[2]*event.values[2]) - SensorManager.GRAVITY_EARTH;

            //if this is higher than threshold
            if (accel>minimum)
            {
                accels.add(accel);

                if(accels.size() == 1) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            double maxAccel = Collections.max(accels);
                            accels.clear();
                            throwEvent(maxAccel);
                        }
                    }, 250);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void throwEvent(double maxAccel) {
        isThrowing = true;
        Log.i("throwEvent", "We throwin bois");

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));


        //calculate time to the top in milliseconds
        double ascendTime = (maxAccel/SensorManager.GRAVITY_EARTH);

        Log.i("throwEvent", "maxAccel is " + maxAccel + ", time to top is " + ascendTime);

        //get ascent time in millisconds.
        int  ascendTimeMS = (int)(ascendTime * 1000);

        Log.i("throwEvent", "sleeptime: " + ascendTimeMS);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //update texts
                heightView.setText("Throwing...");
                lblHighScore.setText("");
            }
        });

        //wait until ball gets to the top
        try {
            Thread.sleep(ascendTimeMS);
        }  catch(java.lang.InterruptedException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //vibrate again
        v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

        //play sound
        mediaPlayer.start();

        //height is
        Log.i("throwEvent", "maxAccel: " + maxAccel + ", gravity: " + SensorManager.GRAVITY_EARTH);

        double height = (Math.pow(maxAccel,2)/2*SensorManager.GRAVITY_EARTH);

        //For some ungodly reason, the answer is multiplied by 100 in the line above. I have no idea
        //How or why this happens, but otherwise the match checks out (the max height of a thrown
        //object is equal to V*V/2G). I made some debug outputs of the values here if you want to
        //check the math by hand. If you figure out why this is happening, please let me know as
        //I have no idea what is happening here. ¯\_(ツ)_/¯
        Log.i("throwEvent", "(wrong) height is " + height);
        height = height/100f;
        Log.i("throwEvent", "height is " + height);
        final String heightString = String.format(Locale.ENGLISH, "%.2f", height);

        //update view
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                heightView.setText("Height: " + heightString+ "m");
            }
        });

        //create new score
        Score newScore = new Score(name, height, ascendTime);

        //add Score to highScores
        highScores.add(newScore);

        //if this is the high score, say so on the menu.
        if (newScore == Collections.max(highScores, new ScoreComparator())){
            runOnUiThread(new Runnable(){
                @Override
                public void run()   {
                    lblHighScore.setText("HIGH SCORE!");
                }
            });
        }


        //end throw event
        isThrowing = false;
    }
}
