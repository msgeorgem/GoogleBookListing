package com.example.android.googlebooklisting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

/**
 * Created by Marcin on 2017-05-17.
 */

public class ImageAdapter extends ArrayAdapter<String> {

    private String[] imageURLArray;

    private LayoutInflater inflater;

    public ImageAdapter(Context context, int textViewResourceId,
                        String[] imageArray) {

        super(context, textViewResourceId, imageArray);

        // TODO Auto-generated constructor stub


        inflater = ((Activity) context).getLayoutInflater();

        imageURLArray = imageArray;

    }
}
