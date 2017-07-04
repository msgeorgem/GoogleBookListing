package com.example.android.googlebooklisting;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Marcin on 2017-05-14.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, List<Book> listBooks) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0b0, listBooks);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatRating(double rating) {
        DecimalFormat ratingFormat = new DecimalFormat("0.0");
        return ratingFormat.format(rating);
    }
    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

            viewHolder = new ViewHolder(convertView);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Book currentBook = getItem(position);


        viewHolder.imageURL = currentBook.getThumbnail();
        Context context = viewHolder.imageView.getContext();
        Picasso.with(context).load(viewHolder.imageURL).into(viewHolder.imageView);

        viewHolder.titleTextView.setText(currentBook.getTitle());
        viewHolder.subtitleTextView.setText(currentBook.getAuthor());
        viewHolder.descriptionTextView.setText(currentBook.getDescription());
        viewHolder.publishedDateView.setText(currentBook.getPublishedDate());

        String formattedRating = formatRating(currentBook.getRating());
        viewHolder.ratingView.setText(formattedRating);

        return convertView;
    }
    class ViewHolder {
        public ImageView thumbnailView;
        public Bitmap bitmap;
        public String imageURL;
        ImageView imageView;
        private TextView titleTextView;
        private TextView subtitleTextView;
        private TextView descriptionTextView;
        private TextView publishedDateView;
        private TextView ratingView;


        public ViewHolder(@NonNull View view) {
            this.thumbnailView = (ImageView)view
                    .findViewById(R.id.thumbnail);
            this.titleTextView = (TextView)view
                    .findViewById(R.id.title);
            this.subtitleTextView = (TextView)view
                    .findViewById(R.id.author);
            this.descriptionTextView = (TextView)view
                    .findViewById(R.id.description);
            this.publishedDateView = (TextView)view
                    .findViewById(R.id.published_date);
            this.ratingView = (TextView)view
                    .findViewById(R.id.rating);
        }
    }
}