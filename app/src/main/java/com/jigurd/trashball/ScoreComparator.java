package com.jigurd.trashball;

import java.util.Comparator;

//Compares scores of two Score objects, needed to sort the list.
public class ScoreComparator implements Comparator<Score> {
    //compare(T,T) as specified in the Comparator interface
    public int compare(Score s1, Score s2){
        if (s1.getmScore() == s2.getmScore()){
            return 0;
        } else if (s1.getmScore() > s2.getmScore()){
            return 1;
        } else {
            return -1;
        }
    }
}