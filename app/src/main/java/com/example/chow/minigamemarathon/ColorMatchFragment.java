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
    TextView colorText;
    private View rootView;
    private int questionsCorrect = 0,questionCount,questionsAttempted=0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void assignWidgetFunctions()
    {

        game = new ColorMatchAlgorithm(gameMode);
        redCheck=(CheckBox)rootView.findViewById(R.id.redCheckBox);
        greenCheck=(CheckBox)rootView.findViewById(R.id.greenCheckBox);
        blueCheck=(CheckBox)rootView.findViewById(R.id.blueCheckBox);
        enterButton=(Button) rootView.findViewById(R.id.enterB);
        colorText=(TextView)rootView.findViewById(R.id.colorName);

    }

    @Override
    public int getIconID() {
        return R.drawable.ic_videogame_asset_black_24dp;
    }

    @Override
    public String getDescription() {
        return "You are given three primary colors at the bottom of your screen and a secondary color in the center of your screen. Your goal is to make the secondary color with the primary colors provided.";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.color_game, container, false);
        assignWidgetFunctions();
        game.genAnswer();
        setTextColor();
        colorText.setTextColor(android.graphics.Color.WHITE);
        //redCheck.setTextColor(Color.RED);
        //blueCheck.setTextColor(Color.BLUE);
        //greenCheck.setTextColor(Color.GREEN);
        colorText.setText(game.getAnsweredStr());
        colorText.setText(game.getAnsweredStr());
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
                game.genAnswer();
                setTextColor();
                colorText.setText(game.getAnsweredStr());
                if (finished()==true)
                {
                    notifyGameEnd();

                }
            }
        });
        return rootView;
    }

    public boolean finished()
    {
        if (questionsAttempted>=game.getQuestionCount())
        {
            return true;
        }
        else {return false;}
    }
    @Override
    public String getGameName() {return "Color Match";}

    @Override
    public double getPercentScore() {

        return ((double)(questionsCorrect)/(questionsAttempted));
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

    public void checkAnswer()
    {
        if(redCheck.isChecked()==true && blueCheck.isChecked()==true&&greenCheck.isChecked()==false&&game.getAnswerInt()==5)
        {
            questionsCorrect++;
            Log.d("HI", "correct");
        }
        else if(redCheck.isChecked()==true && greenCheck.isChecked()==true&&blueCheck.isChecked()==false&&game.getAnswerInt()==1)
        {
            questionsCorrect++;
            Log.d("HI", "correct");
        }
        else if (greenCheck.isChecked()==true&& blueCheck.isChecked()==true&&redCheck.isChecked()==false&&game.getAnswerInt()==3)
        {
            questionsCorrect++;
            Log.d("HI", "correct");
        }
        else if (greenCheck.isChecked()==false&& blueCheck.isChecked()==false&&redCheck.isChecked()==true&&game.getAnswerInt()==0)
        {
            questionsCorrect++;
            Log.d("HI", "correct");
        }
        else if (greenCheck.isChecked()==true&& blueCheck.isChecked()==false&&redCheck.isChecked()==false&&game.getAnswerInt()==2)
        {
            questionsCorrect++;
            Log.d("HI", "correct");
        }
        else if (greenCheck.isChecked()==false&& blueCheck.isChecked()==true&&redCheck.isChecked()==false&&game.getAnswerInt()==4)
        {
            questionsCorrect++;
            Log.d("HI", "correct");
        }
        else{Log.d("HI", "Wrong!");}
        questionsAttempted++;
        Log.d("HI","color"+game.getAnswerInt());
        Log.d("HI","correct"+questionsCorrect);
        Log.d("HI","attempt"+questionsAttempted);

    }

    public void setTextColor()
    {
        if (game.getAnswerInt()==0)
        {
            colorText.setBackgroundColor(android.graphics.Color.RED);; //"red";
            colorText.setTextColor(android.graphics.Color.WHITE);
        }
        else if (game.getAnswerInt()==1)
        {colorText.setBackgroundColor(android.graphics.Color.YELLOW);
            colorText.setTextColor(android.graphics.Color.BLACK);}
        else if (game.getAnswerInt()==2)
        {colorText.setBackgroundColor(android.graphics.Color.GREEN);
            colorText.setTextColor(android.graphics.Color.BLACK);}
        else if (game.getAnswerInt()==3)
        {colorText.setBackgroundColor(android.graphics.Color.CYAN);
            colorText.setTextColor(android.graphics.Color.BLACK);}
        else if (game.getAnswerInt()==4)
        {colorText.setBackgroundColor(android.graphics.Color.BLUE);
            colorText.setTextColor(android.graphics.Color.WHITE);}
        else if (game.getAnswerInt()==5)
        {colorText.setBackgroundColor(android.graphics.Color.MAGENTA);
            colorText.setTextColor(android.graphics.Color.WHITE);}
    }
}
