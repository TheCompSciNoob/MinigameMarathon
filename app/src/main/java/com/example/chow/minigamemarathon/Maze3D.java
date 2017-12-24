package com.example.chow.minigamemarathon;

import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by per6 on 12/4/17.
 */

public class Maze3D {

    //directions
    public static final int NONE = 3000, UP = 3001, DOWN = 3002, LEFT = 3003, RIGHT = 3004, FRONT = 3005, BACK = 3006;
    public static final Integer[] ALL_DIRECTIONS = {UP, DOWN, LEFT, RIGHT, FRONT, BACK};
    //instance variables
    private Cell[][][] maze3D;
    private static final String TAG = "Maze3D";
    private int startRow, startCol, startLayer, endRow, endCol, endLayer;
    private int numRows, numCols;

    public Maze3D(int layers, int numRows, int numCols) {
        maze3D = new Cell[layers][numRows][numCols];
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

    public int getEndLayer() {
        return endLayer;
    }


    public Cell[][][] getMaze3D() {
        return maze3D;
    }

    public void generateMaze() {
        Cell startCell = new Cell(startLayer, startRow, startCol, null);
        maze3D[startLayer][startRow][startCol] = startCell;
        Stack<Cell> cellStack = new Stack<>();
        cellStack.push(startCell);
        while (!cellStack.isEmpty()) {
            int layerOffset = 0, rowOffset = 0, colOffset = 0;
            Cell topCell = cellStack.peek();
            int direction = topCell.nextDirection();
            switch (direction) {
                case NONE:
                    cellStack.pop();
                    if (topCell.fromCell != null) {
                        topCell.fromCell.setIsSolutionCell(topCell.isSolutionCell || topCell.fromCell.isSolutionCell);
                    }
                    if (topCell.isSolutionCell) {
                        Log.d(TAG, "generateMaze: debug " + topCell.layer + " " + topCell.row + " " + topCell.col);
                    }
                    continue;
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
            try {
                int newLayer = topCell.layer + layerOffset, newRow = topCell.row + rowOffset, newCol = topCell.col + colOffset;
                if (maze3D[newLayer][newRow][newCol] == null) {
                    Cell newCell = new Cell(newLayer, newRow, newCol, topCell);
                    maze3D[newLayer][newRow][newCol] = newCell;
                    cellStack.push(newCell);
                    if (newLayer == endLayer && newRow == endRow && newCol == endCol) {
                        newCell.setIsSolutionCell(true);
                    }
                    //destroy walls
                    switch (direction) {
                        case UP:
                            topCell.setWallTop(false);
                            newCell.setWallBottom(false);
                            break;
                        case DOWN:
                            topCell.setWallBottom(false);
                            newCell.setWallTop(false);
                            break;
                        case LEFT:
                            topCell.setWallLeft(false);
                            newCell.setWallRight(false);
                            break;
                        case RIGHT:
                            topCell.setWallRight(false);
                            newCell.setWallLeft(false);
                            break;
                        case FRONT:
                            topCell.setWallFront(false);
                            newCell.setWallBack(false);
                            break;
                        case BACK:
                            topCell.setWallBack(false);
                            newCell.setWallFront(false);
                            break;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.d(TAG, "generateMaze: maze generation reached edge");
            }
        }
    }

    public int getMinMovesToSolution() {
        int numCellSolutions = 0;
        for (Cell[][] layer : maze3D) {
            for (Cell[] row : layer) {
                for (Cell col : row) {
                    if (col == null) {
                        Log.e(TAG, "getMinMovesToSolution: cell is null", null);
                    }
                    if (col.isSolutionCell) {
                        numCellSolutions++;
                    }
                }
            }
        }
        return numCellSolutions;
    }

    public class Cell {

        //wall properties
        private boolean wallTop;
        private boolean wallBottom;
        private boolean wallLeft;
        private boolean wallRight;
        private boolean wallFront;
        private boolean wallBack;
        private Cell fromCell;
        private int layer, row, col;
        //check if it is on solution path
        private boolean isSolutionCell;
        //checking for remaining directions
        private List<Integer> directions;
        private int directionIndex;

        public Cell(int layer, int row, int col, Cell fromCell) {
            //walls will be destroyed later
            wallTop = true;
            wallBottom = true;
            wallLeft = true;
            wallRight = true;
            wallFront = true;
            wallBack = true;
            //cell location for recursion
            this.fromCell = fromCell;
            this.layer = layer;
            this.row = row;
            this.col = col;
            //possible directions
            directionIndex = -1;
            directions = Arrays.asList(ALL_DIRECTIONS);
            Collections.shuffle(directions);
        }

        private int nextDirection() {
            directionIndex++;
            if (directionIndex >= directions.size()) {
                return NONE;
            }
            return directions.get(directionIndex);
        }

        public void setIsSolutionCell(boolean solutionCell) {
            isSolutionCell = solutionCell;
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
}
