package com.example.chow.minigamemarathon;

/**
 * Created by per6 on 11/17/17.
 */

public class Score {
    int _id;
    String _name;
    String _score;
    String _time;
    String _gameMode;

    public Score() {

    }

    public Score(String _name, String _score, String _time, String _gameMode) {
        this._name = _name;
        this._score = _score;
        this._time = _time;
        this._gameMode = _gameMode;
    }

    public Score(int _id, String _name, String _score, String _time, String _gameMode) {
        this._id = _id;
        this._name = _name;
        this._score = _score;
        this._time = _time;
        this._gameMode = _gameMode;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_score() {
        return _score;
    }

    public void set_score(String _score) {
        this._score = _score;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public String get_gameMode() {
        return _gameMode;
    }

    public void set_gameMode(String _gameMode) {
        this._gameMode = _gameMode;
    }
}