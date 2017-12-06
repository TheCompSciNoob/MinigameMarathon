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
    private int numRows, numCols;
    private int playerRow, playerCol;
    private Paint line, redIndicator, boundingBox;
    //variables for the view
    private int width, height, backgroundColor;
    private boolean[][] hLines, vLines;
    private float totalCellWidth;
    private float totalCellHeight;
    private float cellWidth;
    private float cellHeight;

    public MazeView(Context context, Maze3D maze3D, int backgroundColor)
    {
        super(context);
        this.maze3D = maze3D;
        //starting and ending points
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
    }

    public void setPlayerLocation(int playerLayer, int playerRow, int playerCol)
    {
        //store walls as 2 separate boolean 2D arrays
        hLines = getHorizontalLines(playerLayer);
        vLines = getVerticalLines(playerLayer);
        this.playerRow = playerRow;
        this.playerCol = playerCol;
        //TODO change player row and player col
        //invalidate and redraw after player location is changed
        invalidate();
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
                    canvas.drawLine(x,              //startX
                            y + cellHeight,  //startY
                            x + cellWidth,   //stopX
                            y + cellHeight,  //stopY
                            line);
                }
                //draws the player
                canvas.drawCircle((playerCol * totalCellWidth)+(cellWidth/2),   //x of center
                        (playerRow * totalCellHeight)+(cellWidth/2),  //y of center
                        (cellWidth*0.45f),                           //radius
                        redIndicator);
            }
        }
    }

    private boolean[][] getHorizontalLines(int layer)
    {
        Maze3D.Cell[][][] maze = maze3D.getMaze3D();
        boolean[][] horizontalLines = new boolean[maze[layer].length-1][maze[layer][0].length];
        for (int row = 0; row < maze.length - 1; row++) {
            for (int col = 0; col < maze[row].length; col++)
            {
                horizontalLines[row][col] = maze[layer][row][col].isWallBottom() || maze[layer][row+1][col].isWallTop();
            }
        }
        return horizontalLines;
    }

    private boolean[][] getVerticalLines(int layer)
    {
        Maze3D.Cell[][][] maze = maze3D.getMaze3D();
        boolean[][] verticalLines = new boolean[maze[layer].length][maze[layer][0].length-1];
        for (int row = 0; row < maze.length; row++)
        {
            for (int col = 0; col < maze[row].length - 1; col++)
            {
                verticalLines[row][col] = maze[layer][row][col].isWallRight() || maze[layer][row][col+1].isWallLeft();
            }
        }
        return verticalLines;
    }
}
