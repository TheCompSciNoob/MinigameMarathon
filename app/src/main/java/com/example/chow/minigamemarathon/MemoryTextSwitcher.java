package com.example.chow.minigamemarathon;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextSwitcher;

import java.util.ArrayList;

/**
 * Created by Kyros on 11/21/2017.
 */

public class MemoryTextSwitcher extends TextSwitcher {

    private ArrayList<CharSequence> informationList = new ArrayList<>();

    public MemoryTextSwitcher(Context context) {
        super(context);
        setAnimations();
    }

    public MemoryTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAnimations();
    }

    private void setAnimations()
    {
        setInAnimation(getContext(), android.R.anim.fade_in);
        setOutAnimation(getContext(), android.R.anim.fade_out);
    }

    public void addText(CharSequence newText)
    {
        informationList.add(newText);
    }

    public boolean removeText(CharSequence removeText)
    {
        return informationList.remove(removeText);
    }

    public CharSequence removeText(int position)
    {
        return informationList.remove(position);
    }

    public void clearText()
    {
        informationList.clear();
    }

    public void displayCurrentTextAtPosition(int position)
    {
        setCurrentText(informationList.get(position));
    }

    public void displayTextAtPosition(int position)
    {
        setText(informationList.get(position));
    }
}
