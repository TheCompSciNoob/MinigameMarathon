package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by Kyros on 11/17/2017.
 */

public class HighScoreTabFragment extends Fragment {

    private GameMode gameMode;
    private BaseAdapter adapter;
    private ArrayList<Score> database;
    private ArrayList<Score> scores;
    private static final String SCORE_KEY = "saved scores from savedInstanceState";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab_high_score_layout, container, false);
        if (savedInstanceState != null)
        {
            scores = savedInstanceState.getParcelableArrayList(SCORE_KEY);
        }
        else
        {
            scores = filterScoresFromDataBase(); //TODO: get scores from scores in the method
        }
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return scores.size();
            }

            @Override
            public Object getItem(int position) {
                return scores.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LinearLayout displayLayout;
                ViewHolder viewHolder;
                if (convertView == null)
                {
                    viewHolder = new ViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    displayLayout = (LinearLayout) inflater.inflate(R.layout.game_end_status_display_layout, null);
                    displayLayout.setPadding(8, 8, 8, 8);
                    viewHolder.playerName = displayLayout.findViewById(R.id.game_name);
                    viewHolder.gameTime = displayLayout.findViewById(R.id.game_time);
                    viewHolder.gameScore = displayLayout.findViewById(R.id.game_score);
                    viewHolder.playerName.setText((position + 1) + ". " + scores.get(position).get_name());
                    viewHolder.gameTime.append(GameFragment.formatMillisToMMSSMSMS(Long.parseLong(scores.get(position).get_time())));
                    viewHolder.gameScore.append(scores.get(position).get_score());
                    displayLayout.setTag(viewHolder);
                }
                else
                {
                    displayLayout = (LinearLayout) convertView;
                }
                return displayLayout;
            }

            @Override
            public int getViewTypeCount() {
                return Math.max(getCount(), 1);
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };
        ListView displayScoresListView = rootView.findViewById(R.id.high_score_viewer);
        displayScoresListView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SCORE_KEY, scores);
    }

    private ArrayList<Score> filterScoresFromDataBase() {
        ArrayList<Score> filteredScores = new ArrayList<>();
        if (database != null)
        {
            for (Score score : database)
            {
                if (score.get_gameMode().equals(gameMode))
                {
                    filteredScores.add(score);
                    Log.d(TAG, "filterScoresFromDataBase: score added");
                }
            }
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
        return filteredScores;
    }

    public void setArguments(GameMode gameMode, ArrayList<Score> database)
    {
        //TODO: sort out data that is only of this gamemode
        this.gameMode = gameMode;
        this.database = database;
    }

    private static class ViewHolder
    {
        TextView playerName;
        TextView gameTime;
        TextView gameScore;
    }
}
