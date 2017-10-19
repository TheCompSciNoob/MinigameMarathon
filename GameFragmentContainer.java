package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kyros on 10/16/2017.
 */

public class GameFragmentContainer extends Fragment implements GameEventListener {

    private StopWatch totalStopWatch, sectionStopWatch;
    private int fragmentPos = -1;
    private ArrayList<GameFragment> gameFragments;
    private static final long UPDATE_INTERVAL = 5;
    private Timer timer;
    private TextView totalTimeView, sectionTimeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.container_fragment_layout, container, false);
        //wire widgets
        totalTimeView = rootView.findViewById(R.id.stopwatch_view);
        //show fragment
        displayNextFragment();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTimeInfo(totalStopWatch.getTimeElapsed(), sectionStopWatch.getTimeElapsed());
            }
        }, 0, UPDATE_INTERVAL);
        return rootView;
    }

    public void addGameFragment(GameFragment newGameFragment)
    {
        gameFragments.add(newGameFragment);
    }

    @Override
    public void onGameStart() {
        if (gameFragments.get(fragmentPos).isGame())
        {
            totalStopWatch.resume();
            sectionStopWatch.start();
            updateTimeInfo(totalStopWatch.getTimeElapsed(), sectionStopWatch.getTimeElapsed());
        }
    }

    @Override
    public void onGameEnd() {
        totalStopWatch.pause();
        sectionStopWatch.stop();
    }

    private void updateTimeInfo(long totalTime, long sectionTime)
    {
        //update TextView in layout
        totalTimeView.setText("" + totalTime);
    }

    private void displayNextFragment()
    {
        fragmentPos++;
        try
        {
            GameFragment currentFragment = gameFragments.get(fragmentPos);
            currentFragment.setGameEventListener(this);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.fragment_game, currentFragment)
                    .commit();
        }
        catch (Exception e)
        {
            finish();
        }
    }

    private void finish()
    {
        timer.cancel();
        Toast.makeText(getActivity(), "finished", Toast.LENGTH_SHORT);
    }
}
