package com.example.chow.minigamemarathon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by per6 on 12/19/17.
 */

public class WorldHighScoreFragment extends HighScoreFragment implements BackendlessHandler.ScoreReceivedListener, BackendlessHandler.NetworkStatusChangedListener{
    private BackendlessHandler backendlessHandler;
    private List<Score> scoreResponse;
    private static final String TAG = "WorldHighScoreFragment";

    public WorldHighScoreFragment() {
        super();
        isScoreDeletable = false;
    }

    public void setScoreResponse(List<Score> scoreResponse) {
        this.scoreResponse = scoreResponse;
    }

    @Override
    protected List<Score> retrieveScoresFromDatabase() {
            backendlessHandler = new BackendlessHandler(getContext());
            backendlessHandler.populateScores();
            backendlessHandler.setScoreReceiveListener(this);
            backendlessHandler.setNetworkStatusChangedListener(this);
        return new ArrayList<Score>();
    }

    @Override
    public void onScoreReceived(List<Score> response) {
        Log.d(TAG, "onScoreReceived: " + response.size());
        updateScores(response);
    }
    @Override
    public void onNetworkStatusChanged() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        // Set title
        alertDialogBuilder.setTitle("Wifi Disconnected");
        // Set dialog message
        alertDialogBuilder
                .setMessage("Would you like to open Wifi settings now?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //exit
                    }
                });

        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
