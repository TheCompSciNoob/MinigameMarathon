package com.example.chow.minigamemarathon;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by Kyros on 10/25/2017.
 */

public abstract class StopWatch {

    private CountDownTimer timer;
    private static final long START_TIME = Long.MAX_VALUE;
    private long updateInterval, timeElapsed, lastUpdateTime;

    //test
    private final String TAG = "StopWatch";

    public StopWatch(long updateInterval) {
        this.updateInterval = updateInterval;
        timeElapsed = 0;
        timer = null;
    }

    private void initTimer()
    {
        Log.d(TAG, "initTimer: timer initialized");
        if (timer != null)
        {
            timer.cancel();
        }
        lastUpdateTime = START_TIME;
        timer = new CountDownTimer(START_TIME, updateInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeElapsed += (lastUpdateTime - millisUntilFinished);
                lastUpdateTime = millisUntilFinished;
                StopWatch.this.onTick(timeElapsed);
            }

            @Override
            public void onFinish() {
                //nothing
                //just for structure
            }
        };
    }

    public void start()
    {
        Log.d(TAG, "start: start() is called");
        initTimer();
        timer.start();
    }

    public void pause()
    {
        timer.cancel();
    }

    public void resume()
    {
        initTimer();
        timer.start();
        Log.d(TAG, "resume: resumed");
    }

    public void stop()
    {
        timer.cancel();
        timeElapsed = 0;
    }

    public long getTimeElapsed()
    {
        return timeElapsed;
    }

    public abstract void onTick(long timeElapsed);
}