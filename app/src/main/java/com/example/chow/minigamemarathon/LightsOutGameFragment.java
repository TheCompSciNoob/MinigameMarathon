package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Kyros on 10/22/2017.
 */

public class LightsOutGameFragment extends GameFragment implements View.OnClickListener, ViewTreeObserver.OnPreDrawListener {

    private BaseAdapter adapter;
    ArrayList<Boolean> translatedList;
    private LightsOut game;
    private boolean[][] originalGrid;
    private int numSwitchFlipped = 0, numPuzzlesGenerated = 1;
    private View rootView;
    private GameMode gameMode;
    private GridView displayedLights;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.lights_out_layout, container, false);

        return rootView;
    }

    private ArrayList<Boolean> convertTo1D(boolean[][] grid) {
        ArrayList<Boolean> convertedList = new ArrayList<>();
        for (boolean[] row : grid) {
            for (boolean col : row) {
                convertedList.add(col);
            }
        }
        return convertedList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.generate_new_puzzle:
                game = new LightsOut(gameMode);
                game.randomize();
                originalGrid = LightsOut.makeCopyOf(game.getGrid());
                translatedList.clear();
                translatedList.addAll(convertTo1D(game.getGrid()));
                adapter.notifyDataSetChanged();
                numPuzzlesGenerated++;
                break;
            case R.id.reset_current_puzzle:
                game.setGrid(originalGrid);
                translatedList.clear();
                translatedList.addAll(convertTo1D(game.getGrid()));
                adapter.notifyDataSetChanged();
                break;
            default:
                Toast.makeText(getActivity(), "defaulted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getGameName() {
        return "Lights Out";
    }

    @Override
    public double getPercentScore() {
        return game.getPercentScore(numSwitchFlipped, numPuzzlesGenerated);
    }

    @Override
    public boolean isSolved() {
        return game.isSolved();
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void assignWidgetFunctions() {
        //wire widgets here
        game = new LightsOut(gameMode);
        game.randomize();
        originalGrid = LightsOut.makeCopyOf(game.getGrid());
        translatedList = new ArrayList<>(convertTo1D(game.getGrid()));
        displayedLights = getView().findViewById(R.id.displayed_lights_gridview);
        displayedLights.getViewTreeObserver().addOnPreDrawListener(this);
        displayedLights.setNumColumns(game.getGrid()[0].length);
        displayedLights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean[][] grid = game.getGrid();
                game.flipSwitch(position / grid[0].length, position % grid[0].length);
                translatedList.clear();
                translatedList.addAll(convertTo1D(grid));
                adapter.notifyDataSetChanged();
                numSwitchFlipped++;
                if (isSolved()) {
                    LightsOutGameFragment.this.notifyGameEnd();
                }
            }
        });
        Button generateNewPuzzle = rootView.findViewById(R.id.generate_new_puzzle);
        generateNewPuzzle.setOnClickListener(this);
        Button resetCurrentPuzzle = rootView.findViewById(R.id.reset_current_puzzle);
        resetCurrentPuzzle.setOnClickListener(this);
    }

    @Override
    public int getIconID() {
        return R.drawable.ic_lights_on_black_24dp;
    }

    @Override
    public String getDescription() {
        return "When the game starts, a grid is randomized and your job is to turn off all the lights. But when you press one light, all of the adjacent lights will toggle as well. A torch means that the light is on and a cross means that the light is off.";
    }

    @Override
    public boolean onPreDraw() {
        displayedLights.getViewTreeObserver().removeOnPreDrawListener(this);
        //resize GridView
        int initialHeight = displayedLights.getMeasuredHeight();
        int initialWidth = displayedLights.getMeasuredWidth();
        Log.d(TAG, "onPreDraw: initialHeight: " + initialHeight + " initialWidth: " + initialWidth);
        final int dimension = Math.min(initialHeight / game.getGrid().length, initialWidth / game.getGrid()[0].length);
        //set up adapter after gridview is resized
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return translatedList.size();
            }

            @Override
            public Object getItem(int i) {
                return translatedList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                AppCompatImageView imageView;
                if (convertView == null) {
                    imageView = new AppCompatImageView(getActivity());
                    imageView.setLayoutParams(new GridView.LayoutParams(dimension, dimension));
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(2, 2, 2, 2);
                } else {
                    imageView = (AppCompatImageView) convertView;
                }
                //put appropriate image into ImageView
                if (translatedList.get(position)) {
                    imageView.setImageResource(R.drawable.ic_lights_on_black_24dp);
                } else {
                    imageView.setImageResource(R.drawable.ic_lights_off_black_24dp);
                }
                return imageView;
            }
        };
        displayedLights.setAdapter(adapter);

        return true;
    }
}
