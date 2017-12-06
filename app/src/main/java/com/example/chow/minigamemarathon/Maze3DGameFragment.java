package com.example.chow.minigamemarathon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by per6 on 12/4/17.
 */

public class Maze3DGameFragment extends GameFragment {

    private GameMode gameMode;
    private LinearLayout rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = (LinearLayout) inflater.inflate(R.layout.maze_3d_layout, container, false);
        return rootView;
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void assignWidgetFunctions() {
        Maze3D maze3D = new Maze3D(1, 20, 20);
        maze3D.generateMaze();
        //"if this works I'm gonna kill myself" - Chi,2017
        MazeView mazeView = new MazeView(getActivity(), maze3D, Color.TRANSPARENT);
        mazeView.setPlayerLocation(0, 0, 0);
        rootView.addView(mazeView, 0);
    }

    @Override
    public String getGameName() {
        return "Maze 3D";
    }

    @Override
    public double getPercentScore() {
        return 0;
    }

    @Override
    public boolean isSolved() {
        return false;
    }
}
