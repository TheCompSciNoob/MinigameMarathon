package com.example.chow.minigamemarathon;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by per6 on 12/19/17.
 */

public class WorldHighScoreFragment extends HighScoreFragment implements BackendlessHandler.ScoreReceivedListener{
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
        return new ArrayList<Score>();
    }

    @Override
    public void onScoreReceived(List<Score> response) {
        Log.d(TAG, "onScoreReceived: " + response.size());
        updateScores(response);
    }
}
