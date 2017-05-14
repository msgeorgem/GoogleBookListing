package com.example.android.googlebooklisting;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Marcin on 2017-05-14.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy mm dd");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatRating(double rating) {
        DecimalFormat ratingFormat = new DecimalFormat("0.0");
        return ratingFormat.format(rating);
    }

    private int getRatingColor(double rating) {
        int ratingColorResourceId;

        int ratingFloor = (int) Math.floor(rating);
        switch (ratingFloor) {
            case 0:
            case 1:
                ratingColorResourceId = R.color.rating1;
                break;
            case 2:
                ratingColorResourceId = R.color.rating2;
                break;
            case 3:
                ratingColorResourceId = R.color.rating3;
                break;
            case 4:
                ratingColorResourceId = R.color.rating4;
                break;
            case 5:
                ratingColorResourceId = R.color.rating5;
                break;
            case 6:
                ratingColorResourceId = R.color.rating6;
                break;
            case 7:
                ratingColorResourceId = R.color.rating7;
                break;
            case 8:
                ratingColorResourceId = R.color.rating8;
                break;
            case 9:
                ratingColorResourceId = R.color.rating9;
                break;
            default:
                ratingColorResourceId = R.color.rating10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), ratingColorResourceId);
    }

    public BookAdapter(Activity context, List<Book> listBooks) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0b0, listBooks);
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
        ViewHolder viewHolder;;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Book currentBook = getItem(position);
//        viewHolder.thumbnailView.setImageIcon(currentBook.getThumbnail());
        viewHolder.titleTextView.setText(currentBook.getTitle());
        viewHolder.subtitleTextView.setText(currentBook.getSubtitle());
        viewHolder.descriptionTextView.setText(currentBook.getDescription());
        // TODO: Fix the date format
        Date dateObject = new Date(currentBook.getTimeInMilliseconds());
        String formattedDate = formatDate(dateObject);
        viewHolder.publishedDateView.setText(formattedDate);

        String formattedRating = formatRating(currentBook.getRating());
        viewHolder.ratingView.setText(formattedRating);
        // Set the proper background color on the rating circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
//        GradientDrawable ratingCircle = (GradientDrawable) viewHolder.ratingView.getBackground();
        // Get the appropriate background color based on the current book rating
//        int ratingColor = getRatingColor(currentBook.getRating());
        // Set the color on the rating circle
//        ratingCircle.setColor(ratingColor);

        // Return the whole list item layout (containing 5 TextViews 1 ImageView)
        // so that it can be shown in the ListView
        return convertView;
    }
    class ViewHolder {
        private ImageView thumbnailView;
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
                    .findViewById(R.id.subtitle);
            this.descriptionTextView = (TextView)view
                    .findViewById(R.id.description);
            this.publishedDateView = (TextView)view
                    .findViewById(R.id.published_date);
            this.ratingView = (TextView)view
                    .findViewById(R.id.rating);
        }
    }
}