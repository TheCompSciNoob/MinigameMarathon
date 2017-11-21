package com.example.chow.minigamemarathon;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.ContentValues.TAG;

/**
 * Created by Kyros on 11/20/2017.
 */

public class HighScoreTabFragment extends Fragment {

    private ScoreAdapter adapter;
    private DrawerLayout.SimpleDrawerListener drawerListener;
    private ArrayList<Score> filteredDatabase;
    private GameMode gameMode;
    private boolean suitableDisplayFilterMoment = false;
    private View rootView;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.tab_high_score_layout, container, false);
        drawerListener = new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                tryDisplayInfo();
                Log.d(TAG, "onDrawerClosed: drawer closed and info displayed");
            }
        };
        drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(drawerListener);

        return rootView;
    }

    public void startSortTask(final Comparator<Score> comparator)
    {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> sortTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Collections.sort(filteredDatabase, comparator);
                adapter.notifyDataSetChanged();
                return null;
            }
        };
    }

    private void startFilterTask(final ArrayList<Score> unfilteredDatabase)
    {
        @SuppressLint("StaticFieldLeak") final AsyncTask<Void, Void, Void> filterTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                filteredDatabase = new ArrayList<>();
                for (Score score : unfilteredDatabase)
                {
                    if (score.get_gameMode().equals(gameMode))
                    {
                        filteredDatabase.add(score);
                    }
                }
                adapter = new ScoreAdapter(filteredDatabase);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                tryDisplayInfo();
            }
        };
        filterTask.execute();
    }

    private void tryDisplayInfo()
    {
        if (!suitableDisplayFilterMoment)
        {
            suitableDisplayFilterMoment = true;
        }
        else if (recyclerView == null)
        {
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
            recyclerView = rootView.findViewById(R.id.high_score_recyclerview);
            recyclerView.setLayoutManager(manager);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            TextView loadScreen = rootView.findViewById(R.id.load_screen);
            loadScreen.setVisibility(View.INVISIBLE);
            recyclerView.setAdapter(adapter);
            adapter.startClock();
        }
    }

    public void setArguments(GameMode gameMode, ArrayList<Score> unfilteredDatabase, int positionInAdapter)
    {
        this.gameMode = gameMode;
        startFilterTask(unfilteredDatabase);
    }

    //adapter for recycler view
    private class ScoreAdapter extends RecyclerView.Adapter<ScoreViewHolder>
    {
        private ArrayList<Score> scores;
        private ArrayList<TextSwitcher> switcherArrayList;
        private ArrayList<String> values1, values2;
        private int detailPosition;
        private UpdateClock clock;

        public ScoreAdapter(ArrayList<Score> scores) {
            super();
            this.scores = scores;
            switcherArrayList = new ArrayList<>();
            values1 = new ArrayList<>();
            values2 = new ArrayList<>();
            detailPosition = 0;
        }

        @Override
        public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            LinearLayout displayLayout = (LinearLayout) inflater.inflate(R.layout.high_score_status_dsiplay_layout, parent, false);
            ScoreViewHolder vh = new ScoreViewHolder(displayLayout);
            return vh;
        }

        @Override
        public void onBindViewHolder(final ScoreViewHolder holder, final int position) {
            holder.playerName.setText((position + 1) + ". " + scores.get(position).get_name());
            holder.playerName.setTextColor(Color.BLACK);
            Animation in = new AlphaAnimation(0.f, 1.f);
            in.setDuration(1000);
            Animation out = new AlphaAnimation(1.f, 0.f);
            out.setDuration(1000);
            holder.scoreDetails.setInAnimation(in);
            holder.scoreDetails.setOutAnimation(out);/*
            holder.scoreDetails.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    TextView detail = new TextView(holder.scoreDetails.getContext());
                    detail.setTextSize(24);
                    return detail;
                }
            });*/
            //Strings to loop back and forth
            holder.timeDetail = "Time: " + GameFragment.formatMillisToMMSSMSMS(Long.parseLong(scores.get(position).get_time()));
            holder.scoreDetail = "Score: " + scores.get(position).get_score();
            addSwitcher(holder.scoreDetails, holder.timeDetail, holder.scoreDetail);
        }

        @Override
        public int getItemCount() {
            return scores.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            clock = new UpdateClock(4000) {
                @Override
                public void onUpdate() {
                    Log.d(TAG, "onUpdate: clock updated");
                    detailPosition = (detailPosition + 1) % 2;
                    for (int i = 0; i < switcherArrayList.size(); i++)
                    {
                        if (detailPosition == 0)
                        {
                            switcherArrayList.get(i).setText(values1.get(i));
                        }
                        else
                        {
                            switcherArrayList.get(i).setText(values2.get(i));
                        }
                    }
                }
            };
        }

        @Override
        public void onViewDetachedFromWindow(ScoreViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            switcherArrayList.remove(holder.scoreDetails);
            values1.remove(holder.timeDetail);
            values2.remove(holder.scoreDetail);
        }

        private void addSwitcher(TextSwitcher newSwitcher, String newTimeDetail, String newScoreDetail)
        {
            switcherArrayList.add(newSwitcher);
            values1.add(newTimeDetail);
            values2.add(newScoreDetail);
            if (detailPosition == 0)
            {
                newSwitcher.setText(newTimeDetail);
            }
            else
            {
                newSwitcher.setText(newScoreDetail);
            }
        }

        private void stopClock()
        {
            clock.cancel();
        }

        private void startClock()
        {
            clock.start();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        drawerLayout.removeDrawerListener(drawerListener);
        if (adapter != null)
        {
            adapter.stopClock();
        }
    }

    private abstract class UpdateClock extends CountDownTimer
    {

        private UpdateClock(long interval)
        {
            super(Long.MAX_VALUE, interval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            onUpdate();
        }

        @Override
        public void onFinish() {
            //nothing
        }

        public abstract void onUpdate();

    }

    //ViewHolder for layout in recycler view
    private static class ScoreViewHolder extends RecyclerView.ViewHolder
    {
        TextView playerName;
        TextSwitcher scoreDetails;
        String timeDetail, scoreDetail;

        public ScoreViewHolder(LinearLayout itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.game_name);
            scoreDetails = itemView.findViewById(R.id.score_details_switcher);
        }
    }
}
