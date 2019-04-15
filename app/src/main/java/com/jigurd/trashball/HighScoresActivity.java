package com.jigurd.trashball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoresActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        final Intent i = getIntent();
        final ArrayList<Score>  scoreList = (ArrayList<Score>) i.getSerializableExtra("LIST");

        ListAdapter adapter = new ListAdapter(this, R.layout.list_helper_view, scoreList);
        final ListView listView = findViewById(R.id.listScores);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id)
            {
                Score element = scoreList.get(pos);
                Toast.makeText(getApplicationContext(),
                        element.displayPlayer()+" "+element.displayScore(),
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });
        listView.setAdapter(adapter);
    }
}