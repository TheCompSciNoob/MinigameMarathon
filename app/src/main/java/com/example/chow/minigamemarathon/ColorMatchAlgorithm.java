package com.example.chow.minigamemarathon;

/**
 * Created by per6 on 11/28/17.
 */

public class ColorMatchAlgorithm {
    private String colorStr1,colorStr2;
    public int getColorNumber(String colorstring)
    {
        //red-0
        //yellow-1
        //green-2
        //cyan-3
        //blue-4
        //magenta-5
        if (colorstring.equals("red")){return 0;}
        else if (colorstring.equals("yellow")){return 1;}
        else if (colorstring.equals("green")){return 2;}
        else if (colorstring.equals("cyan")){return 3;}
        else if (colorstring.equals("blue")){return 4;}
        else if (colorstring.equals("magenta")){return 5;}
        else {return -1;}
    }
    public boolean checkColorMatch(int color1, int color2, int answercolor)
    {
        if (color1==0&&color2==4 || color2==0&&color1==4)
        {
            if (answercolor==5)
            {
                return true;
            }
            else {return false;}
        }
        else
        {
            if(answercolor==(color1+color2)/2)
            {return true;}
            else {return false;}
        }
    }
}
