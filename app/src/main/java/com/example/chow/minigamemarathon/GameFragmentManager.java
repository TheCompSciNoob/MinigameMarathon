package com.example.chow.minigamemarathon;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by per6 on 10/26/17.
 */

public class GameFragmentManager {

    private String TAG = "GameFragmentManager";

    private ArrayList<GameFragment> gameFragments;
    private int fragmentPosition, scorePerGame = 10000, totalScore;
    private AppCompatActivity activity;
    private ArrayList<Fragment> displayedFragments;
    private StartFragment startScreen;
    private EndFragment endScreen;
    private StopWatch timer;
    private ArrayList<String> gameDataSets;
    private GameMode gameMode = GameMode.EASY;

    public GameFragmentManager(AppCompatActivity activity, ArrayList<GameFragment> gameFragments)
    {
        this.activity = activity;
        this.gameFragments = gameFragments;
        fragmentPosition = -1;
        timer = new StopWatch(5);
        makeTransitionFragments();
        makeListenersForGames();
        gameDataSets = new ArrayList<>();
        displayedFragments = new ArrayList<>();
        displayedFragments.add(startScreen);
        displayedFragments.addAll(gameFragments);
        displayedFragments.add(endScreen);
    }

    private void makeListenersForGames() {
        for (final GameFragment gameFragment : gameFragments)
        {
            gameFragment.setGameStateUpdateListener(new GameFragment.OnGameStateUpdateListener() {
                @Override
                public void onGameSolved(GameFragment solvedFragment) {
                    timer.pause();
                    totalScore += (int) (solvedFragment.getPercentScore() * scorePerGame);
                    gameDataSets.add(solvedFragment.getLevelData(scorePerGame, timer.getLapTimeElapsed()));
                    timer.lap();
                    displayNextFragment();
                    Log.d(TAG, "onGameSolved: " + gameDataSets.toString());
                }

                @Override
                public void onGameStart(GameFragment startingFragment) {
                    timer.start();
                }

                @Override
                public void onGamePaused(GameFragment pausedFragment) {
                    timer.pause();
                }

                @Override
                public void onGameResume(GameFragment resumeFragment) {
                    timer.resume();
                }
            });
        }
    }

    public void stopTimer()
    {
        if (timer != null)
        {
            timer.stop();
        }
    }

    private void makeTransitionFragments() {
        startScreen = new StartFragment();
        startScreen.setOnStartListener(new StartFragment.OnStartListener() {
            @Override
            public void onStart(GameMode gameMode) {
                GameFragmentManager.this.gameMode = gameMode;
                if (gameMode != null)
                {
                    displayNextFragment();
                }
            }
        });
        endScreen = new EndFragment();
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
                nextFragment.setGameMode(gameMode);
                nextFragment.setStartTotalTime(timer.getTotalTimeElapsed());
                nextFragment.setRound(fragmentPosition + 1 - (displayedFragments.size() - gameFragments.size()));
                nextFragment.setScore(totalScore);
                timer.setOnTickListener(nextFragment);
            }
            else if (displayFragment instanceof EndFragment)
            {
                EndFragment nextFragment = (EndFragment) displayFragment;
                nextFragment.setDataSet(gameDataSets);
            }
            fm.beginTransaction().replace(R.id.display_frame,displayFragment).commit();
        }
    }
}
