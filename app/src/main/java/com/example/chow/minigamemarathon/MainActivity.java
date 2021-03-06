package com.example.chow.minigamemarathon;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private int currentIDSelected = R.id.game_new; //startup fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onDrawerClosed(null); //opens startup fragment
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //closes drawer first and then loads fragment
        //avoids drawer lag
        currentIDSelected = item.getItemId();
        drawer.addDrawerListener(this);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (drawerView != null) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.removeDrawerListener(this);
        }
        Fragment fragment = null;
        if (currentIDSelected == R.id.game_new) {
            GameContainerFragment gameContainerFragment = new GameContainerFragment();
            gameContainerFragment.setArguments(GameContainerFragment.getAllGames());
            fragment = gameContainerFragment;
        } else if (currentIDSelected == R.id.score_high) {
            fragment = new HighScoreFragment();
        } else if (currentIDSelected == R.id.world_leader_board) {
            fragment = new WorldHighScoreFragment();
        } else if (currentIDSelected == R.id.practice) {
            fragment = new PracticeNavigationFragment();
        } else if (currentIDSelected == R.id.how_to_play) {
            fragment = new HowToPlayFragment();
        } else if (currentIDSelected == R.id.about_us) {
            fragment = new AboutUsFragment();
        }
        //start the fragment
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.display_frame, fragment)
                    .commit();
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        //nothing
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        //nothing
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        //nothing
    }
}
