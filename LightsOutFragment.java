package com.example.kyros.gridviewtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Kyros on 10/22/2017.
 */

public class LightsOutFragment extends Fragment {

    private View rootView;
    private GridView displayedLights;
    GridViewImageAdapter adapter;
    ArrayList<ImageView> translatedList;
    private LightsOut game;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.lights_out_layout, container, false);
        game = new LightsOut(5, 5);
        translatedList = new ArrayList<>(convertTo1D(game.getGrid()));
        adapter = new GridViewImageAdapter(getActivity(), translatedList);
        displayedLights = rootView.findViewById(R.id.lights_out_grid);
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
                Toast.makeText(getActivity(), "item clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private ArrayList<ImageView> convertTo1D(boolean[][] grid)
    {
        ArrayList<ImageView> convertedList = new ArrayList<>();
        for (boolean[] row : grid)
        {
            for (boolean col : row)
            {
                ImageView imageView = new ImageView(getActivity());
                imageView.setFocusable(false);
                if (col)
                {
                    imageView.setImageResource(R.drawable.ic_add_circle_black_24dp);
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
                }
                convertedList.add(imageView);
            }
        }
        return convertedList;
    }
}
