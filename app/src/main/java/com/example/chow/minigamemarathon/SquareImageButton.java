package com.example.chow.minigamemarathon;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Kyros on 11/21/2017.
 */

public class SquareImageButton extends android.support.v7.widget.AppCompatImageButton {

    public SquareImageButton(Context context) {
        super(context);
    }

    public SquareImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        int height =  getMeasuredHeight();
        setMeasuredDimension(height, height);
    }
}
