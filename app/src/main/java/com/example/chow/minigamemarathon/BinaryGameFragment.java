package com.example.chow.minigamemarathon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BinaryGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BinaryGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BinaryGameFragment extends GameFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int numberClicked;
    private int leftNumber = 0;
    private int rightNumber = 1;
    private boolean buttonPressed = false;
    private int score;
    private boolean done;
    private String gameText;

    public BinaryGameFragment() {
        // Required empty public constructor
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
        View v = inflater.inflate(R.layout.fragment_binary_game, container, false);
        //Wires widgets
        BinaryGame game = new BinaryGame();
        gameText = game.getBinaryString();
        StringBuilder b = new StringBuilder(gameText);
        TextView binaryText = v.findViewById(R.id.binary_view);
        Button buttonLeft = v.findViewById(R.id.button_left);
        Button buttonRight = v.findViewById(R.id.button_right);
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        binaryText.setText(gameText);
        buttonLeft.setText(leftNumber + "");
        buttonRight.setText(rightNumber + "");
        int currentIndex = 0;
        done = false;
        score = 0;
        while(!done){
            if(currentIndex <= gameText.length() - 1){
                if(numberClicked == Integer.parseInt(gameText.substring(currentIndex, currentIndex + 1))){
                        b.append(gameText);
                        String toReplace = "<font color=#d6d6d6" + gameText.substring(currentIndex, currentIndex + 1) + "</font>";
                        b.deleteCharAt(currentIndex);
                        //TODO: Fix offset bug
                        b.insert(currentIndex + 1,toReplace);
                        //TODO: Figure out if this is actually the right way to do it
                        currentIndex += toReplace.length() - 1;
                        binaryText.setText(Html.fromHtml(b.toString()));
                        score++;
                        //Random number test for switching buttons
                        if((int) (Math.random() * 100 + 1) < 36){
                            buttonRight.setText(leftNumber + "");
                            buttonLeft.setText(rightNumber + "");
                        }
                        else{
                            buttonRight.setText (rightNumber + "");
                            buttonLeft.setText(leftNumber + "");
                        }
                }
                else{
                    b.append(gameText);
                    String toReplace = "<font color=#d81c1c" + gameText.substring(currentIndex, currentIndex + 1) + "</font>";
                    b.deleteCharAt(currentIndex);
                    b.insert(currentIndex + 1,toReplace);
                    currentIndex += toReplace.length() - 1;
                    binaryText.setText(Html.fromHtml(b.toString()));
                    if((int) (Math.random() * 100 + 1) < 36){
                        buttonRight.setText(leftNumber + "");
                        buttonLeft.setText(rightNumber + "");
                    }
                    else{
                        buttonRight.setText (rightNumber + "");
                        buttonLeft.setText(leftNumber + "");
                    }
                }
            }
            else{
                done = true;
            }
        }
        return v;
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
        switch(view.getId()){
            case R.id.button_left:
                numberClicked = leftNumber;
                buttonPressed = true;
                break;
            case R.id.button_right:
                numberClicked = rightNumber;
                buttonPressed = true;
                break;
            default:
                Log.wtf("BinaryGameFragment","You should not see this.");
                buttonPressed = false;
                break;
        }
    }

    @Override
    public double getPercentScore() {
        return (double) (score/50);
    }

    @Override
    public boolean isSolved() {
        return done;
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
