package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Kyros on 10/16/2017.
 */

public class LightsOutFragment extends GameFragment {

    private boolean[][] lights;
    private ArrayList<CheckBox> translatedLights;
    ArrayAdapter<CheckBox> adapter;
    GridView lightsGridView;

    public LightsOutFragment() {
        super(R.layout.lights_out_layout);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        lightsGridView = rootView.findViewById(R.id.lights_display);
        //change the root element
        adapter = new ArrayAdapter<CheckBox>(getActivity(), android.R.layout.simple_list_item_1, getTranslatedLights());
        lightsGridView.setAdapter(adapter);
        lightsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flipSwitch(position);
                if (isSolved())
                {
                    finish();
                }
            }
        });
        start();
        return rootView;
    }

    public void setDimensions(int numRows, int numCols)
    {
        lights = new boolean[numRows][numCols];
        updateTranslatedLights();
    }

    private void updateTranslatedLights() {
        translatedLights = new ArrayList<>();
        for (boolean[] row : lights)
        {
            for (boolean col : row)
            {
                CheckBox light = new CheckBox(null);
                light.setChecked(col);
                translatedLights.add(light);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public ArrayList<CheckBox> getTranslatedLights()
    {
        updateTranslatedLights();
        return translatedLights;
    }

    public void flipSwitch(int indexInArray)
    {
        int row = indexInArray / lights.length;
        int col= indexInArray % lights[0].length;
        lights[row][col] = !lights[row][col];
        try{
            lights[row+1][col] = !lights[row+1][col+1];
        } catch (Exception e){}
        try{
            lights[row][col+1] = !lights[row][col+1];
        } catch (Exception e){}
        try{
            lights[row-1][col] = !lights[row-1][col];
        } catch (Exception e){}
        try{
            lights[row][col-1] = !!lights[row][col-1];
        } catch (Exception e){}
        updateTranslatedLights();
    }

    @Override
    public boolean isGame() {
        return true;
    }

    @Override
    public void initializeVariables() {

    }

    @Override
    public boolean isSolved() {
        boolean solved = true;
        for (boolean[] row : lights)
        {
            for (boolean col : row)
            {
                if (col)
                {
                    solved = false;
                }
            }
        }
        return solved;
    }
}
