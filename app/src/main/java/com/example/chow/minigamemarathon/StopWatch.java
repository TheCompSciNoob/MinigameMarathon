package com.example.chow.minigamemarathon;

import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Kyros on 10/25/2017.
 */

public class StopWatch {

    private CountDownTimer timer;
    private static final long START_TIME = Long.MAX_VALUE;
    private long updateInterval, lapTimeElapsed, totalTimeElapsed, lastUpdateTime;
    private OnTickListener listener;
    private ArrayList<Long> laps;

    //test
    private final String TAG = "StopWatch";

    public StopWatch(long updateInterval) {
        this.updateInterval = updateInterval;
        lapTimeElapsed = 0;
        laps = new ArrayList<>();
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
                lapTimeElapsed += (lastUpdateTime - millisUntilFinished);
                lastUpdateTime = millisUntilFinished;
                if (listener != null)
                {
                    listener.onTick(lapTimeElapsed, totalTimeElapsed + lapTimeElapsed);
                }
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
        initTimer();
        timer.start();
        Log.d(TAG, "start: timer started");
    }

    public void pause()
    {
        if (timer != null)
        {
            timer.cancel();
        }
    }

    public void resume()
    {
        initTimer();
        timer.start();
    }

    public void stop()
    {
        if (timer != null)
        {
            timer.cancel();
        }
        lapTimeElapsed = 0;
    }

    public void lap()
    {
        laps.add(lapTimeElapsed);
        totalTimeElapsed += lapTimeElapsed;
        lapTimeElapsed = 0;
    }

    public ArrayList<Long> getLaps()
    {
        return laps;
    }

    public void removeOnTickListener()
    {
        listener = null;
    }

    public void setOnTickListener(OnTickListener listener)
    {
        this.listener = listener;
    }

    public long getTotalTimeElapsed()
    {
        return totalTimeElapsed;
    }

    public long getLapTimeElapsed()
    {
        return lapTimeElapsed;
    }


    public interface OnTickListener
    {
        public void onTick(long lapTimeElapsed, long totalTimeElapsed);
    }
}