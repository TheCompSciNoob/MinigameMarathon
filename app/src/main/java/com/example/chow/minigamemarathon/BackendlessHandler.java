package com.example.chow.minigamemarathon;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

/**
 * Created by per6 on 12/12/17.
 */

public class BackendlessHandler {
    public static final String TAG = "BackendlessHandler";
    private List<Score> returnValue;

    public BackendlessHandler() {
    }

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
    public void updateScore(Score oldScore, final Score newScore){
        Backendless.Persistence.of(Score.class).findById(oldScore.getObjectId(), new AsyncCallback<Score>() {
            @Override
            public void handleResponse(Score response) {
                response.set_gameMode(newScore.get_gameMode());
                response.set_time(newScore.get_time());
                response.set_score(newScore.get_score());
                response.set_name(newScore.get_name());
                response.set_id(newScore.get_id());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "handleFault: Backendless error " + fault.getCode());
            }
        });
    }
    public void deleteScore(Score deleteScore){
        Backendless.Persistence.of(Score.class).findById(deleteScore.getObjectId(), new AsyncCallback<Score>() {
            @Override
            public void handleResponse(Score response) {
                Backendless.Persistence.of(Score.class).remove(response, new AsyncCallback<Long>() {
                    @Override
                    public void handleResponse(Long response) {

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e(TAG, "handleFault: Backendless error " + fault.getCode());
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "handleFault: Backendless error " + fault.getCode());
            }
        });

    }
    public List<Score> getAllScores(){
        //TODO: Finish getAllScores()
        Backendless.Persistence.of(Score.class).find(new AsyncCallback<List<Score>>() {
            @Override
            public void handleResponse(List<Score> response) {
                returnValue = response;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "handleFault: Backendless error " + fault.getCode());
            }
        });
        return returnValue;
    }

}
