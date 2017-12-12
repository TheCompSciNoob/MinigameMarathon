package com.example.chow.minigamemarathon;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by per6 on 12/12/17.
 */

public class BackendlessHandler {
    public static final String TAG = "BackendlessHandler";
    public void saveScore(Score score){
        Backendless.Persistence.save(score, new AsyncCallback<Score>() {
            @Override
            public void handleResponse(Score response) {

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "handleFault: Backendless error " + fault.getCode());
            }
        });
    }
    public void updateScore(Score score){
        Backendless.Persistence.
    }
}
