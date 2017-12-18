package com.example.chow.minigamemarathon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by per6 on 12/18/17.
 */

public class PracticeNavigationFragment extends Fragment {

    private static final String STORE_GAME = "store game key", STORE_GAMEMODE = "store gamemode key";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View optionsLayout = inflater.inflate(R.layout.practice_options_layout, container, false);
        //find radio buttons and set listeners
        final RadioGroup gameGroup = optionsLayout.findViewById(R.id.practice_choose_game);
        final RadioGroup gameModeGroup = optionsLayout.findViewById(R.id.practice_choose_gamemode);
        final GameMode[] gameModes = GameMode.AVAILABLE_GAME_MODES;
        final GameFragment[] allGames = GameContainerFragment.getAllGames();
        for (int i = 0; i < allGames.length; i++) {
            RadioButton chooseGame = new AppCompatRadioButton(getActivity());
            chooseGame.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            chooseGame.setText(allGames[i].getGameName());
            chooseGame.setId(i);
            gameGroup.addView(chooseGame);
        }
        for (int i = 0; i < gameModes.length; i++) {
            RadioButton chooseGameMode = new AppCompatRadioButton(getActivity());
            chooseGameMode.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            chooseGameMode.setText(gameModes[i].toString());
            chooseGameMode.setId(i);
            gameModeGroup.addView(chooseGameMode);
        }
        final SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int gameIndex = preferences.getInt(STORE_GAME, 0);
        int gamemodeIndex = preferences.getInt(STORE_GAMEMODE, 0);
        gameGroup.check(gameIndex);
        gameModeGroup.check(gamemodeIndex);
        Button confirmButton = optionsLayout.findViewById(R.id.confirm_practice_option_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int chosenGameID = gameGroup.getCheckedRadioButtonId(), chosenGameModeID = gameModeGroup.getCheckedRadioButtonId();
                if (chosenGameID != -1 && chosenGameModeID != -1)
                {
                    final GameFragment chosenGame = allGames[chosenGameID];
                    final GameMode chosenGameMode = gameModes[chosenGameModeID];
                    chosenGame.setGameStateUpdateListener(new GameFragment.OnGameStateUpdateListener() {
                        @Override
                        public void onGameSolved(GameFragment solvedFragment) {
                            //TODO: launch a new instance of PracticeNavigationFragment
                            FragmentManager fm = chosenGame.getFragmentManager();
                            fm.beginTransaction()
                                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                                    .replace(R.id.display_frame, new PracticeNavigationFragment())
                                    .commit();
                        }

                        @Override
                        public void onGameStart(GameFragment startingFragment) {
                            chosenGame.finalizeArguments(chosenGameMode);
                            chosenGame.assignWidgetFunctions();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt(STORE_GAME, chosenGameID);
                            editor.putInt(STORE_GAMEMODE, chosenGameModeID);
                            editor.apply();
                        }

                        @Override
                        public void onGamePaused(GameFragment pausedFragment) {

                        }

                        @Override
                        public void onGameResume(GameFragment resumeFragment) {

                        }
                    });
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .replace(R.id.display_frame, chosenGame)
                            .commit();
                }
            }
        });

        return optionsLayout;
    }
}
