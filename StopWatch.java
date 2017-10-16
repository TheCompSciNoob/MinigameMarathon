package com.example.chow.minigamemarathon;

import android.os.SystemClock;

/**
 * Created by Kyros on 10/16/2017.
 */

public class StopWatch {

    private long startTime, currentElapsedTime;
    private boolean isPaused;

    public StopWatch()
    {
        stop();
    }

    public void start()
    {
        startTime = SystemClock.elapsedRealtime();
        currentElapsedTime = 0;
        isPaused = false;
    }

    public void pause()
    {
        currentElapsedTime += (SystemClock.elapsedRealtime() - startTime);
        isPaused = true;
    }

    public void resume()
    {
        startTime = SystemClock.elapsedRealtime();
        isPaused = false;
    }

    public void stop()
    {
        currentElapsedTime = 0;
        startTime = 0;
        isPaused = true;
    }

    public long getTimeElapsed()
    {
        if (isPaused)
        {
            return currentElapsedTime;
        }
        else
        {
            return currentElapsedTime + SystemClock.elapsedRealtime() - startTime;
        }
    }
}
