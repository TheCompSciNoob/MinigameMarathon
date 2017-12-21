package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by per6 on 12/21/17.
 */

public class HowToPlayFragment extends Fragment {

    private RecyclerView gameInstructionList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.how_to_play_layout, container, false);
        //wire the recycler
        gameInstructionList = rootView.findViewById(R.id.how_to_play_recyclerview);


        return rootView;
    }
}
