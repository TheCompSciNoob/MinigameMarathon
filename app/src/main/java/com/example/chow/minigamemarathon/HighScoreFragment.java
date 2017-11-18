package com.example.chow.minigamemarathon;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kyros on 11/17/2017.
 */

public class HighScoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.high_score_layout, container, false);
        setHasOptionsMenu(true);
        Drawable changeIcon = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_sort_black_24dp);
        Toolbar activityToolbar = getActivity().findViewById(R.id.toolbar);
        activityToolbar.setOverflowIcon(changeIcon);
        //make adapter for tabbed fragments
        //TODO: get scores from database, the following is just a test
//        final ArrayList<Score> scores = new ArrayList<>();
//        scores.add(new Score("p1", "15791", "303"));
//        scores.add(new Score("p2", "20000", "405"));
        GameMode[] gameModes = GameMode.AVAILABLE_GAME_MODES;
        ViewPager viewPager = rootView.findViewById(R.id.view_pager_container);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager(), gameModes);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = rootView.findViewById(R.id.fragment_change_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
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
            tabFragment.setGameMode(gameModes[position]);
            return tabFragment;
        }

        @Override
        public int getCount() {
            return gameModes.length;
        }
    }
}
