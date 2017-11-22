package com.example.chow.minigamemarathon;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private ArrayList<Score> filteredDatabase, unfilteredDatabase;
    private GameMode gameMode;
    private View rootView;
    private RecyclerView recyclerView;
    private final String SCORE_TAG = "store scores in instance state";
    private int positionInAdapter;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.tab_high_score_layout, container, false);
        //either filters the scores or gets filtered scores from savedInstanceState
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> displayTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (savedInstanceState != null) //when fragment is recreated
                {
                    filteredDatabase = savedInstanceState.getParcelableArrayList(SCORE_TAG);
                    adapter = new ScoreAdapter(filteredDatabase);
                }

                else //when fragment is first created
                {
                    filteredDatabase = new ArrayList<>();
                    for (Score score : unfilteredDatabase) {
                        if (score.get_gameMode().equals(gameMode)) {
                            filteredDatabase.add(score);
                        }
                    }
                    adapter = new ScoreAdapter(filteredDatabase);
                }
                return  null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                displayInfo();
            }
        };
        displayTask.execute();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SCORE_TAG, filteredDatabase);
        if (adapter != null) {
            adapter.stopClock();
        }
    }

    public void startSortTask(Comparator<Score> comparator, boolean shouldColorCode) {
        Collections.sort(filteredDatabase, comparator);
        adapter.updateSortState(comparator, shouldColorCode);
    }

    private void displayInfo() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView = rootView.findViewById(R.id.high_score_recyclerview);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        TextView loadScreen = rootView.findViewById(R.id.load_screen);
        loadScreen.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        adapter.startClock();
    }

    public void setArguments(GameMode gameMode, ArrayList<Score> unfilteredDatabase, int positionInAdapter) {
        this.gameMode = gameMode;
        this.unfilteredDatabase = unfilteredDatabase;
        this.positionInAdapter = positionInAdapter;
    }

    //adapter for recycler view
    private class ScoreAdapter extends RecyclerView.Adapter<ScoreViewHolder> {
        private ArrayList<Score> scores;
        private ArrayList<Integer> ranks;
        private ArrayList<MemoryTextSwitcher> switcherArrayList;
        private int detailPosition;
        private UpdateClock clock;
        private int rankOffset;
        private boolean isColorCodeEnabled;
        private Comparator<Score> sortComparator;
        //rank colors
        private final int COLOR_HERE_I_AM = Color.BLACK,
                COLOR_GOLD = Color.rgb(201, 137, 16),
                COLOR_SILVER = Color.rgb(168, 168, 168),
                COLOR_BRONZE = Color.rgb(150, 90, 56);

        public ScoreAdapter(ArrayList<Score> scores) {
            super();
            this.scores = scores;
            ranks = new ArrayList<>();
            switcherArrayList = new ArrayList<>();
            detailPosition = 0;
            rankOffset = 0;
            clock = new UpdateClock(4000) { //updates textswitcher
                @Override
                public void onUpdate() {
                    Log.d(TAG, "onUpdate: clock updated " + gameMode.toString());
                    detailPosition = (detailPosition + 1) % 2;
                    for (int i = 0; i < switcherArrayList.size(); i++) {
                        switcherArrayList.get(i).displayTextAtPosition(detailPosition);
                    }
                }
            };
        }

        @Override
        public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View displayLayout = inflater.inflate(R.layout.high_score_status_dsiplay_layout, parent, false);
            return new ScoreViewHolder(displayLayout);
        }

        @Override
        public void onBindViewHolder(final ScoreViewHolder holder, final int position) {
            //reset
            holder.playerName.setTextColor(COLOR_HERE_I_AM);
            ((TextView) holder.scoreDetails.getCurrentView()).setTextColor(COLOR_HERE_I_AM);
            ((TextView) holder.scoreDetails.getNextView()).setTextColor(COLOR_HERE_I_AM);
            holder.playerRank.setTextColor(COLOR_HERE_I_AM);
            holder.playerRank.setText(""); //empty in case there is no sort
            //assign rank
            int rank = -1;
            if (ranks.size() == scores.size()) {
                rank = ranks.get(position);
                holder.playerRank.setText("#" + rank);
            }
            //assign colors
            if (isColorCodeEnabled) {
                switch (rank) {
                    case 1:
                        holder.playerName.setTextColor(COLOR_GOLD);
                        ((TextView) holder.scoreDetails.getCurrentView()).setTextColor(COLOR_GOLD);
                        ((TextView) holder.scoreDetails.getNextView()).setTextColor(COLOR_GOLD);
                        holder.playerRank.setTextColor(COLOR_GOLD);
                        break;
                    case 2:
                        holder.playerName.setTextColor(COLOR_SILVER);
                        ((TextView) holder.scoreDetails.getCurrentView()).setTextColor(COLOR_SILVER);
                        ((TextView) holder.scoreDetails.getNextView()).setTextColor(COLOR_SILVER);
                        holder.playerRank.setTextColor(COLOR_SILVER);
                        break;
                    case 3:
                        holder.playerName.setTextColor(COLOR_BRONZE);
                        ((TextView) holder.scoreDetails.getCurrentView()).setTextColor(COLOR_BRONZE);
                        ((TextView) holder.scoreDetails.getNextView()).setTextColor(COLOR_BRONZE);
                        holder.playerRank.setTextColor(COLOR_BRONZE);
                        break;
                }
            }
            //player name
            holder.playerName.setText(scores.get(position).get_name());
            holder.playerName.setTypeface(null, Typeface.BOLD);
            //Strings to loop back and forth
            holder.timeDetail = "Time: " + GameFragment.formatMillisToMMSSMSMS(Long.parseLong(scores.get(position).get_time()));
            holder.scoreDetail = "Score: " + scores.get(position).get_score();
            holder.scoreDetails.clearText();
            holder.scoreDetails.addText(holder.timeDetail);
            holder.scoreDetails.addText(holder.scoreDetail);
        }

        @Override
        public int getItemCount() {
            return scores.size();
        }

        @Override
        public void onViewAttachedToWindow(ScoreViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            addSwitcher(holder.scoreDetails);
        }

        @Override
        public void onViewDetachedFromWindow(ScoreViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            switcherArrayList.remove(holder.scoreDetails);
        }

        private void addSwitcher(MemoryTextSwitcher newSwitcher) {
            switcherArrayList.add(newSwitcher);
            newSwitcher.displayCurrentTextAtPosition(detailPosition);
        }

        private void stopClock() {
            clock.cancel();
            Log.d(TAG, "stopClock: clock canceled");
        }

        private void startClock() {
            clock.start();
            Log.d(TAG, "startClock: clock started");
        }

        public void updateSortState(Comparator<Score> sortComparator, boolean isColorCodeEnabled) {
            this.sortComparator = sortComparator;
            this.isColorCodeEnabled = isColorCodeEnabled;
            recalculateRanks();
            adapter.notifyDataSetChanged();
        }

        private void recalculateRanks() {
            rankOffset = 0;
            ranks.clear(); //clear ranks to recalculate
            if (sortComparator != null && isColorCodeEnabled) //accounts for duplicating scores
            {
                for (int i = 0; i < scores.size(); i++) {
                    if (i != 0 && sortComparator.compare(scores.get(i), scores.get(i - 1)) == 0) //duplicating scores
                    {
                        rankOffset++;
                    }
                    ranks.add(i - rankOffset + 1); //adds correct rank
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (adapter != null) {
            adapter.stopClock();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null) {
            adapter.stopClock();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startClock();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: tab fragment destroyed " + gameMode.toString());
    }

    private abstract class UpdateClock {
        private long interval;
        private CountDownTimer timer;

        private UpdateClock(long interval) {
            this.interval = interval;
        }

        private void initTimer() {
            if (timer != null) {
                timer.cancel();
            }
            timer = new CountDownTimer(Long.MAX_VALUE, interval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    onUpdate();
                }

                @Override
                public void onFinish() {
                    //nothing
                }
            };
        }

        public void start() {
            initTimer();
            timer.start();
        }

        public void cancel() {
            if (timer != null)
            {
                timer.cancel();
            }
        }

        public abstract void onUpdate();

    }

    //ViewHolder for layout in recycler view
    private static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView playerName, playerRank;
        MemoryTextSwitcher scoreDetails;
        String timeDetail, scoreDetail;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.game_name);
            scoreDetails = itemView.findViewById(R.id.score_details_switcher);
            playerRank = itemView.findViewById(R.id.player_rank);
        }
    }
}
