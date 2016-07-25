package com.example.davide.letssound.managers;

import com.google.api.services.youtube.YouTubeRequest;

import rx.Subscription;

/**
 * Created by davide on 13/07/16.
 */
public interface MediaSearchManagerInterface {
    public Object onSearchSync(String query);
    public Subscription onSearchAsync(String query);

}
