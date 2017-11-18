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
    private int round, score;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sectionTime = getView().findViewById(R.id.section_time_status);
        sectionTime.setText(formatMillisToMMSSMSMS(0));
        totalTime = getView().findViewById(R.id.total_time_status);
        totalTime.setText(formatMillisToMMSSMSMS(startTotalTime));
        //from previous game
        TextView roundView = getView().findViewById(R.id.current_game_view);
        roundView.setText("" + round);
        TextView scoreView = getView().findViewById(R.id.current_score_view);
        scoreView.setText("" + score);
        if (listener != null) {
            listener.onGameStart(this);
        }
    }

    public static String formatMillisToMMSSMSMS(long millisTime) {
        long millis = millisTime;
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        millis = millis % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }

    public void setStartTotalTime(long totalTimeElapsedMillis) {
        startTotalTime = totalTimeElapsedMillis;
    }

    public void setGameStateUpdateListener(OnGameStateUpdateListener listener) {
        this.listener = listener;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void onTick(long lapTimeElapsed, long totalTimeElapsed) {
        sectionTime.setText(formatMillisToMMSSMSMS(lapTimeElapsed));
        totalTime.setText(formatMillisToMMSSMSMS(totalTimeElapsed));
    }

    public void notifyGameEnd() {
        if (listener != null) {
            listener.onGameSolved(this);
        }
    }

    public String[] getLevelData(int scorePerGame, long millis)
    {
        return new String[] {getGameName(), millis + "", (int) (scorePerGame * getPercentScore()) + ""};
    }

    @Override
    public void onPause() {
        super.onPause();
        if (listener != null)
        {
            listener.onGamePaused(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null)
        {
            listener.onGameResume(this);
        }
    }

    public abstract String getGameName();

    public abstract double getPercentScore();

    public abstract boolean isSolved();

    public abstract void setGameMode(GameMode gameMode);

    public interface OnGameStateUpdateListener
    {
        public void onGameSolved(GameFragment solvedFragment);

        public void onGameStart(GameFragment startingFragment);

        public void onGamePaused(GameFragment pausedFragment);

        public void onGameResume(GameFragment resumeFragment);
    }
}
