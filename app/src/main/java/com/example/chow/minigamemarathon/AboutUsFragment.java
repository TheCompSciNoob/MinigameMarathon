package com.example.chow.minigamemarathon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by per6 on 12/21/17.
 */

public class AboutUsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.about_us_layout, container, false);
        TextView developersListTextView = rootView.findViewById(R.id.develop_list_textview);
        Developer[] developers = {new Developer("Project Lead", "TheCompSciNoob *Shears*"),
                new Developer("Database Engineer", "*hedgy579 Return Array Before Async Callback*"),
                new Developer("Colors", "Chrysopelea-Rc *Object-less*")};
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (Developer dev : developers)
        {
            int startIndex = builder.length();
            builder.append(dev.getDevTitle());
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), startIndex, builder.length(), 0);
            builder.append("\n");
            builder.append(dev.getDevDescription());
            builder.append("\n");
        }
        developersListTextView.setText(builder, TextView.BufferType.SPANNABLE);
        //directs player to source code
        Button sourceCodeButton = rootView.findViewById(R.id.source_code_button);
        sourceCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TheCompSciNoob/MinigameMarathon"));
                startActivity(browserIntent);
            }
        });

        return rootView;
    }

    private static class Developer {
        private String devTitle, devDescription;

        public Developer(String devName, String devDescription) {
            this.devTitle = devName;
            this.devDescription = devDescription;
        }

        public String getDevTitle() {
            return devTitle;
        }

        public void setDevTitle(String devTitle) {
            this.devTitle = devTitle;
        }

        public String getDevDescription() {
            return devDescription;
        }

        public void setDevDescription(String devDescription) {
            this.devDescription = devDescription;
        }
    }
}
