package com.example.chow.minigamemarathon;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kyros on 11/17/2017.
 */

public class HighScoreFragment extends Fragment {

    private TabLayout tabLayout;
    private AppBarLayout appBar;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.high_score_layout, container, false);
        setHasOptionsMenu(true);
        Drawable changeIcon = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_sort_black_24dp);
        Toolbar activityToolbar = getActivity().findViewById(R.id.toolbar);
        activityToolbar.setOverflowIcon(changeIcon);
        //nested fragments for different gamemodes
        GameMode[] gameModes = GameMode.AVAILABLE_GAME_MODES;
        ViewPager viewPager = rootView.findViewById(R.id.view_pager_high_score_container);
        viewPager.setOffscreenPageLimit(gameModes.length-1);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager(), gameModes);
        viewPager.setAdapter(adapter);
        tabLayout = new TabLayout(getActivity());
        tabLayout.setLayoutParams(new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabLayout.setPadding(8, 8, 8, 8);
        tabLayout.setupWithViewPager(viewPager);
        appBar = getActivity().findViewById(R.id.app_bar);
        appBar.addView(tabLayout);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        appBar.removeView(tabLayout);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_scores, menu);
    }

    private String[] getNames(GameMode[] gameModes)
    {
        String[] names = new String[gameModes.length];
        for (int i = 0; i < names.length; i++)
        {
            names[i] = gameModes[i].toString();
        }
        return names;
    }

    private static ArrayList<Score> retrieveScoresFromDatabase()
    {
        //TODO: get scores from database in the method and return the list of scores; the following is only an example
        ArrayList<Score> scores = new ArrayList<>();
        scores.add(new Score("p1", 15602, 1234, GameMode.EASY));
        scores.add(new Score("p2", 12345, 7855, GameMode.EASY));
        scores.add(new Score("p3", 45613, 3124, GameMode.EASY));
        scores.add(new Score("p1", 34561, 6451, GameMode.EASY));
        scores.add(new Score("p2", 78945, 1254, GameMode.EASY));
        scores.add(new Score("p3", 45612, 3895, GameMode.HARD));
        scores.add(new Score("p1", 75654, 9871, GameMode.HARD));
        scores.add(new Score("p2", 22546, 8888, GameMode.HARD));
        scores.add(new Score("p3", 34551, 1245, GameMode.HARD));
        scores.add(new Score("p1", 66895, 4561, GameMode.HARD));
        scores.add(new Score("p2", 12345, 5582, GameMode.HARD));
        scores.add(new Score("p3", 22345, 7125, GameMode.DEBUG));
        scores.add(new Score("p1", 96532, 9326, GameMode.DEBUG));
        scores.add(new Score("p2", 81534, 4512, GameMode.DEBUG));
        scores.add(new Score("p3", 45124, 2989, GameMode.DEBUG));
        scores.add(new Score("p1", 33654, 3364, GameMode.DEBUG));
        scores.add(new Score("p2", 12014, 1010, GameMode.DEBUG));


        String[] names = {"Player 1", "Player 2", "Player 3"};
        GameMode[] gameModes = GameMode.AVAILABLE_GAME_MODES;
        Random random = new Random();
        for (int i = 0; i < 10; i++)
        {
            String name = names[random.nextInt(names.length)];
            int score = random.nextInt(30000) + 20000;
            long time = random.nextInt(2000) + 1000;
            GameMode randomGameMode = gameModes[random.nextInt(gameModes.length)];
            scores.add(new Score(name, score, time, randomGameMode));
        }

        return scores;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        private GameMode[] gameModes;

        public SectionsPagerAdapter(FragmentManager fm, GameMode[] gameModes) {
            super(fm);
            this.gameModes = gameModes;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return gameModes[position].toString();
        }

        @Override
        public Fragment getItem(int position) {
            HighScoreTabFragment tabFragment = new HighScoreTabFragment();
            tabFragment.setArguments(gameModes[position], retrieveScoresFromDatabase(), position);
            return tabFragment;
        }

        @Override
        public int getCount() {
            return gameModes.length;
        }
    }
}
