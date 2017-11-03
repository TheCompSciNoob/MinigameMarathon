package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

/**
 * Created by per6 on 10/26/17.
 */

public abstract class GameFragment extends Fragment implements StopWatch.OnTickListener {

    private TextView sectionTime, totalTime;
    private long startTotalTime;
    private OnGameStateUpdateListener listener;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sectionTime = getView().findViewById(R.id.section_time_status);
        sectionTime.setText(formatMillisToMMSSMSMS(0));
        totalTime = getView().findViewById(R.id.total_time_status);
        totalTime.setText(formatMillisToMMSSMSMS(startTotalTime));
        if (listener != null)
        {
            listener.onGameStart();
        }
    }

    private String formatMillisToMMSSMSMS(long millisTime)
    {
        long millis = millisTime;
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        millis = millis % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }

    public void setStartTotalTime(long totalTimeElapsedMillis)
    {
        startTotalTime = totalTimeElapsedMillis;
    }

    public void setGameStateUpdateListener(OnGameStateUpdateListener listener)
    {
        this.listener = listener;
    }
    public void setRound(int round)
    {
        TextView roundView = getView().findViewById(R.id.current_game_view);
        roundView.setText("" + round);
    }

    public void setScore(int score)
    {
        TextView scoreView = getView().findViewById(R.id.current_score_view);
        scoreView.setText("" + score);
    }

    @Override
    public void onTick(long lapTimeElapsed, long totalTimeElapsed) {
        sectionTime.setText(formatMillisToMMSSMSMS(lapTimeElapsed));
        totalTime.setText(formatMillisToMMSSMSMS(totalTimeElapsed));
        if (isSolved() && listener != null)
        {
            listener.onGameSolved();
        }
    }

    public abstract double getPercentScore();

    public abstract boolean isSolved();

    public interface OnGameStateUpdateListener
    {
        public void onGameSolved();

        public void onGameStart();
    }
}
