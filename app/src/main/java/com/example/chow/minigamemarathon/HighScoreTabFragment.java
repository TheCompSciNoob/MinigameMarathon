package com.example.chow.minigamemarathon;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Kyros on 11/17/2017.
 */

public class HighScoreTabFragment extends Fragment {

    private int positionInAdapter = -1, positionSelected = -1;
    private ListView displayScoresListView;
    private TextView loadingText;
    private GameMode gameMode;
    private BaseAdapter adapter;
    private ArrayList<Score> database;
    private ArrayList<Score> scores;
    private boolean suitableDisplayFilterMoment = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab_high_score_layout, container, false);
        displayScoresListView = rootView.findViewById(R.id.high_score_viewer);
        loadingText = rootView.findViewById(R.id.loading_text);
        ViewPager parentContainer = getActivity().findViewById(R.id.view_pager_high_score_container);
        parentContainer.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                positionSelected = position;
                if (positionSelected != positionInAdapter)
                {
                    displayScoresListView.setAdapter(null);
                    loadingText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (positionSelected == positionInAdapter && state == ViewPager.SCROLL_STATE_IDLE)
                {
                    loadingText.setVisibility(View.INVISIBLE);
                    displayScoresListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        startFilterTask();
        //check if nav drawer is closed
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                displayScores();
            }
        });

        return rootView;
    }

    private void startFilterTask()
    {
        //new thread to obtain data from database
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                scores = new ArrayList<>();
                scores = filterScoresFromDataBase();
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

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                displayScores();
            }
        };
        task.execute();
    }

    private void displayScores()
    {
        if (!suitableDisplayFilterMoment)
        {
            suitableDisplayFilterMoment = true;
        }
        else if (positionInAdapter == 0)
        {
            loadingText.setVisibility(View.INVISIBLE);
            displayScoresListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
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
                }
            }
        }
        return filteredScores;
    }

    public void setArguments(GameMode gameMode, ArrayList<Score> database, int positionInAdapter)
    {
        this.gameMode = gameMode;
        this.database = database;
        this.positionInAdapter = positionInAdapter;
    }

    private static class ViewHolder
    {
        TextView playerName;
        TextView gameTime;
        TextView gameScore;
    }
}
