package com.example.davide.letssound.models;


import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by davide on 16/07/16.
 */
public class SoundTrack extends RealmObject implements Serializable {
    private ResourceId id;
    private String kind;
    private SoundTrackSnippet snippet;
    private long timestamp;


    public void setTimestamp() {
        this.timestamp = System.currentTimeMillis()/1000;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
    /**
     *
     * @return
     */
    public ResourceId getId() {
        return id;
    }

    /**
     * The id object contains information that can be used to uniquely identify the resource that
     * matches the search request.
     * @param id id or {@code null} for none
     */
    public SoundTrack setId(ResourceId id) {
        this.id = id;
        return this;
    }

    /**
     * Identifies what kind of resource this is. Value: the fixed string "youtube#searchResult".
     * @return value or {@code null} for none
     */
    public String getKind() {
        return kind;
    }

    /**
     * Identifies what kind of resource this is. Value: the fixed string "youtube#searchResult".
     * @param kind kind or {@code null} for none
     */
    public SoundTrack setKind(String kind) {
        this.kind = kind;
        return this;
    }

    /**
     * The snippet object contains basic details about a search result, such as its title or
     * description. For example, if the search result is a video, then the title will be the video's
     * title and the description will be the video's description.
     * @return value or {@code null} for none
     */
    public SoundTrackSnippet getSnippet() {
        return snippet;
    }

    /**
     * The snippet object contains basic details about a search result, such as its title or
     * description. For example, if the search result is a video, then the title will be the video's
     * title and the description will be the video's description.
     * @param snippet snippet or {@code null} for none
     */
    public SoundTrack setSnippet(SoundTrackSnippet snippet) {
        this.snippet = snippet;
        return this;
    }
}
