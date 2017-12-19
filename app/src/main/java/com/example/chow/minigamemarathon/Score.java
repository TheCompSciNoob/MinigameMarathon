package com.example.chow.minigamemarathon;

import java.util.Date;

/**
 * Created by per6 on 11/17/17.
 */

public class Score {
    private String objectId;
    int id;
    String name;
    String score;
    String time;
    String gameMode;
    public Date created;
    public Date updated;

    public Score() {

    }

    public Score(String name, String score, String time, String gameMode) {
        this.name = name;
        this.score = score;
        this.time = time;
        this.gameMode = gameMode;
    }

    public Score(int id, String name, String score, String time, String gameMode) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.time = time;
        this.gameMode = gameMode;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId( String objectId ) {
        this.objectId = objectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}