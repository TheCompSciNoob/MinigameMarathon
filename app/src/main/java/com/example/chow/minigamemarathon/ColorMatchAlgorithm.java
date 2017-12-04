package com.example.chow.minigamemarathon;

/**
 * Created by per6 on 11/28/17.
 */

public class ColorMatchAlgorithm {
    private Color colorSelect1, colorSelect2, answeredColor;
    private boolean finished = false;
    private int questionsAnswered = 0;
    public int questionCount;

    public int questionsAttempted=0;
    public ColorMatchAlgorithm(GameMode gameMode)
    {
        if (gameMode.equals(GameMode.EASY))
        {questionCount=5;}
        else if (gameMode.equals(GameMode.HARD))
        {questionCount=15;}
        else if (gameMode.equals(GameMode.DEBUG))
        {questionCount=2;}
    }
    public void checkIfFinished() {
        if (questionsAnswered >= questionCount) {
            finished = true;
        }
    }
    public boolean isFinished() {
        return finished;
    }

    public void genAnswer() {
        answeredColor.genColorID();
    }

    public void checkAnswer() {
        if (colorSelect1.getColorNumberID() == 0 && colorSelect2.getColorNumberID() == 4 || colorSelect2.getColorNumberID() == 0 && colorSelect1.getColorNumberID() == 4) {
            if (answeredColor.getColorNumberID() == 5) {
                questionsAnswered++;
            }

        } else {
            if (answeredColor.getColorNumberID() == (colorSelect1.getColorNumberID() + colorSelect2.getColorNumberID()) / 2) {
                questionsAnswered++;
            }
        }
        questionsAttempted++;
        checkIfFinished();

    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public int getQuestionsAttempted() {
        return questionsAttempted;
    }
}

