package com.example.chow.minigamemarathon;

/**
 * Created by per6 on 11/30/17.
 */

public class ColorMatchFragment extends GameFragment{
    private GameMode gameMode;
    private ColorMatchAlgorithm game;
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
