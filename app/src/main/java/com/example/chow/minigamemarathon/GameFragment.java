package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by per6 on 10/26/17.
 */

public abstract class GameFragment extends Fragment {

    private OnGameStateUpdateListener gameStateUpdateListener;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //timings
        if (gameStateUpdateListener != null) {
            gameStateUpdateListener.onGameStart(this);
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

    public void setGameStateUpdateListener(OnGameStateUpdateListener listener) {
        this.gameStateUpdateListener = listener;
    }

    public void notifyGameEnd() {
        View rootView = getView();
        if (rootView != null)
        {
            rootView.setVisibility(View.GONE);
            rootView.setEnabled(false);
        }
        if (gameStateUpdateListener != null) {
            gameStateUpdateListener.onGameSolved(this);
        }
    }

    public String[] getLevelData(int scorePerGame, long millis) {
        return new String[]{getGameName(), millis + "", (int) (scorePerGame * getPercentScore()) + ""};
    }

    @Override
    public void onPause() {
        super.onPause();
        if (gameStateUpdateListener != null) {
            gameStateUpdateListener.onGamePaused(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gameStateUpdateListener != null) {
            gameStateUpdateListener.onGameResume(this);
        }
    }

    public void finalizeArguments(GameMode gameMode) {
        setGameMode(gameMode);
    }

    public abstract String getGameName();

    public abstract double getPercentScore();

    public abstract boolean isSolved();

    public abstract void setGameMode(GameMode gameMode);

    public abstract void assignWidgetFunctions();

    public abstract int getIconID();

    public abstract String getDescription();

    public interface OnGameStateUpdateListener {
        public void onGameSolved(GameFragment solvedFragment);

        public void onGameStart(GameFragment startingFragment);

        public void onGamePaused(GameFragment pausedFragment);

        public void onGameResume(GameFragment resumeFragment);
    }
}
