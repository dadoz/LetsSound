package com.example.davide.letssound.managers;

import com.google.api.services.youtube.YouTubeRequest;

/**
 * Created by davide on 13/07/16.
 */
public interface SearchManagerInterface {
    public Object onSearchSync();
    public void onSearchAsync();

}
