package com.example.per6.startmenufragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import static com.example.per6.startmenufragments.R.id.button;
import static com.example.per6.startmenufragments.R.id.divide;
import static com.example.per6.startmenufragments.R.id.multiply;
import static com.example.per6.startmenufragments.R.id.numb1;
import static com.example.per6.startmenufragments.R.id.numb2;
import static com.example.per6.startmenufragments.R.id.numb3;
import static com.example.per6.startmenufragments.R.id.numb4;
import static com.example.per6.startmenufragments.R.id.plus;
import static com.example.per6.startmenufragments.R.id.subtract;

public class MainActivity extends AppCompatActivity {
private Button startbutton, button1, button2, button3, button4,add, sub, mult, div;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wirewidgets();
        FragmentManager fragment = getSupportFragmentManager();
        fragment.beginTransaction().replace(R.id.frame,new StartScreenFragment()).commit();
        fragment.beginTransaction().replace(R.id.frame,new CanUEvenMattth()).commit();
    }

    private void wirewidgets() {
        startbutton=(Button) findViewById(button);
        button1=(Button) findViewById(numb1);
        button2=(Button) findViewById(numb2);
        button3=(Button) findViewById(numb3);
        button4=(Button) findViewById(numb4);
        add=(Button) findViewById(plus);
        sub=(Button) findViewById(subtract);
        mult=(Button) findViewById(multiply);
        div=(Button) findViewById(divide);

    }

}
