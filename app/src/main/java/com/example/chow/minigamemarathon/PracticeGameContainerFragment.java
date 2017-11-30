package com.example.chow.minigamemarathon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Kyros on 11/22/2017.
 */

public class PracticeGameContainerFragment extends Fragment implements StopWatch.OnTickListener {

    private StopWatch clock;
    private MenuItem menuItemChange;
    private ArrayList<Integer> icons;
    private static final String STORE_GAME = "store game key", STORE_GAMEMODE = "store gamemode key";

    public PracticeGameContainerFragment()
    {
        icons = new ArrayList<>();
        icons.add(R.drawable.ic_settings_white_24dp);
        icons.add(R.drawable.ic_build_white_24dp);
        icons.add(R.drawable.ic_create_white_24dp);
        clock = new StopWatch(1000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.practice_container_layout, container, false);
        setHasOptionsMenu(true);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.practice_container, new PracticeNavigationFragment())
                .commit();
        clock.setOnTickListener(this);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.practice_menu_options, menu);
        menuItemChange = menu.getItem(0);
        clock.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.practice_game_options:
                showGameOptionsDialog();
                break;
        }
        return true;
    }

    public void showGameOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout optionsLayout = (LinearLayout) inflater.inflate(R.layout.practice_options_dialog_layout, null);
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
        builder.setView(optionsLayout);
        final AlertDialog dialog = builder.create();
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
                            showGameOptionsDialog();
                        }

                        @Override
                        public void onGameStart(GameFragment startingFragment) {
                            chosenGame.finalizeArguments(chosenGameMode);
                            chosenGame.assignWidgetFunctions();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt(STORE_GAME, chosenGameID);
                            editor.putInt(STORE_GAMEMODE, chosenGameModeID);
                            editor.apply();
                            Log.d(TAG, "onGameStart: PracticeGameContainerFragment replaced fragment");
                        }

                        @Override
                        public void onGamePaused(GameFragment pausedFragment) {

                        }

                        @Override
                        public void onGameResume(GameFragment resumeFragment) {

                        }
                    });
                    FragmentManager fm = getChildFragmentManager();
                    fm.beginTransaction().replace(R.id.practice_container, chosenGame).commit();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        clock.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        clock.pause();
    }

    @Override
    public void onTick(long lapTimeElapsed, long totalTimeElapsed) {
        menuItemChange.setIcon(icons.get(0));
        icons.add(icons.remove(0));
    }
}
