package com.example.chow.minigamemarathon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by per6 on 11/17/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "highscoresManager";

    private static final String TABLE_HIGHSCORES = "highscores";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";
    private static final String KEY_TIME = "time";
    private static final String KEY_GAMEMODE = "gamemode";
    //TODO: Add gamemode implementation

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_HIGHSCORES_TABLE = "CREATE TABLE " + TABLE_HIGHSCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SCORE + " TEXT," +  KEY_TIME + " TEXT," + KEY_GAMEMODE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_HIGHSCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);
        onCreate(sqLiteDatabase);
    }

    // Add new score
    public void addScore(Score score) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // sets the row values
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, score.getName());
        values.put(KEY_SCORE, score.getScore());
        values.put(KEY_TIME, score.getTime());
        values.put(KEY_GAMEMODE, score.getGameMode());

        // inserts the new row
        sqLiteDatabase.insert(TABLE_HIGHSCORES, null, values);
        sqLiteDatabase.close();
    }

    // Get single score
    public Score getScore(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //select the Score with the given ID
        Cursor cursor = sqLiteDatabase.query(TABLE_HIGHSCORES, new String[]{KEY_ID,KEY_NAME,KEY_SCORE,KEY_TIME, KEY_GAMEMODE},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null ,
                null , null);
        if(cursor != null)
            cursor.moveToFirst();
        //make Score based on values stored in DB
        Score score = new Score(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));

        return score;
    }

    // Get all scores
    public ArrayList<Score> getAllScores() {
        ArrayList<Score> scoreList = new ArrayList<>();
        //Select all in DB
        String selectQuery = "SELECT * FROM " + TABLE_HIGHSCORES;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Score score = new Score();
                score.setId(Integer.parseInt(cursor.getString(0)));
                score.setName(cursor.getString(1));
                score.setScore(cursor.getString(2));
                score.setTime(cursor.getString(3));
                score.setGameMode(cursor.getString(4));
                scoreList.add(score);
            }while(cursor.moveToNext());
        }
        return scoreList;
    }

    // Get num scores
    public int getScoreCount() {
        String countQuery = " SELECT * FROM " + TABLE_HIGHSCORES;
        SQLiteDatabase sqLiteDatabase =  this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
    // Update single score
    public int updateScore(Score score) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, score.getName());
        values.put(KEY_SCORE, score.getScore());
        values.put(KEY_TIME, score.getTime());
        values.put(KEY_GAMEMODE, score.getGameMode());

        //update row
        return sqLiteDatabase.update(TABLE_HIGHSCORES, values, KEY_ID + " = ?", new String[]{String.valueOf(score.getId())});
    }

    // Delete single score
    public void deleteScore(Score score) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_HIGHSCORES, KEY_ID + " = ?", new String[]{ String.valueOf(score.getId())});
        sqLiteDatabase.close();
    }
}
