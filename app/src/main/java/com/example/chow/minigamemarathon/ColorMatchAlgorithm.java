package com.example.chow.minigamemarathon;

import android.util.Log;

/**
 * Created by per6 on 11/28/17.
 */

public class ColorMatchAlgorithm {
    public String colorStr1, colorStr2, answeredStr;
    public static final String TAG = "minecraft is terrible";
    private boolean finished = false;
    private int questionsCorrect = 0, questionAttempted=0;
    public int questionCount, colorInt1, colorInt2, answerInt;

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
        if (questionsCorrect >= questionCount) {
            finished = true;
            Log.d(TAG, "checkIfFinished: Finished!");
        }
    }


    public void genAnswer()
    {
        int answerInt=(int)(Math.random()*3)*2+1;
        if (answerInt==0)
            {answeredStr = "red";}
        else if (answerInt==1)
            {answeredStr = "yellow";}
        else if (answerInt==2)
            {answeredStr = "green";}
        else if (answerInt==3)
            {answeredStr = "cyan";}
        else if (answerInt==4)
            {answeredStr = "blue";}
        else if (answerInt==5)
            {answeredStr = "magenta";}
        else
            {answeredStr = "NOTHING!";}
    }
    public String colorString(int num)
    {
        int answerNum=num;
        if (answerNum==0)
        {return "red";}
        else if (answerNum==1)
        {return "yellow";}
        else if (answerNum==2)
        {return "green";}
        else if (answerNum==3)
        {return "cyan";}
        else if (answerNum==4)
        {return "blue";}
        else if (answerNum==5)
        {return "magenta";}
        else
        {return "NOTHING!";}
    }
    public void checkAnswer()
    {
        if (colorInt1 == 0 && colorInt2 == 4 || colorInt2 == 0 && colorInt1 == 4) {
            if (answerInt == 5) {
                questionsCorrect++;
                Log.d(TAG, "checkAnswer: Correct!");
            }

        } else {
            if (answerInt == (colorInt1 + colorInt2) / 2) {
                questionsCorrect++;
                Log.d(TAG, "checkAnswer: Correct!");
            }
        }
        questionsAttempted++;
        checkIfFinished();
        genAnswer();

    }

    public int getColorInt1() {
        return colorInt1;
    }

    public void setColorInt1(int colorInt1) {
        this.colorInt1 = colorInt1;
        colorStr1=colorString(colorInt1);
    }

    public int getColorInt2() {
        return colorInt2;
    }

    public void setColorInt2(int colorInt2) {
        this.colorInt2 = colorInt2;
        colorStr2=colorString(colorInt2);
    }

    public String getColorStr1() {
        return colorStr1;
    }

    public int getQuestionsCorrect() {
        return questionsCorrect;
    }

    public int getQuestionAttempted() {
        Log.d(TAG, "getQuestionAttempted: "+questionsAttempted);
        return questionAttempted;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getAnsweredStr() {
        return answeredStr;
    }


}