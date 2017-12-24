package com.example.chow.minigamemarathon;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BinaryGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BinaryGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BinaryGameFragment extends GameFragment implements View.OnClickListener {
    private static final String TAG = "BinaryGameFragment";

    private char numberClicked;
    private static final char NUMBER_OPTION_1 = '0', NUMBER_OPTION_2 = '1';
    private char numberLeft = NUMBER_OPTION_1, numberRight = NUMBER_OPTION_2;
    private static double maxScore = 100;
    private SpannableStringBuilder gameText;
    private BinaryGame game;
    private TextView binaryText;
    private Button buttonLeft, buttonRight;
    private ForegroundColorSpan textColor;
    private int currentIndex, errors;
    private GameMode gameMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.binary_game_layout, container, false);
        return v;
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void assignWidgetFunctions() {
        game = new BinaryGame(gameMode);
        //wire widgets
        gameText = new SpannableStringBuilder(game.getBinaryString());
        binaryText = getView().findViewById(R.id.binary_view);
        binaryText.setMovementMethod(new ScrollingMovementMethod());
        buttonLeft = getView().findViewById(R.id.button_left);
        buttonRight = getView().findViewById(R.id.button_right);
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        binaryText.setText(gameText, TextView.BufferType.SPANNABLE);
        binaryText.setTextColor(Color.BLACK);
        buttonLeft.setText(numberLeft + "");
        buttonRight.setText(numberRight + "");
        currentIndex = 0;
    }

    @Override
    public int getIconID() {
        return R.drawable.ic_developer_board_black_24dp;
    }

    @Override
    public String getDescription() {
        return "You are given two buttons at the bottom of the screen and a string of numbers in the center. Your goal is to accurately type the string of numbers, however, both the string of numbers in the center and the buttons at the bottom will switch around during the course of play.";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_left:
                numberClicked = numberLeft;
                break;
            case R.id.button_right:
                numberClicked = numberRight;
                break;
            default:
                Log.wtf("BinaryGameFragment", "You should not see this.");
                break;
        }
        if (textColor != null) {
            gameText.removeSpan(textColor);
        }
        if (!isSolved()) {
            //correct button pressed
            if (numberClicked == gameText.charAt(currentIndex)) {
                currentIndex++;
                //TODO: Change score eval mechanic
                textColor = new ForegroundColorSpan(Color.GREEN);
                gameText.setSpan(textColor, 0, currentIndex, 0);
                binaryText.setText(gameText, TextView.BufferType.SPANNABLE);
                //manually tell timer to stop
                if (isSolved()) {
                    notifyGameEnd();
                } else {
                    if (Math.random() < 0.2) { //probability to swap text in TextView
                        swapInTextView();
                    }
                    if (Math.random() < 0.36) { //probability to swap the buttons
                        swapButtons();
                    }
                }
            } else {
                //if current index is greater than words per checkpoint * current checkpoint
                //currentIndex = words per checkpoint
                //incorrect button pressed
                currentIndex = getLastCheckPoint();
                errors++;
                gameText.setSpan(textColor, 0, currentIndex, 0);
                binaryText.setText(gameText, TextView.BufferType.SPANNABLE);
            }
        }
    }

    private int getLastCheckPoint() {
        switch (gameMode) {
            case EASY:
                return Math.max(currentIndex - 15, 0);
            case HARD:
                return Math.max(currentIndex - 30, 0);
            default:
                return 0;
        }
    }

    private void swapInTextView() {
        char temp = 'a';
        switch (gameText.charAt(currentIndex)) {
            case NUMBER_OPTION_1:
                temp = NUMBER_OPTION_2;
                break;
            case NUMBER_OPTION_2:
                temp = NUMBER_OPTION_1;
                break;
            default:
                Toast.makeText(getActivity(), "Binary Game Error Occurred", Toast.LENGTH_SHORT).show();
        }
        gameText.delete(currentIndex, currentIndex + 1);
        gameText.insert(currentIndex, temp + "");
        binaryText.setText(gameText, TextView.BufferType.SPANNABLE);
    }


    private void swapButtons() {
        char temp = numberLeft;
        numberLeft = numberRight;
        numberRight = temp;
        buttonLeft.setText(numberLeft + "");
        buttonRight.setText(numberRight + "");
    }

    @Override
    public void notifyGameEnd() {
        super.notifyGameEnd();
    }

    @Override
    public String getGameName() {
        return "Binary";
    }

    @Override
    public double getPercentScore() {
        final double errorDepreciation = 0.95;
        double gameScore = maxScore * Math.pow(errorDepreciation, errors);
        return gameScore / maxScore;
    }

    @Override
    public boolean isSolved() {
        return currentIndex >= gameText.length();
    }
}
