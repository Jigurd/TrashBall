package com.jigurd.trashball;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

//a Score object keeps track of all data pertaining to a given score. They're put in the
//highScores list and displayed in the highScore table.
public class Score implements Serializable {
    private String mPlayer;
    private double mScore;
    private double mAirTime;
    private LocalDateTime mPlayTime;

    Score(String player, double score, double airTime){
        this.mPlayer = player;
        this.mScore = score;
        this.mAirTime = airTime;
        this.mPlayTime = LocalDateTime.now();
    }

    public String displayPlayer(){
        return this.mPlayer;
    }

    public String displayScore(){
        return String.format(Locale.ENGLISH, "%.2f" + "m",this.mScore);
    }

    public String displayAirTime(){
        return String.format(Locale.ENGLISH, "%.2f" + "s",this.mAirTime);
    }

    public String displayPlayTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
        return this.mPlayTime.format(formatter);
    }

    public double getmScore(){
        return this.mScore;
    }
}



