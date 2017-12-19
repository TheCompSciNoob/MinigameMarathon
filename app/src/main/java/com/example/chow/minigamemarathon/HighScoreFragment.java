package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Kyros on 11/17/2017.
 */

public class HighScoreFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private AppBarLayout appBar;
    private View rootView;
    private View extraToolbar;
    private SectionsPagerAdapter adapter;
    private DatabaseHandler db;
    private ArrayList<Score> unfilteredScores;
    protected boolean isScoreDeletable = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.high_score_layout, container, false);
        extraToolbar = inflater.inflate(R.layout.high_score_tab_selection_layout, container, false);
        db = new DatabaseHandler(this.getContext());
        unfilteredScores = retrieveScoresFromDatabase();
        appBar = getActivity().findViewById(R.id.app_bar);
        ViewPager viewPager = rootView.findViewById(R.id.view_pager_high_score_container);
        TabLayout tabLayout = extraToolbar.findViewById(R.id.tab_selection_tablayout);
        final ImageButton imageButton = extraToolbar.findViewById(R.id.sort_button);
        //nested fragments for different gamemodes
        GameMode[] gameModes = GameMode.AVAILABLE_GAME_MODES;
        viewPager.setOffscreenPageLimit(gameModes.length - 1);
        adapter = new SectionsPagerAdapter(getChildFragmentManager(), gameModes);
        viewPager.setAdapter(adapter);
        //add layout to toolbar of activity
        tabLayout.setupWithViewPager(viewPager);
        imageButton.setImageResource(R.drawable.ic_sort_black_24dp);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), imageButton);
                popupMenu.getMenuInflater().inflate(R.menu.filter_scores, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(HighScoreFragment.this);
                popupMenu.show();
            }
        });
        return rootView;
    }

    public void deleteScore(Score deleteScore)
    {
        db.deleteScore(deleteScore);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        appBar.removeView(extraToolbar);
    }

    private ArrayList<Score> retrieveScoresFromDatabase() {
        return db.getAllScores();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Comparator<Score> scoreComparator = null;
        boolean shouldColorCode = false;
        switch (item.getItemId()) {
            case R.id.sort_a_to_z:
                shouldColorCode = false;
                scoreComparator = new Comparator<Score>() {
                    @Override
                    public int compare(Score score1, Score score2) {
                        return score1.getName().toLowerCase().compareTo(score2.getName().toLowerCase());
                    }
                };
                break;
            case R.id.sort_time:
                shouldColorCode = true;
                scoreComparator = new Comparator<Score>() {
                    @Override
                    public int compare(Score score1, Score score2) {
                        return (int) (Long.parseLong(score1.getTime()) - Long.parseLong(score2.getTime()));
                    }
                };
                break;
            case R.id.sort_score:
                shouldColorCode = true;
                scoreComparator = new Comparator<Score>() {
                    @Override
                    public int compare(Score score1, Score score2) {
                        return Integer.parseInt(score2.getScore()) - Integer.parseInt(score1.getScore());
                    }
                };
                break;
        }
        if (scoreComparator != null) {
            for (HighScoreTabFragment fragment : adapter.getChildFragments()) {
                fragment.startSortTask(scoreComparator, shouldColorCode);
            }
        }
        return true;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        private HighScoreTabFragment[] childFragments;
        private GameMode[] gameModes;

        public SectionsPagerAdapter(FragmentManager fm, GameMode[] gameModes) {
            super(fm);
            this.gameModes = gameModes;
            childFragments = new HighScoreTabFragment[gameModes.length];
            for (int i = 0; i < gameModes.length; i++)
            {
                HighScoreTabFragment tabFragment = new HighScoreTabFragment();
                tabFragment.setArguments(gameModes[i], unfilteredScores, isScoreDeletable);
                childFragments[i] = tabFragment;
            }
            appBar.addView(extraToolbar);
        }

        public HighScoreTabFragment[] getChildFragments() {
            return childFragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return gameModes[position].toString();
        }

        @Override
        public Fragment getItem(int position) {
            return childFragments[position];
        }

        @Override
        public int getCount() {
            return gameModes.length;
        }
    }
}
