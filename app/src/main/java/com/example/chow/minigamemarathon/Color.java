package com.example.chow.minigamemarathon;

/**
 * Created by per6 on 11/30/17.
 */

public class Color {

    private int colorNumberID;
    private String colorStringID;
    public void genColorID()
    {
        colorNumberID=(int)(Math.random()*3)*2+1;
        if (colorNumberID==0)
        {colorStringID = "red";}
        else if (colorNumberID==1)
        {colorStringID = "yellow";}
        else if (colorNumberID==2)
        {colorStringID = "green";}
        else if (colorNumberID==3)
        {colorStringID = "cyan";}
        else if (colorNumberID==4)
        {colorStringID = "blue";}
        else if (colorNumberID==5)
        {colorStringID = "magenta";}
        else
        {colorStringID = "NOTHING!";}
    }
    public int getColorNumberID() {
        return colorNumberID;
    }

    public void setColorNumberID(int colorNumberID) {
        this.colorNumberID = colorNumberID;
    }

    public String getColorStringID() {
        return colorStringID;
    }

    public void setColorStringID(String colorStringID) {
        this.colorStringID = colorStringID;
    }

}
