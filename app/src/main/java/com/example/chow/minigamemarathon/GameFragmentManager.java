package com.example.chow.minigamemarathon;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by per6 on 10/26/17.
 */

public class GameFragmentManager {

    private String TAG = "GameFragmentManager";

    private ArrayList<GameFragment> gameFragments;
    private int fragmentPosition;
    private AppCompatActivity activity;
    private ArrayList<Fragment> displayedFragments;
    private StartFragment startScreen;
    private long totalTimeElapsed;
    private Button startButton;

    public GameFragmentManager(AppCompatActivity activity, ArrayList<GameFragment> gameFragments)
    {
        this.activity = activity;
        this.gameFragments = gameFragments;
        totalTimeElapsed = 0;
        fragmentPosition = -1;
        makeTransitionFragments();
        makeListenersForGameFragments();
        displayedFragments = new ArrayList<>();
        displayedFragments.add(startScreen);
        displayedFragments.addAll(gameFragments);
    }

    private void makeTransitionFragments() {
        startScreen = new StartFragment();
        startScreen.setOnStartListener(new GameStateUpdateListener() {
            @Override
            public void onGameStateUpdate() {
                displayNextFragment();
            }
        });
    }

    private void makeListenersForGameFragments() {
        for (final GameFragment gameFragment : gameFragments)
        {
            gameFragment.setGameStateUpdateListener(new GameStateUpdateListener() {
                @Override
                public void onGameStateUpdate() {
                    if (gameFragment.isSolved())
                    {
                        totalTimeElapsed += gameFragment.getSectionTimeElapsed();
                        displayNextFragment();
                        Log.d(TAG, "onGameStateUpdate: next fragment displayed");
                    }
                }
            });
        }
    }

    public void displayNextFragment()
    {
        fragmentPosition++;
        if (fragmentPosition < displayedFragments.size())
        {
            FragmentManager fm = activity.getSupportFragmentManager();
            Fragment displayFragment = displayedFragments.get(fragmentPosition);
            if (displayFragment instanceof GameFragment)
            {
                ((GameFragment) displayFragment).setTotalTimeElapsed(totalTimeElapsed);
            }
            fm.beginTransaction().replace(R.id.display_frame,displayFragment).commit();
        }
    }

}
