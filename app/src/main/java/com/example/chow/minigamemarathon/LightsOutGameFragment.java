package com.example.chow.minigamemarathon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Kyros on 10/22/2017.
 */

public class LightsOutGameFragment extends GameFragment implements View.OnClickListener{

    private BaseAdapter adapter;
    ArrayList<Boolean> translatedList;
    private LightsOut game;
    private boolean[][] originalGrid;
    private int numSwitchFlipped = 0, numPuzzlesGenerated = 1;
    private View rootView;
    private GameMode gameMode;
    private Context context;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.lights_out_layout, container, false);

        return rootView;
    }

    private ArrayList<Boolean> convertTo1D(boolean[][] grid)
    {
        ArrayList<Boolean> convertedList = new ArrayList<>();
        for (boolean[] row : grid)
        {
            for (boolean col : row)
            {
                convertedList.add(col);
            }
        }
        return convertedList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
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
        return LightsOut.getPercentScore(numSwitchFlipped, numPuzzlesGenerated, gameMode);
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
    public void initializeVariables() {
        //wire widgets here
        if (gameMode == null)
        {
            Log.d(TAG, "set_gameMode: gamemode is null");
        }
        game = new LightsOut(gameMode);
        game.randomize();
        originalGrid = LightsOut.makeCopyOf(game.getGrid());
        translatedList = new ArrayList<>(convertTo1D(game.getGrid()));
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
                ImageView imageView;
                if (convertView == null)
                {
                    imageView = new SquareImageView(getActivity());
                    imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(5, 5, 5, 5);
                }
                else
                {
                    imageView = (ImageView) convertView;
                }
                //put appropriate image into ImageView
                if (translatedList.get(position))
                {
                    imageView.setImageResource(R.drawable.ic_lights_on_black_24dp);
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_lights_off_black_24dp);
                }
                return imageView;
            }
        };
        GridView displayedLights = new SquareGridView(context);
        displayedLights.setNumColumns(game.getGrid()[0].length);
        displayedLights.setAdapter(adapter);
        displayedLights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean[][] grid = game.getGrid();
                game.flipSwitch(position / grid[0].length , position % grid[0].length);
                translatedList.clear();
                translatedList.addAll(convertTo1D(grid));
                adapter.notifyDataSetChanged();
                numSwitchFlipped++;
                if (isSolved())
                {
                    LightsOutGameFragment.this.notifyGameEnd();
                }
            }
        });
        LinearLayout gridViewContainer = rootView.findViewById(R.id.displayed_lights_gridview_container);
        gridViewContainer.addView(displayedLights, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Button generateNewPuzzle = rootView.findViewById(R.id.generate_new_puzzle);
        generateNewPuzzle.setOnClickListener(this);
        Button resetCurrentPuzzle = rootView.findViewById(R.id.reset_current_puzzle);
        resetCurrentPuzzle.setOnClickListener(this);
    }

    private class SquareImageView extends android.support.v7.widget.AppCompatImageView
    {
        public SquareImageView(Context context)
        {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int dimension = Math.max(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(dimension, dimension);
            setMeasuredDimension(dimension, dimension);
        }
    }

    private class SquareGridView extends GridView {
        public SquareGridView(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int dimension = Math.min(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(dimension, dimension);
            setMeasuredDimension(dimension, dimension);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
