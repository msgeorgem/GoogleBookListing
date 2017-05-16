package com.example.android.googlebooklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Marcin on 2017-05-14.
 */

public class QueryUtils {    /**
 * Tag for the log messages
 */
public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return an {@link ArrayList<Book>} object to represent a single news.
     */
    public static ArrayList<Book> fetchNewsData(String requestUrl) {
        Log.i(LOG_TAG,"fetch Book Data");

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Book> book = extractBook(jsonResponse);

        // Return the {@link Event}
        return book;


    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Book> extractBook(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding book to
        ArrayList<Book> book = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject root = new JSONObject(bookJSON);
            JSONArray items = root.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject bookS = items.getJSONObject(i);
                JSONObject volumeInfo = bookS.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");

                String author;
                if (volumeInfo.has("authors")) {
                    author = volumeInfo.getString("authors");
                    author = author.replace("\"", " ");
                    author = author.replace("]", "");
                    author = author.replace("[", "");
                } else {
                    author = "author unknown";
                }

                String publishedDate;
                if (volumeInfo.has("publishedDate")) {
                    publishedDate = volumeInfo.getString("publishedDate");
                } else {
                    publishedDate = "date unknown";
                }

                String description;
                if (volumeInfo.has("description")) {
                    description = volumeInfo.getString("description");
                } else {
                    description = "no description provided";
                }

                String url = volumeInfo.getString("infoLink");

                Double rating;
                if (volumeInfo.has("averageRating")) {
                    rating = volumeInfo.getDouble("averageRating");
                } else {
                    rating = 0.0;
                }
                String language = volumeInfo.getString("language");

                JSONObject imageLinks;
                String thumbnail;
                if (volumeInfo.has("imageLinks")) {
                    imageLinks = volumeInfo.getJSONObject("imageLinks");
                    thumbnail = imageLinks.getString("thumbnail");
                } else {
                    thumbnail = "0";
                }

                book.add(new Book(title, author, publishedDate, description, url, rating, language, thumbnail));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of books
        return book;
    }
}
