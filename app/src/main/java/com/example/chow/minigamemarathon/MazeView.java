package com.example.chow.minigamemarathon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by per6 on 12/4/17.
 */

public class MazeView extends View {

    //variables for the game
    private Maze3D maze3D;
    private int startRow, startCol, endRow, endCol;
    private int numRows, numCols;
    private Paint line, redIndicator;
    //variables for the view
    private int width, height;

    public MazeView(Context context, Maze3D maze3D, int layer)
    {
        super(context);
        this.maze3D = maze3D;
        //starting and ending points
        startRow = maze3D.getStartRow();
        startCol = maze3D.getStartCol();
        endRow = maze3D.getEndRow();
        endCol = maze3D.getEndCol();
        numRows = maze3D.getNumRows();
        numCols = maze3D.getNumCols();
        //graphics
        line = new Paint(Color.BLACK);
        redIndicator = new Paint(Color.RED);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int dimension = Math.min(w, h);
        width = dimension;
        height = dimension;
        int lineWidth = 1;
        float totalCellWidth = (width - ((float) numCols * lineWidth)) / numCols + lineWidth;
        float totalCellHeight = (height - ((float) numRows * lineWidth)) / numRows + lineWidth;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
