package com.example.chow.minigamemarathon;

import android.animation.LayoutTransition;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.IconCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kyros on 11/22/2017.
 */

public class PracticeNavigationFragment extends Fragment {

    private ArrayList<Integer> icons;
    private ImageButton buttonChange;
    private StopWatch clock;

    public PracticeNavigationFragment()
    {
        icons = new ArrayList<>();
        icons.add(R.drawable.ic_settings_black_24dp);
        icons.add(R.drawable.ic_build_black_24dp);
        icons.add(R.drawable.ic_create_black_24dp);
        clock = new StopWatch(1000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.practice_navigation_layout, container, false);
        //wire widgets
        buttonChange = rootView.findViewById(R.id.open_practice_dialog_button);
        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PracticeGameContainerFragment) getParentFragment()).showGameOptionsDialog();
            }
        });
        clock.setOnTickListener(new StopWatch.OnTickListener() {
            @Override
            public void onTick(long lapTimeElapsed, long totalTimeElapsed) {
                buttonChange.setImageResource(icons.get(0));
                buttonChange.setScaleType(ImageView.ScaleType.FIT_XY);
                icons.add(icons.remove(0));
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        clock.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        clock.resume();
    }
}
