package com.example.chow.minigamemarathon;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

/**
 * Created by Kyros on 11/19/2017.
 */

public class GameContainerFragment extends Fragment implements GameFragment.OnGameStateUpdateListener, StopWatch.OnTickListener {

    private TextView sectionTime, totalTime, roundView, scoreView;
    private int totalScore;
    private static final int SCORE_PER_GAME = 10000;
    protected ArrayList<Fragment> displayedFragments = new ArrayList<>();
    protected StopWatch timer;
    protected int fragmentPosition;
    private String[][] gameDataSets;
    private ViewPager viewPager;
    private GameMode gameMode = GameMode.AVAILABLE_GAME_MODES[0];
    private SectionsPagerAdapter adapter;
    private boolean isLastFragmentGameFragment = false;
    private LinearLayout extraTimeBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.game_fragment_container_layout, container, false);
        extraTimeBar = (LinearLayout) inflater.inflate(R.layout.complete_layout_basic, container, false);
        //info for each game fragment
        sectionTime = extraTimeBar.findViewById(R.id.section_time_status);
        totalTime = extraTimeBar.findViewById(R.id.total_time_status);
        roundView = extraTimeBar.findViewById(R.id.current_game_view);
        scoreView = extraTimeBar.findViewById(R.id.current_score_view);
        //child fragments
        adapter = new SectionsPagerAdapter(getChildFragmentManager(), displayedFragments);
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

            @Override
            public void onPageSelected(int position) {
                LinearLayout statusContainer = rootView.findViewById(R.id.status_container);
                if (displayedFragments.get(position) instanceof GameFragment)
                {
                    if (!isLastFragmentGameFragment)
                    {
                        statusContainer.addView(extraTimeBar);
                    }
                    GameFragment startingFragment = (GameFragment) displayedFragments.get(fragmentPosition);
                    startingFragment.finalizeArguments(gameMode);
                    startingFragment.assignWidgetFunctions();
                    roundView.setText("" + (fragmentPosition + 2 - (displayedFragments.size() - gameDataSets.length)));
                    scoreView.setText("" + totalScore);
                    sectionTime.setText(GameFragment.formatMillisToMMSSMSMS(0));
                    totalTime.setText(GameFragment.formatMillisToMMSSMSMS(timer.getTotalTimeElapsed()));
                    isLastFragmentGameFragment = true;
                }
                else
                {
                    statusContainer.removeView(extraTimeBar);
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
                DatabaseHandler db = new DatabaseHandler(GameContainerFragment.this.getContext());
                //set the best score
                SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                TextView bestScore = extraTimeBar.findViewById(R.id.best_score_view);
                bestScore.setText("" + preferences.getInt(EndFragment.BEST_SCORE_KEY + gameMode.name(), 0));
                //start timer
                timer = new StopWatch(5);
                timer.setOnTickListener(GameContainerFragment.this);
                displayNextFragment();
            }
        });
        EndFragment endScreen = new EndFragment();
        //make listeners for GameFragments
        for (final GameFragment fragment : gameFragments)
        {
            fragment.setGameMode(gameMode);
            fragment.setGameStateUpdateListener(this);
        }
        //instance variables
        fragmentPosition = -1;
        gameDataSets = new String[gameFragments.length][];
        //add all fragments to the list
        displayedFragments = new ArrayList<>();
        displayedFragments.add(startScreen);
        displayedFragments.addAll(Arrays.asList(gameFragments));
        displayedFragments.add(endScreen);
    }

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
        //nothing
        //just for structure
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

    protected void displayNextFragment() {
        fragmentPosition++;
        if (fragmentPosition < displayedFragments.size())
        {
            Fragment displayFragment = displayedFragments.get(fragmentPosition);
            if (displayFragment instanceof EndFragment)
            {
                EndFragment endFragment = (EndFragment) displayFragment;
                endFragment.setArguments(gameDataSets, gameMode);
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
        if (timer!= null)
        {
            timer.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null)
        {
            timer.pause();
        }
    }

    public static GameFragment[] getAllGames()
    {
        return new GameFragment[] {new LightsOutGameFragment(), new BinaryGameFragment(), new Maze3DGameFragment()};
    }

    @Override
    public void onTick(long lapTimeElapsed, long totalTimeElapsed) {
        sectionTime.setText(GameFragment.formatMillisToMMSSMSMS(lapTimeElapsed));
        totalTime.setText(GameFragment.formatMillisToMMSSMSMS(totalTimeElapsed));
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
