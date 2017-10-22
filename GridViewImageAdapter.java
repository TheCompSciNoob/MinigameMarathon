package com.example.kyros.gridviewtest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Kyros on 10/22/2017.
 */

public class GridViewImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ImageView> translatedLights;

    public GridViewImageAdapter(Context context, ArrayList<ImageView> translatedLights) {
        this.context = context;
        this.translatedLights = translatedLights;
    }

    @Override
    public int getCount() {
        return translatedLights.size();
    }

    @Override
    public Object getItem(int position) {
        return translatedLights.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null)
        {
            imageView = translatedLights.get(position);
            imageView.setLayoutParams(new GridView.LayoutParams(185, 185)); //could be a problem
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        return imageView;
    }
}