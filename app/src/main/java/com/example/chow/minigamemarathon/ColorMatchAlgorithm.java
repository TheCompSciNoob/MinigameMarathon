package com.example.chow.minigamemarathon;

/**
 * Created by per6 on 11/28/17.
 */

public class ColorMatchAlgorithm {
    private Color colorSelect1, colorSelect2, answeredColor;
    private boolean finished=false;
    private int questionsAnswered=0;

    public void checkIfFinished()
    {
        if (questionsAnswered>=5)
        {finished=true}
    }

    public void genAnswer()
    {
        answeredColor.genColorID();
    }

    public void checkAnswer()
    {
        if (colorSelect1.getColorNumberID() == 0 && colorSelect2.getColorNumberID() == 4 || colorSelect2.getColorNumberID() == 0 && colorSelect1.getColorNumberID() == 4) {
            if (answeredColor.getColorNumberID() == 5)
            {
                questionsAnswered++;
            }

        }
        else {
            if (answeredColor.getColorNumberID() == (colorSelect1.getColorNumberID() + colorSelect2.getColorNumberID()) / 2)
            {
                questionsAnswered++;
            }
        }
    }
