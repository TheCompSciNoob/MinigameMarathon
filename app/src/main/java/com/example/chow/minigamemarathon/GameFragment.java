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
    private boolean isPaused;

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
                if (!isPaused) {
                    if (isSolved()) {
                        this.pause(); //changed from sectionstopwatch to this
                    }
                    if (listener != null) {
                        listener.onGameStateUpdate();
                    }
                }
            }
        };
        sectionStopWatch.start();
    }

    private String formatMillisToMMSSMSMS(long millisTime) {
        long millis = millisTime;
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        millis = millis % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }

    public void setTotalTimeElapsed(long totalTimeElapsedMillis) {
        totalTimeElapsed = totalTimeElapsedMillis;
    }

    public long getSectionTimeElapsed() {
        return sectionStopWatch.getTimeElapsed();
    }

    public void setGameStateUpdateListener(GameStateUpdateListener listener) {
        this.listener = listener;
    }

    public void setRound(int round) {
        TextView roundView = getView().findViewById(R.id.current_game_view);
        roundView.setText("" + round);
    }

    public void setScore(int score) {
        TextView scoreView = getView().findViewById(R.id.current_score_view);
        scoreView.setText("" + score);
    }

    @Override
    public void onPause() {
        isPaused = false;
        super.onPause();
    }

    public abstract double getPercentScore();

    public abstract boolean isSolved();
}
