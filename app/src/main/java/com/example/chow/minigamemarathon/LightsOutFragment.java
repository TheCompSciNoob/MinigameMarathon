package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Kyros on 10/22/2017.
 */

public class LightsOutFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private GridView displayedLights;
    private GridViewImageAdapter adapter;
    ArrayList<Boolean> translatedList;
    private LightsOut game;
    private boolean[][] originalGrid;
    private final int HEIGHT = 5, WIDTH = 5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.lights_out_layout, container, false);
        game = new LightsOut(HEIGHT, WIDTH);
        game.randomize();
        originalGrid = Arrays.copyOf(game.getGrid(), game.getGrid().length);
        translatedList = new ArrayList<>(convertTo1D(game.getGrid()));
        adapter = new GridViewImageAdapter(getActivity(), translatedList);
        displayedLights = rootView.findViewById(R.id.displayed_lights_gridview);
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
            }
        });
        Button generateNewPuzzle = rootView.findViewById(R.id.generate_new_puzzle);
        generateNewPuzzle.setOnClickListener(this);
        Button resetCurrentPuzzle = rootView.findViewById(R.id.reset_current_puzzle);
        resetCurrentPuzzle.setOnClickListener(this);

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
                game = new LightsOut(HEIGHT, WIDTH);
                game.randomize();
                originalGrid = Arrays.copyOf(game.getGrid(), game.getGrid().length);
                translatedList.clear();
                translatedList.addAll(convertTo1D(game.getGrid()));
                adapter.notifyDataSetChanged();
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
}
