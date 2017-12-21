package com.example.chow.minigamemarathon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by per6 on 12/21/17.
 */

public class HowToPlayFragment extends Fragment {

    private RecyclerView gameInstructionList;
    private GameFragment[] allGames = GameContainerFragment.getAllGames();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.how_to_play_layout, container, false);
        //wire the recycler
        gameInstructionList = rootView.findViewById(R.id.how_to_play_recyclerview);
        gameInstructionList.setLayoutManager(new LinearLayoutManager(getContext()));
        gameInstructionList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        gameInstructionList.setAdapter(new HowToPlayAdapter());

        return rootView;
    }

    private class HowToPlayAdapter extends RecyclerView.Adapter<HowToPlayHolder>
    {
        @Override
        public HowToPlayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View howToPlayLayout = inflater.inflate(R.layout.how_to_play_single_item_layout, parent, false);
            return new HowToPlayHolder(howToPlayLayout);
        }

        @Override
        public void onBindViewHolder(HowToPlayHolder holder, int position) {
            GameFragment gameFragment = allGames[position];
            holder.gameIcon.setImageResource(gameFragment.getIconID());
            holder.gameName.setText(gameFragment.getGameName());
            holder.gameDescription.setText(gameFragment.getDescription());
        }

        @Override
        public int getItemCount() {
            return allGames.length;
        }
    }

    private class HowToPlayHolder extends RecyclerView.ViewHolder
    {
        AppCompatImageView gameIcon;
        TextView gameName, gameDescription;

        private HowToPlayHolder(View itemView) {
            super(itemView);
            gameIcon = itemView.findViewById(R.id.game_icon);
            gameName = itemView.findViewById(R.id.game_name_how_to_play);
            gameDescription = itemView.findViewById(R.id.game_description);
        }
    }
}
