package com.example.kyros.gridviewtest;

import java.util.Random;

/**
 * Created by Kyros on 10/22/2017.
 */

public class LightsOut {

    private boolean[][] grid;
    private final int RANDOMIZE_TIMES = 100;

    public LightsOut(int height, int width)
    {
        grid = new boolean[height][width];
    }


    public void randomize()
    {
        for (int i = 0; i < RANDOMIZE_TIMES; i++)
        {
            Random ran = new Random();
            flipSwitch(ran.nextInt(grid.length), ran.nextInt(grid[0].length));
        }
    }

    public void flipSwitch(int row, int col) {
        grid[row][col] = !grid[row][col];
        try {
            grid[row - 1][col] = !grid[row - 1][col];
        } catch (Exception e) {
        }
        try {
            grid[row + 1][col] = !grid[row + 1][col];
        } catch (Exception e) {}
        try {
            grid[row][col-1] = !grid[row][col-1];
        } catch (Exception e) {}
        try {
            grid[row][col+1] = !grid[row][col+1];
        } catch (Exception e) {}
    }

    public boolean[][] getGrid()
    {
        return grid;
    }

    public boolean isSolved()
    {
        for (boolean[] row : grid)
        {
            for (boolean col : row)
            {
                if (col)
                {
                    return false;
                }
            }
        }
        return  true;
    }
}
