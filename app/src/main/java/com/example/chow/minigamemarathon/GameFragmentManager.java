package com.example.chow.minigamemarathon;

import android.support.v4.app.Fragment;
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
    private ArrayList<Fragment> displayedFragments;
    private StartFragment startScreen;

    public GameFragmentManager(AppCompatActivity activity, ArrayList<GameFragment> gameFragments)
    {
        this.activity = activity;
        this.gameFragments = gameFragments;
        makeStartAndEndScreens();
        makeListenersForGameFragments();
        displayedFragments = new ArrayList<>();
        displayedFragments.add(startScreen);
        displayedFragments.addAll(gameFragments);
    }

    private void makeListenersForGameFragments() {
        for (final GameFragment gameFragment : gameFragments)
        {
            gameFragment.setGameStateUpdateListener(new GameStateUpdateListener() {
                @Override
                public void onGameStateUpdate() {
                    if (gameFragment.isSolved())
                    {
                        displayNextFragment();
                    }
                }
            });
        }
    }

    private void makeStartAndEndScreens() {
        startScreen = new StartFragment();
        startScreen.setOnStartListener(new GameStateUpdateListener() {
            @Override
            public void onGameStateUpdate() {
                displayNextFragment();
            }
        });
    }


    public void displayNextFragment()
    {
        fragmentPosition++;
        if (fragmentPosition < displayedFragments.size())
        {
            FragmentManager fm = activity.getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.display_frame,gameFragments.get(fragmentPosition)).commit();
        }
    }

}
