package com.example.chow.minigamemarathon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by per6 on 12/18/17.
 */

public class PracticeNavigationFragment extends Fragment {

    private static final String STORE_GAME = "store game key", STORE_GAMEMODE = "store gamemode key";
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View optionsLayout = inflater.inflate(R.layout.practice_options_layout, container, false);
        //shared preferences
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //find radio buttons and set listeners
        final GameFragment[] allGames = GameContainerFragment.getAllGames();
        Spinner gameSpinner = optionsLayout.findViewById(R.id.practice_choose_game_spinner);
        ArrayAdapter<GameFragment> gameAdapter = new ArrayAdapter<GameFragment>(getContext(), R.layout.app_theme_spinner_style, allGames);
        gameAdapter.setDropDownViewResource(R.layout.app_theme_dropdown_spinner_item);
        gameSpinner.setAdapter(gameAdapter);
        gameSpinner.setSelection(sharedPreferences.getInt(STORE_GAME, 0));
        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt(STORE_GAME, position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });
        final GameMode[] gameModes = GameMode.AVAILABLE_GAME_MODES;
        Spinner gamemodeSpinner = optionsLayout.findViewById(R.id.practice_choose_gamemode_spinner);
        ArrayAdapter<GameMode> gamemodeAdapter = new ArrayAdapter<GameMode>(getContext(), R.layout.app_theme_spinner_style, gameModes);
        gamemodeAdapter.setDropDownViewResource(R.layout.app_theme_dropdown_spinner_item);
        gamemodeSpinner.setAdapter(gamemodeAdapter);
        gamemodeSpinner.setSelection(sharedPreferences.getInt(STORE_GAMEMODE, 0));
        gamemodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt(STORE_GAMEMODE, position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //nothing
            }
        });
        //confirm button
        Button confirmButton = optionsLayout.findViewById(R.id.confirm_practice_option_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final GameFragment chosenGameFragment = allGames[sharedPreferences.getInt(STORE_GAME, 0)];
                final GameMode chosenGameMode = gameModes[sharedPreferences.getInt(STORE_GAMEMODE, 0)];
                chosenGameFragment.setGameStateUpdateListener(new GameFragment.OnGameStateUpdateListener() {
                    @Override
                    public void onGameSolved(GameFragment solvedFragment) {
                        FragmentManager fm = chosenGameFragment.getFragmentManager();
                        fm.beginTransaction()
                                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                                .replace(R.id.display_frame, new PracticeNavigationFragment())
                                .commit();
                    }

                    @Override
                    public void onGameStart(GameFragment startingFragment) {
                        chosenGameFragment.finalizeArguments(chosenGameMode);
                        chosenGameFragment.assignWidgetFunctions();
                    }

                    @Override
                    public void onGamePaused(GameFragment pausedFragment) {
                        //nothing
                    }

                    @Override
                    public void onGameResume(GameFragment resumeFragment) {
                        //nothing
                    }
                });
                //start the GameFragment
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.display_frame, chosenGameFragment)
                        .commit();
            }
        });

        return optionsLayout;
    }
}
