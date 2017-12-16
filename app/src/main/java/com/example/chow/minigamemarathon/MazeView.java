package com.example.chow.minigamemarathon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by per6 on 12/4/17.
 */

public class MazeView extends View {

    private static final String TAG = "MazeView";
    private Maze3D.Cell[][][] maze = null;
    //variables for the game
    private Maze3D maze3D;
    private int numRows, numCols;
    private int playerLayer, playerRow, playerCol;
    private Paint line, redIndicator, grayLayerIndicator;
    //variables for the view
    private int width, height, backgroundColor;
    private boolean[][] hLines, vLines;
    private float totalCellWidth, totalCellHeight, cellWidth, cellHeight, lineWidth;
    private Drawable upLayerIndicator, downLayerIndicator, upDownLayerIndicator;

    public MazeView(Context context) {
        super(context);
    }

    public MazeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MazeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setArguments(Maze3D maze3D, int backgroundColor) {
        this.maze3D = maze3D;
        //graphics
        line = new Paint();
        line.setColor(Color.BLACK);
        redIndicator = new Paint();
        redIndicator.setColor(Color.RED);
        redIndicator.setStyle(Paint.Style.STROKE);
        grayLayerIndicator = new Paint();
        grayLayerIndicator.setColor(Color.LTGRAY);
        grayLayerIndicator.setTextAlign(Paint.Align.CENTER);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //gets maze Cell[][][]
        maze = maze3D.getMaze3D();
        //TODO: starting and ending points
        numRows = maze3D.getNumRows();
        numCols = maze3D.getNumCols();
        //up/down layer signs
        upLayerIndicator = getContext().getResources().getDrawable(R.drawable.ic_stairs_up);
        downLayerIndicator = getContext().getResources().getDrawable(R.drawable.ic_stairs_down);
        upDownLayerIndicator = getContext().getResources().getDrawable(R.drawable.ic_stairs_up_down);
        //invalidate();
        //requestLayout();
        setVisibility(VISIBLE);
    }

    public void setPlayerLocation(int playerLayer, int playerRow, int playerCol) {
        this.playerLayer = playerLayer;
        this.playerRow = playerRow;
        this.playerCol = playerCol;
        //store walls as 2 separate boolean 2D arrays
        hLines = getHorizontalLines();
        vLines = getVerticalLines();
        //invalidate and redraw after player location is changed
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int dimension = Math.min(w, h);
        width = dimension;
        height = dimension;
        lineWidth = 3;
        cellWidth = (width - ((float) numCols * lineWidth)) / numCols;
        totalCellWidth = cellWidth + lineWidth;
        cellHeight = (height - ((float) numRows * lineWidth)) / numRows;
        totalCellHeight = cellWidth + lineWidth;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            //draw layer
            Log.d(TAG, "onDraw: " + canvas.getWidth());
            grayLayerIndicator.setTextSize(MazeView.this.getHeight() - 16);
            int drawX = canvas.getWidth() / 2;
            int drawY = (int) ((canvas.getHeight() / 2) - ((grayLayerIndicator.descent() + grayLayerIndicator.ascent()) / 2));
            canvas.drawText(playerLayer + 1 + "", drawX, drawY, grayLayerIndicator);
            //iterate over the boolean arrays to draw walls
            for (int col = 0; col < numCols; col++) {
                for (int row = 0; row < numRows; row++) {
                    Log.d(TAG, "onDraw: iterating in array: size " + numCols);
                    //cell we're referencing
                    Maze3D.Cell thisCell = maze[playerLayer][row][col];
                    //paint location
                    float x = row * totalCellWidth;
                    float y = col * totalCellHeight;
                    if (row < numCols - 1 && vLines[col][row]) {
                        //we'll draw a vertical line
                        canvas.drawLine(x + cellWidth,   //start X
                                y,               //start Y
                                x + cellWidth,   //stop X
                                y + cellHeight,  //stop Y
                                line);
                        Log.d(TAG, "onDraw: stuff is drawn");
                    }
                    if (col < numRows - 1 && hLines[col][row]) {
                        //we'll draw a horizontal line
                        canvas.drawLine(x,              //startX
                                y + cellHeight,  //startY
                                x + cellWidth,   //stopX
                                y + cellHeight,  //stopY
                                line);
                    }
                    //finds appropriate indicator and draw
                    if (!thisCell.isWallBack() && !thisCell.isWallFront()) //both up and down arrows
                    {
                        float modifiedWidth = cellWidth / 2;
                        upDownLayerIndicator.setBounds((int) (y + lineWidth + modifiedWidth / 2),
                                (int) (x + lineWidth + modifiedWidth / 2),
                                (int) (y + cellWidth - lineWidth - modifiedWidth / 2),
                                (int) (x + cellWidth - lineWidth - modifiedWidth / 2));
                        upDownLayerIndicator.draw(canvas);
                    } else if (!thisCell.isWallFront()) //up layer arrow
                    {
                        float modifiedWidth = cellWidth / 2;
                        upLayerIndicator.setBounds((int) (y + lineWidth + modifiedWidth / 2),
                                (int) (x + lineWidth + modifiedWidth / 2),
                                (int) (y + cellWidth - lineWidth - modifiedWidth / 2),
                                (int) (x + cellWidth - lineWidth - modifiedWidth / 2));
                        upLayerIndicator.draw(canvas);
                    } else if (!thisCell.isWallBack()) //down layer arrow
                    {
                        float modifiedWidth = cellWidth / 2;
                        Log.d(TAG, "onDraw: " + ((int) (y + lineWidth + modifiedWidth / 2))
                                + " " + ((int) (x + lineWidth + modifiedWidth / 2))
                                + " " + ((int) (y + cellWidth - lineWidth - modifiedWidth / 2))
                                + " " + ((int) (x + cellWidth - lineWidth - modifiedWidth / 2))
                                + " " + cellWidth);
                        downLayerIndicator.setBounds((int) (y + lineWidth + modifiedWidth / 2),
                                (int) (x + lineWidth + modifiedWidth / 2),
                                (int) (y + cellWidth - lineWidth - modifiedWidth / 2),
                                (int) (x + cellWidth - lineWidth - modifiedWidth / 2));
                        downLayerIndicator.draw(canvas);
                    }
                    //draws the player
                    canvas.drawCircle((playerCol * totalCellWidth) + (cellWidth / 2),   //x of center
                            (playerRow * totalCellHeight) + (cellWidth / 2),  //y of center
                            (cellWidth * 0.45f),                           //radius
                            redIndicator);
                }
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "onDraw: view is drawn before user selects difficulty");
        }
    }

    private boolean[][] getHorizontalLines() {
        boolean[][] horizontalLines = new boolean[maze[playerLayer].length - 1][maze[playerLayer][0].length];
        for (int row = 0; row < maze.length - 1; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                horizontalLines[row][col] = maze[playerLayer][row][col].isWallBottom() || maze[playerLayer][row + 1][col].isWallTop();
            }
        }
        return horizontalLines;
    }

    private boolean[][] getVerticalLines() {
        boolean[][] verticalLines = new boolean[maze[playerLayer].length][maze[playerLayer][0].length - 1];
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length - 1; col++) {
                verticalLines[row][col] = maze[playerLayer][row][col].isWallRight() || maze[playerLayer][row][col + 1].isWallLeft();
            }
        }
        return verticalLines;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dimension = Math.min(widthMeasureSpec, heightMeasureSpec);
        if (dimension != 0) {
            setMeasuredDimension(dimension, dimension);
            super.onMeasure(dimension, dimension);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
