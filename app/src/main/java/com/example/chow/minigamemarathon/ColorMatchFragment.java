package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by per6 on 11/30/17.
 */

public class ColorMatchFragment extends GameFragment{
    private GameMode gameMode;
    private ColorMatchAlgorithm game;
    private CheckBox redCheck, greenCheck, blueCheck;
    private Button enterButton;
    private boolean redCheq=false, blueCheq=false, greenCheq=false;
    private TextView colorText;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void assignWidgetFunctions()
    {


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.color_game, container, false);
        game = new ColorMatchAlgorithm(gameMode);
        redCheck=(CheckBox)rootView.findViewById(R.id.redCheckBox);
        greenCheck=(CheckBox)rootView.findViewById(R.id.greenCheckBox);
        blueCheck=(CheckBox)rootView.findViewById(R.id.blueCheckBox);
        enterButton=(Button) rootView.findViewById(R.id.enterB);
        colorText=(TextView)rootView.findViewById(R.id.colorName);
        game.genAnswer();
        colorText.setText(game.getAnsweredStr());
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (redCheq)
                {game.setColorInt1(0);}
                if (greenCheq==true)
                {
                    if (redCheq==true){game.setColorInt2(2);}
                    else {game.setColorInt1(2);}

                }
                if (blueCheq==true)
                    if(redCheq==true||greenCheq==true){game.setColorInt2(4);}
                    else
                    {
                        game.setColorInt1(4);
                        game.setColorInt2(4);
                    }
                    game.checkAnswer();
                    if (game.isFinished())
                    {
                        notifyGameEnd();
                    }
                    colorText.setText(game.getAnsweredStr());
                    colorText.setText(game.getAnsweredStr());


            }
        });
        return rootView;
    }

    @Override
    public String getGameName() {return "Color Match Challenge!";}

    @Override
    public double getPercentScore() {
        Log.d("COMPLETE", "GetPercent score"+((double)(game.getQuestionsCorrect())/(game.getQuestionAttempted())));//HERE HERE HERE HERE HERE HERE
        Log.d("COMPLETE", "Get score correct"+game.getQuestionsCorrect());
        Log.d("COMPLETE", "GetAttempted"+game.getQuestionAttempted());
        return ((double)(game.getQuestionsCorrect())/(game.getQuestionAttempted()));
    }

    @Override
    public boolean isSolved() {

        return game.isFinished();

    }

    @Override
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }



    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.redCheckBox:
                if (checked)
                redCheq=true;
            else
                redCheq=false;
                break;
            case R.id.blueCheckBox:
                if (checked)
                blueCheq=true;
            else
                blueCheq=false;
                break;
            case R.id.greenCheckBox:
                if (checked)
                greenCheq=true;
            else
                greenCheq=false;
                break;

            // TODO: Veggie sandwich
        }
    }

}
