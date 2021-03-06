package com.example.chow.minigamemarathon;

import android.content.Context;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

/**
 * Created by per6 on 12/12/17.
 */

public class BackendlessHandler{
    public static final String TAG = "BackendlessHandler";
    private List<Score> returnValue;
    private ScoreReceivedListener listener;
    private NetworkStatusChangedListener networkListener;
    private Context context;

    public BackendlessHandler(Context context) {
        this.context = context;
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(context, Defaults.APPLICATION_ID, Defaults.API_KEY);
    }

    public void saveScore(Score score){
        Backendless.Persistence.save(score, new AsyncCallback<Score>() {
            @Override
            public void handleResponse(Score response) {

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "handleFault: Backendless error " + fault.getCode() + " Message: " + fault.getMessage() + " Details: " + fault.getDetail());
            }
        });
    }
    public void updateScore(Score oldScore, final Score newScore){
        Backendless.Persistence.of(Score.class).findById(oldScore.getObjectId(), new AsyncCallback<Score>() {
            @Override
            public void handleResponse(Score response) {
                response.setGameMode(newScore.getGameMode());
                response.setTime(newScore.getTime());
                response.setScore(newScore.getScore());
                response.setName(newScore.getName());
                response.setId(newScore.getId());
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
    public void populateScores(){

        Backendless.Persistence.of(Score.class).find(new AsyncCallback<List<Score>>() {
            @Override
            public void handleResponse(List<Score> response) {
                listener.onScoreReceived(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                networkListener.onNetworkStatusChanged();
                Log.e(TAG, "handleFault: Backendless error " + fault.getCode() + " Message: " + fault.getMessage() + " Details: " + fault.getDetail());
            }
        });
    }

    public void setScoreReceiveListener(ScoreReceivedListener listener)
    {
        this.listener = listener;
    }

    public void setNetworkStatusChangedListener(NetworkStatusChangedListener listener){
        this.networkListener = listener;

    }

    public interface ScoreReceivedListener
    {
        public void onScoreReceived(List<Score> response);
    }

    public interface NetworkStatusChangedListener
    {
        public void onNetworkStatusChanged();
    }
}
