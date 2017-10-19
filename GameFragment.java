package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kyros on 10/16/2017.
 */

public abstract class GameFragment extends Fragment {

    private GameEventListener listener = null;
    private int layoutResId;

    public GameFragment(int layoutResId)
    {
        this.layoutResId = layoutResId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(layoutResId, container, false);
        return rootView;
    }

    public abstract boolean isGame();

    public abstract void initializeVariables();

    public abstract boolean isSolved();

    public void setGameEventListener(GameEventListener listener)
    {
        this.listener = listener;
    }

    protected void start()
    {
        if (listener != null)
        {
            listener.onGameStart();
        }
    }

    protected void finish()
    {
        if (listener != null)
        {
            listener.onGameEnd();
        }
    }
}
