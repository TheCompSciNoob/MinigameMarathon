package com.example.chow.minigamemarathon;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

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
    private StopWatch timer;

    public GameFragmentManager(AppCompatActivity activity, ArrayList<GameFragment> gameFragments)
    {
        this.activity = activity;
        this.gameFragments = gameFragments;
        totalTimeElapsed = 0;
        fragmentPosition = -1;
        timer = new StopWatch(5);
        makeTransitionFragments();
        makeListenersForGames();
        displayedFragments = new ArrayList<>();
        displayedFragments.add(startScreen);
        displayedFragments.addAll(gameFragments);
    }

    private void makeListenersForGames() {
        for (final GameFragment gameFragment : gameFragments)
        {
            gameFragment.setGameStateUpdateListener(new GameFragment.OnGameStateUpdateListener() {
                @Override
                public void onGameSolved() {
                    timer.pause();
                    timer.lap();
                    displayNextFragment();
                }

                @Override
                public void onGameStart() {
                    timer.start();
                }
            });
        }
    }

    private void makeTransitionFragments() {
        startScreen = new StartFragment();
        startScreen.setOnStartListener(new StartFragment.OnStartListener() {
            @Override
            public void onStart() {
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
            Fragment displayFragment = displayedFragments.get(fragmentPosition);
            if (displayFragment instanceof GameFragment)
            {
                GameFragment nextFragment = (GameFragment) displayFragment;
                nextFragment.setStartTotalTime(timer.getTotalTimeElapsed());
                timer.setOnTickListener(nextFragment);
            }
            fm.beginTransaction().replace(R.id.display_frame,displayFragment).commit();
        }
    }
}
