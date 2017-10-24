package com.example.chow.minigamemarathon;

import java.util.Random;

/**
 * Created by per6 on 10/13/17.
 */

public class LightsOut{
    private boolean[][] grid;
    private final int RANDOMIZE_TIMES = 2;
    private OnGridChangeListener listener;

    public LightsOut(int width, int height)
    {
        grid = new boolean[width][height];
        grid[3][2] = true;
    }

    public void randomize()
    {
        for (int i = 0; i < RANDOMIZE_TIMES; i++)
        {
            Random ran = new Random();
            flipSwitch(ran.nextInt(grid.length), ran.nextInt(grid[0].length));
        }
    }

    public void flipSwitch(int row, int col)
    {
        grid[row][col] = !grid[row][col];
        try
        {
            grid[row+1][col] = !grid[row+1][col];
        }
        catch (Exception e)
        {

        }
        try
        {
            grid[row][col+1] = !grid[row+1][col+1];
        }
        catch (Exception e)
        {

        }
        try
        {
            grid[row-1][col] = !grid[row-1][col];
        }
        catch (Exception e)
        {

        }
        try
        {
            grid[row][col-1] = !grid[row][col-1];
        }
        catch (Exception e)
        {

        }
        if (listener != null)
        {
            listener.onGridChange();
        }
    }

    public boolean[][] getGrid()
    {
        return grid;
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
}