package com.example.chow.minigamemarathon;

import java.util.Random;

/**
 * Created by per6 on 10/20/17.
 */

public class BinaryGame {
    public BinaryGame(){

    }
    public String getBinaryString(){
        final String CHARS = "01";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while(sb.length() < 50){
            int index = (int) (rnd.nextFloat() * CHARS.length());
            sb.append(CHARS.charAt(index));
        }
        return sb.toString();
    }
}
