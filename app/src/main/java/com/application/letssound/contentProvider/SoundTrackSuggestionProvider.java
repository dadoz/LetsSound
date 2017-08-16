package com.application.letssound.contentProvider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by davide-syn on 8/16/17.
 */

public class SoundTrackSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.application.letssound.contentProvider.SoundTrackSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SoundTrackSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
