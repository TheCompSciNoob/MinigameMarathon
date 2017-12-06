package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by per6 on 11/30/17.
 */

public class ColorMatchFragment extends GameFragment{
    private GameMode gameMode;
    private ColorMatchAlgorithm game;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.color_game, container, false);
    }

    @Override
    public String getGameName() {return "Color Match Challenge!";}

    @Override
    public double getPercentScore() {
        return (game.getQuestionsAnswered()/game.questionsAttempted);
    }

    @Override
    public boolean isSolved() {

        return game.isFinished();

    }

    @Override
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void assignWidgetFunctions() {
        game = new ColorMatchAlgorithm(gameMode);
    }

}
