package com.example.davide.letssound.managers;

import rx.Subscription;

/**
 *
 */
public interface MediaSearchManagerInterface {
    public Object onSearchSync(String query);
    public Subscription onSearchAsync(String query);

}