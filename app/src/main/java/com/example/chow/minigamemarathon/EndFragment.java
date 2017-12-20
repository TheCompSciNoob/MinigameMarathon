package com.example.chow.minigamemarathon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by per6 on 11/1/17.
 */

public class EndFragment extends Fragment {

    private ListView gameDataSummary;
    private BaseAdapter adapter;
    private String[][] levelDataSets;
    private DatabaseHandler db;
    private GameMode gameMode;
    public static final String PREVIOUS_NAME_ENTERED_KEY = "previous name entered";
    private BackendlessHandler backendlessDb;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public EndFragment()
    {
        super();
        levelDataSets = new String[][] {};
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //preferences
        backendlessDb = new BackendlessHandler(this.getContext());
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();
        View rootView = inflater.inflate(R.layout.end_screen_layout, container, false);
        final Button saveRunButton = rootView.findViewById(R.id.save_result_button);
        saveRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(getActivity());
                View dialogView = inflater.inflate(R.layout.save_data_dialog, null);
                saveDialog.setView(dialogView);
                saveDialog.setCancelable(false);
                final AlertDialog alertDialog = saveDialog.create();
                final EditText playerName = dialogView.findViewById(R.id.player_name_input);
                final CheckBox saveOnline = dialogView.findViewById(R.id.checkbox_save_online);
                playerName.setText(preferences.getString(PREVIOUS_NAME_ENTERED_KEY, ""));
                Button saveButton = dialogView.findViewById(R.id.button_save_dialog);
                Button cancelButton = dialogView.findViewById(R.id.button_cancel_dialog);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!playerName.getText().toString().equals(""))
                        {
                            String playerNameInput = playerName.getText().toString();
                            editor.putString(PREVIOUS_NAME_ENTERED_KEY, playerNameInput);
                            editor.apply();
                            storeGameData(playerNameInput);
                            alertDialog.dismiss();
                            saveRunButton.setEnabled(false);
                        }
                        if(saveOnline.isChecked()){
                            String playerNameInput = playerName.getText().toString();
                            storeGameDataOnline(playerNameInput);
                        }
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        db = new DatabaseHandler(this.getContext());
        return rootView;
    }

    private void showResults() {
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return levelDataSets.length + 1;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LinearLayout displayLayout;
                if (convertView == null)
                {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    displayLayout = (LinearLayout) inflater.inflate(R.layout.game_end_status_display_layout, null);
                    TextView gameName = displayLayout.findViewById(R.id.game_name);
                    TextView gameTime = displayLayout.findViewById(R.id.game_time);
                    TextView gameScore = displayLayout.findViewById(R.id.game_score);
                    if (position < levelDataSets.length)
                    {
                        String[] levelData = levelDataSets[position];
                        gameName.setText(levelData[0]);
                        gameTime.append(GameFragment.formatMillisToMMSSMSMS(Long.parseLong(levelData[1])));
                        gameScore.append(levelData[2]);
                    }
                    else
                    {
                        gameName.setTypeface(null, Typeface.BOLD);
                        gameTime.setTypeface(null, Typeface.BOLD);
                        gameScore.setTypeface(null, Typeface.BOLD);
                        gameName.setTextColor(Color.BLACK);
                        gameTime.setTextColor(Color.BLACK);
                        gameScore.setTextColor(Color.BLACK);
                        gameName.setText(R.string.total);
                        gameTime.append(GameFragment.formatMillisToMMSSMSMS(getTotalTime()));
                        gameScore.append(getTotalScore() + "");
                    }
                }
                else
                {
                    displayLayout = (LinearLayout) convertView;
                }
                return displayLayout;
            }
        };
        gameDataSummary = getView().findViewById(R.id.end_screen_listview);
        gameDataSummary.setAdapter(adapter);
    }

    private void storeGameData(String playerName) {
        Score score = new Score(playerName, getTotalScore() + "", getTotalTime() + "", gameMode.name());
        db.addScore(score);
    }

    private void storeGameDataOnline(String playerName){
        Score score = new Score(playerName, getTotalScore() + "", getTotalTime() + "", gameMode.name());
        backendlessDb.saveScore(score);
    }

    public void setArguments(String[][] levelDataSets, GameMode gameMode)
    {
        this.levelDataSets = levelDataSets;
        this.gameMode = gameMode;
        showResults();
    }

    private int getTotalScore()
    {
        int sum = 0;
        for (String[] s : levelDataSets)
        {
            sum += Integer.parseInt(s[2]);
        }
        return sum;
    }

    private long getTotalTime()
    {
        long sum = 0;
        for (String[] s : levelDataSets)
        {
            sum += Long.parseLong(s[1]);
        }
        return sum;
    }
}