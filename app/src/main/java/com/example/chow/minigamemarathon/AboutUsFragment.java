package com.example.chow.minigamemarathon;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by per6 on 12/21/17.
 */

public class AboutUsFragment extends Fragment {

    public static final String[] developersList = {"<b>Project Lead</b> <br>TheCompSciNoob *Shears*",
            "<b>Databse Engineer</b> <br>hedgy579 *Return before async callback*",
            "<b>Colors</b> <br>Chrysopelea-Rc *Object-less*" };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.about_us_layout, container, false);
        TextView developersListTextView = rootView.findViewById(R.id.develop_list_textview);
        for (String developer : developersList) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                developersListTextView.append(Html.fromHtml(developer, Html.FROM_HTML_MODE_COMPACT));
            }
            else
            {
                developersListTextView.append(Html.fromHtml(developer));
            }
            developersListTextView.append("\n");
        }
        Button sourceCodeButton = rootView.findViewById(R.id.source_code_button);
        sourceCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: open github repository
                //TODO: Alex fix this
            }
        });

        return rootView;
    }
}
