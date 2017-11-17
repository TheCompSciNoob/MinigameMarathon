package com.example.chow.minigamemarathon;

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

/**
 * Created by per6 on 11/1/17.
 */

public class EndFragment extends Fragment {

    public static final String DIVIDER = "<divide>", STORE_TAG = "store in shared preferences";
    private ListView gameDataSummary;
    private BaseAdapter adapter;
    private String[][] levelDataSets;

    public EndFragment()
    {
        super();
        levelDataSets = new String[][] {};
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
                return levelDataSets.length;
            }

            @Override
            public Object getItem(int position) {
                return levelDataSets[position];
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
                        String[] levelData = levelDataSets[position];
                        gameName.setText(levelData[0]);
                        gameTime.append(GameFragment.formatMillisToMMSSMSMS(Long.parseLong(levelData[1])));
                        gameScore.append(levelData[2]);
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

    }

    public void setDataSet(String[][] levelDataSets)
    {
        this.levelDataSets = levelDataSets;
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