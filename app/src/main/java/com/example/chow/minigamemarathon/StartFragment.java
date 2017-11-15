package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

/**
 * Created by per6 on 10/30/17.
 */

public class StartFragment extends Fragment {

    private Button startButton;
    private OnStartListener listener;
    private NumberPicker gamemodeChooser;
    private GameMode choosenGameMode;

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
                    listener.onStart(choosenGameMode);
                }
            }
        });
        final GameMode[] gameModes = {GameMode.EASY, GameMode.HARD, GameMode.DEBUG};
        gamemodeChooser = rootView.findViewById(R.id.gamemode_chooser);
        gamemodeChooser.setMinValue(0);
        gamemodeChooser.setMaxValue(gameModes.length-1);
        gamemodeChooser.setDisplayedValues(getNames(gameModes));
        gamemodeChooser.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                choosenGameMode = gameModes[newValue];
            }
        });

        return rootView;
    }

    public void setOnStartListener(OnStartListener listener)
    {
        this.listener = listener;
    }

    private String[] getNames(GameMode[] gameModes)
    {
        String[] names = new String[gameModes.length];
        for (int i = 0; i < names.length; i++)
        {
            names[i] = gameModes[i].toString();
        }
        return names;
    }

    public interface OnStartListener
    {
        public void onStart(GameMode gameMode);
    }
}
