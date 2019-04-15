package com.jigurd.trashball;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.jigurd.trashball.MainActivity.USERPREFS;

public class PreferencesActivity extends AppCompatActivity {
    int minimum;

    TextView lblCurrent;
    TextView fieldName;
    SeekBar barMinimum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        //get minimum from preferences
        final SharedPreferences uPrefs = getSharedPreferences(USERPREFS, MODE_PRIVATE);
        minimum = uPrefs.getInt("minimum", 50);
        final String name = uPrefs.getString("name", "Anonymous");


        //initialize seekBar
        barMinimum=findViewById(R.id.barMinimum);
        barMinimum.setProgress(minimum);

        //initialize current label
        lblCurrent=findViewById(R.id.lblCurrent);
        lblCurrent.setText("Minimum: " + barMinimum.getProgress() + "/" + barMinimum.getMax());

        barMinimum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                lblCurrent.setText("Minimum: " + progress + "/" + barMinimum.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                minimum = barMinimum.getProgress();

                SharedPreferences.Editor uPrefs = getSharedPreferences(USERPREFS, MODE_PRIVATE).edit();
                uPrefs.putInt("minimum", minimum);
                uPrefs.apply();
            }
        });


        //initialize fieldName
        fieldName = findViewById(R.id.fieldName);
        fieldName.setText(name); //set it to the preference name
        fieldName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor uPrefs = getSharedPreferences(USERPREFS, MODE_PRIVATE).edit();
                uPrefs.putString("name", s.toString());
                uPrefs.apply();
            }
        });
    }
}
