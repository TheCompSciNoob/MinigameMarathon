package com.example.chow.minigamemarathon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by per6 on 12/4/17.
 */

public class Maze3DGameFragment extends GameFragment implements View.OnTouchListener, View.OnClickListener {

    private GameMode gameMode;
    private Maze3D maze3D;
    private LinearLayout rootView;
    private int playerLayer = 0, playerRow = 0, playerCol = 0;
    private MazeView mazeView;

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
        maze3D = new Maze3D(1, 20, 20);
        maze3D.generateMaze();
        //"if this works I'm gonna kill myself" - Chi,2017
        mazeView = new MazeView(getActivity(), maze3D, Color.TRANSPARENT);
        mazeView.setPlayerLocation(playerLayer, playerRow, playerCol);
        mazeView.setOnTouchListener(this);
        rootView.addView(mazeView,0);
        Button upLayerOption = rootView.findViewById(R.id.up_layer_option);
        upLayerOption.setOnClickListener(this);
        Button downLayerOption = rootView.findViewById(R.id.down_layer_option);
        downLayerOption.setOnClickListener(this);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.performClick();
        int x = (int) motionEvent.getX();
        int y = mazeView.getHeight() - (int) motionEvent.getY();
        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_UP:
                tryMovePlayer(x, y);
                break;
        }
        return true;
    }

    private void tryMovePlayer(int touchX, int touchY) {
        double w = mazeView.getWidth(), h = mazeView.getHeight();
        int rowOffset = 0, colOffset = 0;
        Maze3D.PathDirection direction = null;
        //find where the player touched the screen
        if (touchY > h / w * touchX && touchY > - h / w * touchX + h) //top quadrant
        {
            rowOffset = -1;
            direction = Maze3D.PathDirection.UP;
        }
        else if (touchY > h / w * touchX && touchY > - h / w * touchX + h) //bottom quadrant
        {
            rowOffset = 1;
            direction = Maze3D.PathDirection.DOWN;
        }
        else if (touchX < w / h * touchY && touchX < w / h * (touchY - h)) //left quadrant
        {
            colOffset = -1;
            direction = Maze3D.PathDirection.LEFT;
        }
        else if (touchX > w / h * touchY && touchX > w / h * (touchY - h))
        {
            colOffset = 1;
            direction = Maze3D.PathDirection.RIGHT;
        }
        //check if the player can move to new cell
        boolean canMove = true;
        if (direction != null)
        {
            //TODO: check if player can move
            switch (direction)
            {
                case UP:
                    break;

                case DOWN:

                    break;
                case LEFT:

                    break;
                case RIGHT:

                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

        }
    }
}
