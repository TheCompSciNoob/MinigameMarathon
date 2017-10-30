package com.example.chow.minigamemarathon;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
        makeListenersForGameFragments();
        displayedFragments = new ArrayList<>();
        displayedFragments.add(new StartFragment());
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
                        gameFragment.stopTimer();
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
        if (fragmentPosition == 0)
        {
            startButton = displayedFragments.get(0).getView().findViewById(R.id.button_start);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayNextFragment();
                }
            });
        }
    }

}
