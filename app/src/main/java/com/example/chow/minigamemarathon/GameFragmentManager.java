package com.example.chow.minigamemarathon;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by per6 on 10/26/17.
 */

public class GameFragmentManager {

    private ArrayList<GameFragment> gameFragments;
    private int fragmentPosition;
    private AppCompatActivity activity;

    public GameFragmentManager(AppCompatActivity activity, ArrayList<GameFragment> gameFragments)
    {
        this.activity = activity;
        this.gameFragments = gameFragments;
    }


    public void displayNextFragment()
    {
        fragmentPosition++;
        if (fragmentPosition < gameFragments.size())
        {
            FragmentManager fm = activity.getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.display_frame,gameFragments.get(fragmentPosition)).commit();
        }
    }

}
