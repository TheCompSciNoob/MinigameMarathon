package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by per6 on 10/30/17.
 */

public class StartFragment extends Fragment {

    private Button startButton;
    private GameStateUpdateListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.start_screen_layout, container, false);
        startButton = rootView.findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                {
                    listener.onGameStateUpdate();
                }
            }
        });
        return rootView;
    }

    public void setOnStartListener(GameStateUpdateListener listener)
    {
        this.listener = listener;
    }
}
