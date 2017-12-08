package com.example.chow.minigamemarathon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
    private Maze3D mazeHandler;
    private Maze3D.Cell[][][] maze3D;
    private LinearLayout rootView;
    private int playerLayer = 0, playerRow = 0, playerCol = 0;
    private MazeView mazeView;
    private static final String TAG = "Maze3DGameFragment";

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
        mazeHandler = new Maze3D(5, 5, 5);
        mazeHandler.generateMaze();
        maze3D = mazeHandler.getMaze3D();
        //"if this works I'm gonna kill myself" - Chi,2017
        mazeView = new MazeView(getActivity(), mazeHandler, Color.TRANSPARENT);
        mazeView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
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
        int x = (int) motionEvent.getX();
        int y = mazeView.getHeight() - (int) motionEvent.getY();
        Log.d(TAG, "onTouch: point " + x + " " + y);
        double w = mazeView.getWidth(), h = mazeView.getHeight();
        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_UP:
                if (y > h / w * x && y > - h / w * x + h) //top quadrant
                {
                    Log.d(TAG, "onTouch: up");
                    tryMovePlayer(Maze3D.PathDirection.UP);
                }
                else if (y < h / w * x && y < - h / w * x + h) //bottom quadrant
                {
                    Log.d(TAG, "onTouch: down");
                    tryMovePlayer(Maze3D.PathDirection.DOWN);
                }
                else if (y < h / w * x && y > - h / w * x + h)
                {
                    Log.d(TAG, "onTouch: right");
                    tryMovePlayer(Maze3D.PathDirection.RIGHT);
                }
                else if (y > h / w * x && y < - h / w * x + h) //left quadrant
                {
                    Log.d(TAG, "onTouch: left");
                    tryMovePlayer(Maze3D.PathDirection.LEFT);
                }
                break;
        }
        Log.d(TAG, "onTouch: player location " + playerLayer + " " + playerRow + " " + playerCol);
        return true;
    }

    private void tryMovePlayer(@NonNull Maze3D.PathDirection direction) {
        //check if the player can move to new cell
        int layerOffset = 0, rowOffset = 0, colOffset = 0;
        boolean canMove = false;
        Maze3D.Cell currentCell = maze3D[playerLayer][playerRow][playerCol];
        Maze3D.Cell nextCell = null;
        try {
            switch (direction)
            {
                case UP:
                    nextCell = maze3D[playerLayer][playerRow - 1][playerCol];
                    canMove = !(currentCell.isWallTop() || nextCell.isWallBottom());
                    rowOffset = -1;
                    break;
                case DOWN:
                    nextCell = maze3D[playerLayer][playerRow + 1][playerCol];
                    canMove = !(currentCell.isWallBottom() || nextCell.isWallTop());
                    rowOffset = 1;
                    break;
                case LEFT:
                    nextCell = maze3D[playerLayer][playerRow][playerCol - 1];
                    canMove = !(currentCell.isWallLeft() || nextCell.isWallRight());
                    colOffset = -1;
                    break;
                case RIGHT:
                    nextCell = maze3D[playerLayer][playerRow][playerCol + 1];
                    canMove = !(currentCell.isWallRight() || nextCell.isWallLeft());
                    colOffset = 1;
                    break;
                case FRONT:
                    nextCell = maze3D[playerLayer - 1][playerRow][playerCol];
                    canMove = !(currentCell.isWallFront() || nextCell.isWallBack());
                    layerOffset = -1;
                    break;
                case BACK:
                    nextCell = maze3D[playerLayer + 1][playerRow][playerCol];
                    canMove = !(currentCell.isWallBack() || nextCell.isWallFront());
                    layerOffset = 1;
                    break;
            }
            //if cell is not null and can move, then move
            if (nextCell != null && canMove)
            {
                Log.d(TAG, "tryMovePlayer: player moved");
                playerLayer += layerOffset;
                playerRow += rowOffset;
                playerCol += colOffset;
                mazeView.setPlayerLocation(playerLayer, playerRow, playerCol);
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            Log.d(TAG, "tryMovePlayer: player tried to move to edge");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.up_layer_option:
                tryMovePlayer(Maze3D.PathDirection.FRONT);
                break;
            case R.id.down_layer_option:
                tryMovePlayer(Maze3D.PathDirection.BACK);
                break;
        }
    }
}
