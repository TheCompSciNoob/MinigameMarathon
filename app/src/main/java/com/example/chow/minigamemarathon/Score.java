package com.example.chow.minigamemarathon;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by per6 on 11/17/17.
 */

public class Score implements Parcelable {
    int _id;
    String _name;
    String _score;
    String _time;

    GameMode _gameMode; //temporarily added alex you have to put this in the database

    public Score() {

    }

    public Score(int _id, String _name, String _score, String _time) {
        this._id = _id;
        this._name = _name;
        this._score = _score;
        this._time = _time;
    }

    public Score(String _name, String _score, String _time) {
        this(0, _name, _score, _time);
    }

    //temporarily added alex you have to put this in the database
    public Score(String _name, String _score, String _time, GameMode _gameMode) {
        this._name = _name;
        this._score = _score;
        this._time = _time;
        this._gameMode = _gameMode;
    }

    //temporarily added alex you have to put this in the database
    public Score(String _name, int _score, long _time, GameMode _gameMode)
    {
        this(_name, _score + "", _time + "", _gameMode);
    }

    //temporarily added alex you have to put this in the database
    public GameMode get_gameMode() {
        return _gameMode;
    }

    //temporarily added alex you have to put this in the database
    public void set_gameMode(GameMode _gameMode) {
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

    protected Score(Parcel in) {
        _id = in.readInt();
        _name = in.readString();
        _score = in.readString();
        _time = in.readString();
        _gameMode = GameMode.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_name);
        dest.writeString(_score);
        dest.writeString(_time);
        dest.writeString(_gameMode.name());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Score> CREATOR = new Parcelable.Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };
}