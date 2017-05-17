package com.example.android.googlebooklisting;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Marcin on 2017-05-17.
 */

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
