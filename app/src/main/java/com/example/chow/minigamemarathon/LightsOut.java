package com.example.chow.minigamemarathon;

import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by per6 on 10/13/17.
 */

public class LightsOut{
    private boolean[][] grid;
    private int randomizeTimes;
    private static final int RANDOMIZE_TIMES_EASY = 10, RANDOMIZE_TIMES_HARD = 30, RANDOMIZE_TIMES_DEBUG = 1;
    private final String TAG = "LightsOut Game";
    private static double flipMaxScore = 100, numGeneratePuzzleMaxScore = 50, totalScore = flipMaxScore + numGeneratePuzzleMaxScore;

    public LightsOut(GameMode gameMode)
    {
        switch (gameMode)
        {
            case EASY:
                grid = new boolean[5][5];
                Log.d(TAG, "LightsOut: EASY");
                randomizeTimes = RANDOMIZE_TIMES_EASY;
                break;
            case HARD:
                grid = new boolean[8][8];
                Log.d(TAG, "LightsOut: HARD");
                randomizeTimes = RANDOMIZE_TIMES_HARD;
                break;
            case DEBUG:
                grid = new boolean[5][5];
                Log.d(TAG, "LightsOut: DEBUG");
                randomizeTimes = RANDOMIZE_TIMES_DEBUG;
                break;
            default:
        }
    }

    public void randomize()
    {
        for (int i = 0; i < randomizeTimes; i++)
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
        Log.d(TAG, "isSolved: " + solved);
        return solved;
    }

    public static double getPercentScore(int numSwitchFlipped, int numTries, GameMode gameMode)
    {
        int RANDOMIZE_TIMES = 0;
        switch (gameMode)
        {
            case EASY:
                RANDOMIZE_TIMES = RANDOMIZE_TIMES_EASY;
                break;
            case HARD:
                RANDOMIZE_TIMES = RANDOMIZE_TIMES_HARD;
                break;
            case DEBUG:
                RANDOMIZE_TIMES = RANDOMIZE_TIMES_DEBUG;
                break;
        }
        int extraSwitches = numSwitchFlipped - RANDOMIZE_TIMES, extraPuzzles = numTries - 1;
        final double switchDepletion = 0.995, generatePuzzleDepletion = .70;
        double flipScore = flipMaxScore * Math.pow(switchDepletion, extraSwitches);
        double puzzleScore = numGeneratePuzzleMaxScore * Math.pow(generatePuzzleDepletion, extraPuzzles);
        return (flipScore + puzzleScore) / totalScore;
    }
}