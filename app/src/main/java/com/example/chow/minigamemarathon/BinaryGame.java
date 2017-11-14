package com.example.chow.minigamemarathon;


import android.util.Log;

import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by per6 on 10/20/17.
 */

public class BinaryGame {

    private int numChars;

    public BinaryGame(GameMode gameMode) {
        numChars = getNumChars(gameMode);
    }

    private int getNumChars(GameMode gameMode)
    {
        switch (gameMode)
        {
            case EASY:
                return 50;
            case HARD:
                return 100;
            case DEBUG:
                return 2;
            default:
                return -1;
        }
    }


    public String getBinaryString(){
        final String CHARS = "01";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while(sb.length() < numChars){
            int index = (int) (rnd.nextFloat() * CHARS.length());
            sb.append(CHARS.charAt(index));
        }
        return sb.toString();
    }
}
