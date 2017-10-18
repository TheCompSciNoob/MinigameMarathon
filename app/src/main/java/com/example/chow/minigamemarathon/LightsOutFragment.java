package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by per6 on 10/18/17.
 */

public class LightsOutFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.complete_layout_basic, container, false);
        //test to change if TextView works
        TextView text = rootView.findViewById(R.id.current_game_view);
        text.setText("Changed");

        return rootView;
    }
}
