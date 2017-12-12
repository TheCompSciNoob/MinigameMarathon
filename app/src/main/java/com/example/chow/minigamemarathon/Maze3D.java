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
    private List<PathDirection> directions;
    private static final String TAG = "Maze3D";
    private int startRow, startCol, startLayer, endRow, endCol, endLayer;
    private int numRows, numCols;

    public Maze3D(int layers, int numRows, int numCols)
    {
        maze3D = new Cell[layers][numRows][numCols];
        directions = Arrays.asList(PathDirection.allDirections);
        this.numRows = numRows;
        this.numCols = numCols;
        //starting and ending points
        startRow = 0;
        startCol = 0;
        startLayer = 0;
        endRow = numRows - 1;
        endCol = numCols - 1;
        endLayer = layers - 1;
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

    public int getEndLayer()
    {
        return endLayer;
    }


    public Cell[][][] getMaze3D() {
        return maze3D;
    }

    public void generateMaze()
    {
        maze3D[startLayer][startRow][startCol] = new Cell();
        generatePathFrom(startLayer, startRow, startCol);
    }

    private void generatePathFrom(int layer, int row, int col) //recursive backtracking
    {
        Collections.shuffle(directions);
        for (PathDirection direction : directions)
        {
        int layerOffset = 0, rowOffset = 0, colOffset = 0;
            //gets offset
            switch (direction)
            {
                case UP:
                    rowOffset = -1;
                    break;
                case DOWN:
                    rowOffset = 1;
                    break;
                case LEFT:
                    colOffset = -1;
                    break;
                case RIGHT:
                    colOffset = 1;
                    break;
                case FRONT:
                    layerOffset = -1;
                    break;
                case BACK:
                    layerOffset = 1;
                    break;
            }
            //create path to next cell if cell is not visited
            try
            {
                if (maze3D[layer + layerOffset][row + rowOffset][col + colOffset] == null)
                {
                    //initialize next cell
                    maze3D[layer + layerOffset][row + rowOffset][col + colOffset] = new Cell();
                    Cell nextCell = maze3D[layer + layerOffset][row + rowOffset][col + colOffset];
                    Cell thisCell = maze3D[layer][row][col];
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
                        case FRONT:
                            thisCell.setWallFront(false);
                            nextCell.setWallBack(false);
                            break;
                        case BACK:
                            thisCell.setWallBack(false);
                            nextCell.setWallFront(false);
                            break;
                    }
                    generatePathFrom(layer + layerOffset, row + rowOffset, col + colOffset);
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
        private boolean wallTop;
        private boolean wallBottom;
        private boolean wallLeft;
        private boolean wallRight;
        private boolean wallFront;
        private boolean wallBack;

        public Cell()
        {
            wallTop = true;
            wallBottom = true;
            wallLeft = true;
            wallRight =  true;
            wallFront = true;
            wallBack = true;
        }

        public boolean isWallFront() {
            return wallFront;
        }

        public void setWallFront(boolean wallFront) {
            this.wallFront = wallFront;
        }

        public boolean isWallBack() {
            return wallBack;
        }

        public void setWallBack(boolean wallBack) {
            this.wallBack = wallBack;
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

    public enum PathDirection
    {
        UP, DOWN, LEFT, RIGHT, FRONT, BACK;

        private static final PathDirection[] allDirections = {UP, DOWN, LEFT, RIGHT, FRONT, BACK};
    }
}
