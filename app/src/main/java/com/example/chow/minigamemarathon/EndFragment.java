package com.example.chow.minigamemarathon;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by per6 on 11/1/17.
 */

public class EndFragment extends Fragment {

    public static final String DIVIDER = "<divide>", STORE_TAG = "store in shared preferences";
    private ListView gameDataSummary;
    private BaseAdapter adapter;
    private ArrayList<String> levelDataSets;

    public EndFragment()
    {
        super();
        levelDataSets = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.end_screen_layout, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        storeGameData();
        showResults();
    }

    private void showResults() {
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return levelDataSets.size() + 2;
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
                    if (position == 0)
                    {
                        gameName.setText("Game");
                        gameName.setTypeface(null, Typeface.BOLD);
                        gameTime.setText("Time");
                        gameTime.setTypeface(null, Typeface.BOLD);
                        gameScore.setText("Score");
                        gameScore.setTypeface(null, Typeface.BOLD);
                    }
                    else if (position == levelDataSets.size() + 1)
                    {
                        gameName.setText("Total");
                        gameName.setTypeface(null, Typeface.BOLD);
                        gameTime.setText("" + GameFragment.formatMillisToMMSSMSMS(getTotalTime()));
                        gameTime.setTypeface(null, Typeface.BOLD);
                        gameScore.setText("" + getTotalScore());
                        gameScore.setTypeface(null, Typeface.BOLD);
                    }
                    else
                    {
                        String[] levelData = levelDataSets.get(position - 1).split(GameFragment.SPLIT);
                        gameName.setText(levelData[0]);
                        gameTime.setText(GameFragment.formatMillisToMMSSMSMS(Long.parseLong(levelData[1])));
                        gameScore.setText(levelData[2]);
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

    private void storeGameData() {
        String result = "";
        for (String s : levelDataSets)
        {
            result += DIVIDER + s;
        }
        result = result.substring(DIVIDER.length());
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor gameDataEditor = preferences.edit();
        Set<String> set = preferences.getStringSet(STORE_TAG, null);
        if (set == null) //when no data is stored before
        {
            HashSet<String> results = new HashSet<>();
            results.add(result);
            gameDataEditor.putStringSet(STORE_TAG, results);
            gameDataEditor.apply();
        }
        else
        {
            set.add(result);
            gameDataEditor.remove(STORE_TAG);
            gameDataEditor.putStringSet(STORE_TAG, set);
            gameDataEditor.apply();
        }
    }

    public void setDataSet(ArrayList<String> levelDataSets)
    {
        this.levelDataSets = levelDataSets;
    }

    private int getTotalScore()
    {
        int sum = 0;
        for (String s : levelDataSets)
        {
            sum += Integer.parseInt(s.split(GameFragment.SPLIT)[2]);
        }
        return sum;
    }

    private long getTotalTime()
    {
        long sum = 0;
        for (String s : levelDataSets)
        {
            sum += Long.parseLong(s.split(GameFragment.SPLIT)[1]);
        }
        return sum;
    }
}