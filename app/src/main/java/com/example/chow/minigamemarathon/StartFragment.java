package com.example.chow.minigamemarathon;

import android.content.Context;
import android.content.SharedPreferences;
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

    private OnStartListener listener;
    private GameMode chosenGameMode;
    private String LAST_DIFFICULTY_KEY = "previous chosen difficulty";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.start_screen_layout, container, false);
        final SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        final int lastValue = preferences.getInt(LAST_DIFFICULTY_KEY, 0);
        final GameMode[] gameModes = GameMode.AVAILABLE_GAME_MODES;
        chosenGameMode = gameModes[lastValue];
        final NumberPicker gamemodeChooser = rootView.findViewById(R.id.gamemode_chooser);
        gamemodeChooser.setMinValue(0);
        gamemodeChooser.setMaxValue(gameModes.length-1);
        gamemodeChooser.setValue(lastValue);
        gamemodeChooser.setDisplayedValues(getNames(gameModes));
        gamemodeChooser.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                chosenGameMode = gameModes[newValue];
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(LAST_DIFFICULTY_KEY);
                editor.putInt(LAST_DIFFICULTY_KEY, newValue);
                editor.apply();
            }
        });
        final Button startButton = rootView.findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.GONE);
                gamemodeChooser.setVisibility(View.GONE);
                if (listener != null)
                {
                    listener.onStart(chosenGameMode);
                }
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
