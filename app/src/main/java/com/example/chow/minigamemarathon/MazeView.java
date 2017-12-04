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
    private Paint line, redIndicator, boundingBox;
    //variables for the view
    private int width, height, backgroundColor;
    private boolean[][] hLines, vLines;
    private float totalCellWidth;
    private float totalCellHeight;
    private float cellWidth;
    private float cellHeight;

    public MazeView(Context context, Maze3D maze3D, int backgroundColor, int layer)
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
        line = new Paint();
        line.setColor(Color.BLACK);
        redIndicator = new Paint();
        redIndicator.setColor(Color.RED);
        boundingBox = new Paint();
        boundingBox.setColor(backgroundColor);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //store walls as 2 separate boolean 2D arrays
        hLines = getHorizontalLines();
        vLines = getVerticalLines();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int dimension = Math.min(w, h);
        width = dimension;
        height = dimension;
        int lineWidth = 1;
        cellWidth = (width - ((float) numCols * lineWidth)) / numCols;
        totalCellWidth = cellWidth + lineWidth;
        cellHeight = (height - ((float) numRows * lineWidth)) / numRows;
        totalCellHeight = cellWidth + lineWidth;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //fill boundingBox
        canvas.drawRect(0, 0, width, height, boundingBox);
        //iterate over the boolean arrays to draw walls
        for(int col = 0; col < numCols; col++) {
            for(int row = 0; row < numRows; row++){
                float x = row * totalCellWidth;
                float y = col * totalCellHeight;
                if(row < numCols - 1 && vLines[col][row]) {
                    //we'll draw a vertical line
                    canvas.drawLine(x + cellWidth,   //start X
                            y,               //start Y
                            x + cellWidth,   //stop X
                            y + cellHeight,  //stop Y
                            line);
                }
                if(col < numRows - 1 && hLines[col][row]) {
                    //we'll draw a horizontal line
                    canvas.drawLine(x,               //startX
                            y + cellHeight,  //startY
                            x + cellWidth,   //stopX
                            y + cellHeight,  //stopY
                            line);
                }
            }
        }
    }

    private boolean[][] getHorizontalLines()
    {
        Maze3D.Cell[][] maze = maze3D.getBaseMaze();
        boolean[][] horizontalLines = new boolean[maze.length-1][maze[0].length];
        for (int row = 0; row < maze.length - 1; row++) {
            for (int col = 0; col < maze[row].length; col++)
            {
                horizontalLines[row][col] = maze[row][col].isWallBottom() || maze[row+1][col].isWallTop();
            }
        }
        return horizontalLines;
    }

    private boolean[][] getVerticalLines()
    {
        Maze3D.Cell[][] maze = maze3D.getBaseMaze();
        boolean[][] verticalLines = new boolean[maze.length][maze[0].length-1];
        for (int row = 0; row < maze.length; row++)
        {
            for (int col = 0; col < maze[row].length - 1; col++)
            {
                verticalLines[row][col] = maze[row][col].isWallRight() || maze[row][col+1].isWallLeft();
            }
        }
        return verticalLines;
    }
}
