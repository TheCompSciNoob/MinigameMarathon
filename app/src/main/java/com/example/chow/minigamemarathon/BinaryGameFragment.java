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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "BinaryGameFragment";
    private static final int WORDS_PER_CHECKPOINT = 10;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private char numberClicked;
    private static final char NUMBER_OPTION_1 = '0', NUMBER_OPTION_2 = '1';
    private char numberLeft = NUMBER_OPTION_1, numberRight = NUMBER_OPTION_2;
    private SpannableStringBuilder gameText;
    private BinaryGame game;
    private TextView binaryText;
    private Button buttonLeft;
    private Button buttonRight;
    private ForegroundColorSpan textColor;
    private int currentIndex;
    private int score;
    private GameMode gameMode;

    public BinaryGameFragment() {
        // Required empty public constructor
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BinaryGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BinaryGameFragment newInstance(String param1, String param2) {
        BinaryGameFragment fragment = new BinaryGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                score++;
                textColor = new ForegroundColorSpan(Color.GREEN);
                gameText.setSpan(textColor, 0, currentIndex, 0);
                binaryText.setText(gameText, TextView.BufferType.SPANNABLE);
                //manually tell timer to stop
                if (isSolved())
                {
                    notifyGameEnd();
                }
                else {
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
                currentIndex = 0;
                binaryText.setText(gameText);
            }
        }
    }

    private void swapInTextView() {
        char temp = 'a';
        switch (gameText.charAt(currentIndex))
        {
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
    public String getGameName() {
        return "Binary";
    }

    @Override
    public double getPercentScore() {
        return (double) (score / game.getNumChars(gameMode));
    }

    @Override
    public boolean isSolved() {
        return currentIndex >= gameText.length();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
