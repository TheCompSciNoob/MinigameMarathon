package com.example.chow.minigamemarathon;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by per6 on 12/12/17.
 */

public class LayerIndicatorTextView extends android.support.v7.widget.AppCompatTextView {

    public LayerIndicatorTextView(Context context) {
        super(context);
    }

    public LayerIndicatorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LayerIndicatorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dimension = Math.min(widthMeasureSpec, heightMeasureSpec);
        if (dimension != 0)
        {
            int margin = 8;
            setTextSize(dimension - margin * 2);
            setMeasuredDimension(dimension, dimension);
            super.onMeasure(dimension, dimension);
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
