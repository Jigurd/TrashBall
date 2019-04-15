package com.jigurd.trashball;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Score>
{

    private LayoutInflater mInflater;
    private ArrayList<Score> scores;
    private int mViewResourceId;

    ListAdapter(Context context, int textViewID, ArrayList<Score> scores)
    {
        super(context, textViewID, scores);
        this.scores = scores;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewID;
    }

    @NonNull
    public View getView(int position, View convertView,@NonNull ViewGroup parent)
    {
        convertView = mInflater.inflate(mViewResourceId, null);

        Score score = scores.get(position);

        if (score != null)
        {
            TextView Score = convertView.findViewById(R.id.txtScore);
            TextView AirTime = convertView.findViewById(R.id.txtAirTime);
            TextView Name = convertView.findViewById(R.id.txtName);
            TextView Time = convertView.findViewById(R.id.txtTime);
            if (Score != null)
            {
                Score.setText(score.displayScore());
            }
            if (AirTime != null)
            {
                AirTime.setText(score.displayAirTime());
            }
            if (Name != null)
            {
                Name.setText(score.displayPlayer());
            }
            if (Time != null)
            {
                Time.setText(score.displayPlayTime());
            }
        }
        return convertView;
    }
}
