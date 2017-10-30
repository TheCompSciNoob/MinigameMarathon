package com.example.chow.minigamemarathon;

import android.util.Log;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by per6 on 10/13/17.
 */

public class LightsOut{
    private boolean[][] grid;
    private static final int RANDOMIZE_TIMES = 1;
    private OnGridChangeListener listener;
    private final String TAG = "LightsOut Game";
    private static double flipMaxScore = 100, numGeneratePuzzleMaxScore = 50, totalScore = flipMaxScore + numGeneratePuzzleMaxScore;

    public LightsOut(int height, int width)
    {
        grid = new boolean[height][width];
    }

    public void randomize()
    {
        for (int i = 0; i < RANDOMIZE_TIMES; i++)
        {
            Random ran = new Random();
            int ranRow = ran.nextInt(grid.length);
            int ranCol = ran.nextInt(grid[0].length);
            flipSwitch(ranRow, ranCol);
            Log.d(TAG, "randomize: row:" + ranRow + " col:" + ranCol);
        }
    }

    public void flipSwitch(int row, int col)
    {
        grid[row][col] = !grid[row][col];

        if (row+1 < grid.length)
        {
            grid[row+1][col] = !grid[row+1][col];
        }
        if (row-1 >= 0)
        {
            grid[row-1][col] = !grid[row-1][col];
        }
        if (col+1 < grid[0].length)
        {
            grid[row][col+1] = !grid[row][col+1];
        }
        if (col-1 >= 0)
        {
            grid[row][col-1] = !grid[row][col-1];
        }
        //call listener
        if (listener != null)
        {
            listener.onGridChange();
        }
    }

    public static boolean[][] makeCopyOf(boolean[][] oldArray)
    {
        boolean[][] newArray = new boolean[oldArray.length][oldArray[0].length];
        for (int i = 0; i < newArray.length; i++)
        {
            newArray[i] = Arrays.copyOf(oldArray[i], oldArray[i].length);
        }
        return newArray;
    }

    public boolean[][] getGrid()
    {
        return grid;
    }

    public void setGrid(boolean[][] newGrid)
    {
        grid = makeCopyOf(newGrid);
    }

    public boolean isSolved()
    {
        boolean solved = true;
        for (boolean[] row: grid)
        {
            for (boolean col: row)
            {
                if (col)
                {
                    solved = false;
                }
            }
        }
        return solved;
    }

    public void setConGridChangeListener(OnGridChangeListener listener)
    {
        this.listener = listener;
    }

    public static double getPercentScore(int numSwitchFlipped, int numTries)
    {
        int extraSwitches = numSwitchFlipped - RANDOMIZE_TIMES, extraPuzzles = numTries - 1;
        final double switchDepletion = 0.995, generatePuzzleDepletion = .70;
        double flipScore = flipMaxScore * Math.pow(switchDepletion, extraSwitches);
        double puzzleScore = numGeneratePuzzleMaxScore * Math.pow(generatePuzzleDepletion, extraPuzzles);
        return (flipScore + puzzleScore) / totalScore * 100;
    }
}