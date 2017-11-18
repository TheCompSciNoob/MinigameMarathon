package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kyros on 11/17/2017.
 */

public class HighScoreTabFragment extends Fragment {

    private GameMode gameMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setGameMode(GameMode gameMode)
    {
        //TODO: sort out data that is only of this gamemode
        this.gameMode = gameMode;
    }
}
