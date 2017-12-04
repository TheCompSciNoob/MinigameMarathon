package com.example.chow.minigamemarathon;

import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by per6 on 12/4/17.
 */

public class Maze3D {

    private Cell[][][] maze3D;
    private Cell[][] baseMaze;
    private List<PathDirection> directions;
    private static final String TAG = "Maze3D";
    private int startRow, startCol, endRow, endCol;
    private int numRows, numCols;

    public Maze3D(int layers, int numRows, int numCols)
    {
        maze3D = new Cell[layers][numRows][numCols];
        baseMaze = new Cell[numRows][numCols];
        directions = Arrays.asList(PathDirection.allDirections);
        this.numRows = numRows;
        this.numCols = numCols;
        //starting and ending points
        startRow = 0;
        startCol = 0;
        endRow = numRows - 1;
        endCol = numCols - 1;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndCol() {
        return endCol;
    }


    public Cell[][] getBaseMaze() {
        return baseMaze;
    }

    public void generateMaze()
    {
        baseMaze[startRow][startCol] = new Cell();
        generatePathFrom(startRow, startCol);
        Log.d(TAG, "generateMaze: " + baseMaze.toString());
    }

    private void generatePathFrom(int row, int col) //recursive backtracking
    {
        Collections.shuffle(directions);
        int rowOffset = 0, colOffset = 0;
        for (PathDirection direction : directions)
        {
            //gets offset
            switch (direction)
            {
                case UP:
                    rowOffset = -1;
                    colOffset = 0;
                    break;
                case DOWN:
                    rowOffset = 1;
                    colOffset = 0;
                    break;
                case LEFT:
                    rowOffset = 0;
                    colOffset = -1;
                    break;
                case RIGHT:
                    rowOffset = 0;
                    colOffset = 1;
                    break;
            }
            //create path to next cell if cell is not visited
            try
            {
                if (baseMaze[row + rowOffset][col + colOffset] == null)
                {
                    //initialize next cell
                    baseMaze[row + rowOffset][col + colOffset] = new Cell();
                    Cell nextCell = baseMaze[row + rowOffset][col + colOffset];
                    Cell thisCell = baseMaze[row][col];
                    //destroy walls
                    switch (direction)
                    {
                        case UP:
                            thisCell.setWallTop(false);
                            nextCell.setWallBottom(false);
                            break;
                        case DOWN:
                            thisCell.setWallBottom(false);
                            nextCell.setWallTop(false);
                            break;
                        case LEFT:
                            thisCell.setWallLeft(false);
                            nextCell.setWallRight(false);
                            break;
                        case RIGHT:
                            thisCell.setWallRight(false);
                            nextCell.setWallLeft(false);
                            break;
                    }
                    generatePathFrom(row + rowOffset, col + colOffset);
                }
            }
            catch (ArrayIndexOutOfBoundsException e) //edge
            {
                Log.d(TAG, "generatePathFrom: maze generation reached edge");
            }
        }
    }

    public class Cell
    {
        private boolean wallTop, wallBottom, wallLeft, wallRight;

        public Cell()
        {
            wallTop = true;
            wallBottom = true;
            wallLeft = true;
            wallRight =  true;
        }

        public boolean isWallTop() {
            return wallTop;
        }

        public void setWallTop(boolean wallTop) {
            this.wallTop = wallTop;
        }

        public boolean isWallBottom() {
            return wallBottom;
        }

        public void setWallBottom(boolean wallBottom) {
            this.wallBottom = wallBottom;
        }

        public boolean isWallLeft() {
            return wallLeft;
        }

        public void setWallLeft(boolean wallLeft) {
            this.wallLeft = wallLeft;
        }

        public boolean isWallRight() {
            return wallRight;
        }

        public void setWallRight(boolean wallRight) {
            this.wallRight = wallRight;
        }
    }

    private enum PathDirection
    {
        UP, DOWN, LEFT, RIGHT;

        private static final PathDirection[] allDirections = {UP, DOWN, LEFT, RIGHT};
    }
}
