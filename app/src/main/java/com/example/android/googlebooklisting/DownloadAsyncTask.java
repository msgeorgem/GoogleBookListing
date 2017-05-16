package com.example.android.googlebooklisting;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Marcin on 2017-05-16.
 */

public class DownloadAsyncTask extends AsyncTask<BookAdapter.ViewHolder, Void, BookAdapter.ViewHolder> {


    @Override
    protected BookAdapter.ViewHolder doInBackground(BookAdapter.ViewHolder... params) {
        // Auto-generated method stub
        //load image directly
        BookAdapter.ViewHolder viewHolder = params[0];
        try {
            URL imageURL = new URL(viewHolder.imageURL);
            viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
        } catch (IOException e) {
            // TODO: handle exception
            Log.e("error", "Downloading Image Failed");
            viewHolder.bitmap = null;
        }
        return viewHolder;
    }

    @Override
    protected void onPostExecute(BookAdapter.ViewHolder result) {
        // TODO Auto-generated method stub
        if (result.bitmap == null) {
            result.imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            result.imageView.setImageBitmap(result.bitmap);
        }
    }
}
