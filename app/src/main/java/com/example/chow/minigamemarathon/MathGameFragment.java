package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MathGameFragment extends GameFragment implements View.OnClickListener {

    //ArrayLists for numbersGridView and operatorsListView
    private static final int RESET = 10001, ADD = 10002, SUBTRACT = 10003, MULTIPLY = 10004, DIVIDE = 10005;
    private ArrayList<String> numberOptions;
    private ArrayList<Integer> operatorOptions;
    //widgets in layout
    private View rootView;
    private GridView numbersGridView; //use string
    private ListView operatorsListView; //use imagebuttons
    private TextView question, expression, hint;
    private BaseAdapter operatorAdapter, numberOptionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.math_game_layout, container, false);
        return rootView;
    }

    //TODO: initialize the numbers
    private void makeArrayLists() {
        operatorOptions = new ArrayList<>();
        operatorOptions.add(RESET);
        operatorOptions.add(ADD);
        operatorOptions.add(SUBTRACT);
        operatorOptions.add(MULTIPLY);
        operatorOptions.add(DIVIDE);
        //TODO: put actual numbers from game object into the arraylist
        numberOptions = new ArrayList<>();
        numberOptions.add("1");
        numberOptions.add("2");
        numberOptions.add("3");
        numberOptions.add("4");
        numberOptions.add("5");
        numberOptions.add("6");
    }

    private void setListeners() {
        //adapter for the operators on the right
        operatorAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return operatorOptions.size();
            }

            @Override
            public Object getItem(int i) {
                return operatorOptions.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageButton imageButton;
                if (convertView == null)
                {
                    imageButton = makeOperator(operatorOptions.get(position));
                }
                else
                {
                    imageButton = (ImageButton) convertView;
                }
                return imageButton;
            }
        };
        operatorsListView.setAdapter(operatorAdapter);

        //adapters for number options on the left
        numberOptionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, numberOptions);
        numbersGridView.setAdapter(numberOptionAdapter);
        numbersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //replace numbers to "" when clicked so that user doesn't see it
                String numberChosen = numberOptions.get(position);
                if (!numberChosen.equals(""))
                {
                    expression.append(numberChosen + " ");
                    numberOptions.set(position, "");
                }
                numberOptionAdapter.notifyDataSetChanged();
            }
        });
    }

    private ImageButton makeOperator(int operatorType) {
        ImageButton operatorButton = new ImageButton(getActivity());
        operatorButton.setOnClickListener(this);
        operatorButton.setLayoutParams(new ListView.LayoutParams(185, 185));
        operatorButton.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        operatorButton.setPadding(20, 20, 20, 20);
        operatorButton.setId(operatorType);
        switch (operatorType)
        {
            case RESET:
                operatorButton.setImageResource(R.drawable.ic_autorenew_black_24dp);
                break;
            case ADD:
                operatorButton.setImageResource(R.drawable.ic_add_black_24dp);
                break;
            case SUBTRACT:
                operatorButton.setImageResource(R.drawable.ic_subtract_black_24dp);
                break;
            case MULTIPLY:
                operatorButton.setImageResource(R.drawable.ic_multiply_black_24dp);
                break;
            case DIVIDE:
                operatorButton.setImageResource(R.drawable.ic_divide_black_24dp);
                break;
            default:
                Toast.makeText(getActivity(), "YOU SHOULD NOT SEE THIS", Toast.LENGTH_SHORT).show();
        }
        return operatorButton;
    }

    private void wireWidgets() {
        numbersGridView = rootView.findViewById(R.id.number_options);
        operatorsListView = rootView.findViewById(R.id.operators);
        question = rootView.findViewById(R.id.question_number);
        expression = rootView.findViewById(R.id.expression_input);
        hint = rootView.findViewById(R.id.hint);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case RESET:
                generateProblem();
                break;
            case ADD:
                //view.setText(+)
                break;
            case SUBTRACT:
                break;
            case MULTIPLY:
                break;
            case DIVIDE:
                break;
            default:
                Toast.makeText(getActivity(), "YOU SHOULD NOT SEE THIS", Toast.LENGTH_SHORT).show();
        }
    }

    private int generateProblem() {
        return 0;
    }

    @Override
    public String getGameName() {
        return "Can You Math?";
    }

    @Override
    public double getPercentScore() {
        //TODO: return score
        return 0;
    }

    @Override
    public boolean isSolved() {
        //TODO: return true if game is finished
        return false;
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        //TODO: set the game mode
    }

    @Override
    public void initializeVariables() {
        makeArrayLists();
        wireWidgets();
        setListeners();
    }
}
