package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

/**
 * Created by Kyros on 11/19/2017.
 */

public class GameContainerFragment extends Fragment {

    private int fragmentPosition, totalScore;
    private static final int SCORE_PER_GAME = 10000;
    private ArrayList<Fragment> displayedFragments = new ArrayList<>();
    private StopWatch timer;
    private String[][] gameDataSets;
    private ViewPager viewPager;
    private GameMode gameMode = GameMode.AVAILABLE_GAME_MODES[0];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.game_fragment_container_layout, container, false);
        Log.d(TAG, "onCreateView: " + rootView.toString());
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager(), displayedFragments);
        viewPager = rootView.findViewById(R.id.game_parent_container);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && displayedFragments.get(fragmentPosition) instanceof GameFragment)
                {
                    Log.d(TAG, "onPageScrollStateChanged: timer is starting " + displayedFragments.get(fragmentPosition).toString());
                    timer.start();
                }
            }
        });
        displayNextFragment();

        return rootView;
    }

    public void setArguments(GameFragment[] gameFragments)
    {
        //make transition screens
        StartFragment startScreen = new StartFragment();
        startScreen.setOnStartListener(new StartFragment.OnStartListener() {
            @Override
            public void onStart(GameMode gameMode) {
                GameContainerFragment.this.gameMode = gameMode;
                displayNextFragment();
            }
        });
        EndFragment endScreen = new EndFragment();
        //make listeners for GameFragments
        for (final GameFragment fragment : gameFragments)
        {
            fragment.setGameMode(gameMode);
            fragment.setGameStateUpdateListener(new GameFragment.OnGameStateUpdateListener() {
                @Override
                public void onGameSolved(GameFragment solvedFragment) {
                    timer.pause();
                    totalScore += (int) (solvedFragment.getPercentScore() * SCORE_PER_GAME);
                    gameDataSets[fragmentPosition + 1 - (displayedFragments.size() - gameDataSets.length)] = solvedFragment.getLevelData(SCORE_PER_GAME, timer.getLapTimeElapsed());
                    timer.lap();
                    displayNextFragment();
                }

                @Override
                public void onGameStart(GameFragment startingFragment) {
                    //don't do anything
                    //wait for the fragment transition to finish
                    //start timer in onPageScrollStateChanged()
                }

                @Override
                public void onGamePaused(GameFragment pausedFragment) {
                    //nothing
                    //just for structure
                    //pause timer in onPause() of GameContainerFragment
                }

                @Override
                public void onGameResume(GameFragment resumeFragment) {
                    //nothing
                    //just for structure
                    //pause timer in onResume() of GameContainerFragment
                }
            });
        }
        //instance variables
        fragmentPosition = -1;
        timer = new StopWatch(5);
        gameDataSets = new String[gameFragments.length][];
        //add all fragments to the list
        displayedFragments = new ArrayList<>();
        displayedFragments.add(startScreen);
        displayedFragments.addAll(Arrays.asList(gameFragments));
        displayedFragments.add(endScreen);
    }

    private void displayNextFragment() {
        fragmentPosition++;
        if (fragmentPosition < displayedFragments.size())
        {
            Fragment displayFragment = displayedFragments.get(fragmentPosition);
            if (displayFragment instanceof GameFragment)
            {
                GameFragment gameFragment = (GameFragment) displayFragment;
                gameFragment.setArguments(fragmentPosition + 2 - (displayedFragments.size() - gameDataSets.length),
                        totalScore, timer.getTotalTimeElapsed(), gameMode);
                gameFragment.initializeVariables();
                gameFragment.displayInfo();
                timer.setOnTickListener(gameFragment);
            }
            else if (displayFragment instanceof EndFragment)
            {
                EndFragment endFragment = (EndFragment) displayFragment;
                endFragment.setArguments(gameDataSets);
            }
            viewPager.setCurrentItem(fragmentPosition);
            Log.d(TAG, "displayNextFragment: next fragment displayed");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (timer != null)
        {
            timer.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (timer.getTotalTimeElapsed() != 0)
        {
            timer.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.pause();
    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter
    {

        private ArrayList<Fragment> displayFragments;

        private SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> displayFragments) {
            super(fm);
            this.displayFragments = displayFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return displayFragments.get(position);
        }

        @Override
        public int getCount() {
            return displayFragments.size();
        }
    }
}
