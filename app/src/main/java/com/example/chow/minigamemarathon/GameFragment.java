package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

/**
 * Created by per6 on 10/26/17.
 */

public abstract class GameFragment extends Fragment {

    private StopWatch sectionStopWatch;
    private TextView sectionTime, totalTime;
    private long totalTimeElapsed;
    private GameStateUpdateListener listener;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //makes and starts stopwatch of the current game
        sectionTime = getView().findViewById(R.id.section_time_status);
        sectionTime.setText(formatMillisToMMSSMSMS(0));
        totalTime = getView().findViewById(R.id.total_time_status);
        totalTime.setText(formatMillisToMMSSMSMS(totalTimeElapsed));
        sectionStopWatch = new StopWatch(5) {
            @Override
            public void onTick(long timeElapsed) {
                sectionTime.setText(formatMillisToMMSSMSMS(timeElapsed));
                totalTime.setText(formatMillisToMMSSMSMS(totalTimeElapsed + timeElapsed));
                if (listener != null)
                {
                    listener.onGameStateUpdate();
                }
                if (isSolved())
                {
                    sectionStopWatch.pause();
                }
            }
        };
        sectionStopWatch.start();
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

    @Override
    public void onPause() {
        super.onPause();
        sectionStopWatch.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        sectionStopWatch.resume();
    }

    public void setTotalTimeElapsed(long totalTimeElapsedMillis)
    {
        totalTimeElapsed = totalTimeElapsedMillis;
    }

    public long getSectionTimeElapsed()
    {
        return sectionStopWatch.getTimeElapsed();
    }

    public void setGameStateUpdateListener(GameStateUpdateListener listener)
    {
        this.listener = listener;
    }

    public abstract double getPercentScore();

    public abstract boolean isSolved();
}
