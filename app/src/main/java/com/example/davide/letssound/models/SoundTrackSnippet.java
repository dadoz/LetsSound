package com.example.davide.letssound.models;

/**
 * Created by davide on 25/07/16.
 */

import com.google.api.client.util.DateTime;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 *
 */
public class SoundTrackSnippet extends RealmObject implements Serializable {

    /**
     * The value that YouTube uses to uniquely identify the channel that published the resource that
     * the search result identifies.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private String channelId;

    /**
     * The title of the channel that published the resource that the search result identifies.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private String channelTitle;

    /**
     * A description of the search result.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private String description;

    /**
     * It indicates if the resource (video or channel) has upcoming/active live broadcast content. Or
     * it's "none" if there is not any upcoming/active live broadcasts.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private String liveBroadcastContent;

    /**
     * The creation date and time of the resource that the search result identifies. The value is
     * specified in ISO 8601 (YYYY-MM-DDThh:mm:ss.sZ) format.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private String publishedAt;

    /**
     * A map of thumbnail images associated with the search result. For each object in the map, the
     * key is the name of the thumbnail image, and the value is an object that contains other
     * information about the thumbnail.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private ThumbnailDetails thumbnails;

    /**
     * The title of the search result.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private String title;

    /**
     * The value that YouTube uses to uniquely identify the channel that published the resource that
     * the search result identifies.
     * @return value or {@code null} for none
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * The value that YouTube uses to uniquely identify the channel that published the resource that
     * the search result identifies.
     * @param channelId channelId or {@code null} for none
     */
    public SoundTrackSnippet setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    /**
     * The title of the channel that published the resource that the search result identifies.
     * @return value or {@code null} for none
     */
    public String getChannelTitle() {
        return channelTitle;
    }

    /**
     * The title of the channel that published the resource that the search result identifies.
     * @param channelTitle channelTitle or {@code null} for none
     */
    public SoundTrackSnippet setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
        return this;
    }

    /**
     * A description of the search result.
     * @return value or {@code null} for none
     */
    public String getDescription() {
        return description;
    }

    /**
     * A description of the search result.
     * @param description description or {@code null} for none
     */
    public SoundTrackSnippet setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * It indicates if the resource (video or channel) has upcoming/active live broadcast content. Or
     * it's "none" if there is not any upcoming/active live broadcasts.
     * @return value or {@code null} for none
     */
    public String getLiveBroadcastContent() {
        return liveBroadcastContent;
    }

    /**
     * It indicates if the resource (video or channel) has upcoming/active live broadcast content. Or
     * it's "none" if there is not any upcoming/active live broadcasts.
     * @param liveBroadcastContent liveBroadcastContent or {@code null} for none
     */
    public SoundTrackSnippet setLiveBroadcastContent(String liveBroadcastContent) {
        this.liveBroadcastContent = liveBroadcastContent;
        return this;
    }

    /**
     * The creation date and time of the resource that the search result identifies. The value is
     * specified in ISO 8601 (YYYY-MM-DDThh:mm:ss.sZ) format.
     * @return value or {@code null} for none
     */
    public DateTime getPublishedAt() {
        return new DateTime(publishedAt);
    }

    /**
     * The creation date and time of the resource that the search result identifies. The value is
     * specified in ISO 8601 (YYYY-MM-DDThh:mm:ss.sZ) format.
     * @param publishedAt publishedAt or {@code null} for none
     */
    public SoundTrackSnippet setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    /**
     * A map of thumbnail images associated with the search result. For each object in the map, the
     * key is the name of the thumbnail image, and the value is an object that contains other
     * information about the thumbnail.
     * @return value or {@code null} for none
     */
    public ThumbnailDetails getThumbnails() {
        return thumbnails;
    }

    /**
     * A map of thumbnail images associated with the search result. For each object in the map, the
     * key is the name of the thumbnail image, and the value is an object that contains other
     * information about the thumbnail.
     * @param thumbnails thumbnails or {@code null} for none
     */
    public SoundTrackSnippet setThumbnails(ThumbnailDetails thumbnails) {
        this.thumbnails = thumbnails;
        return this;
    }

    /**
     * The title of the search result.
     * @return value or {@code null} for none
     */
    public String getTitle() {
        return title;
    }

    /**
     * The title of the search result.
     * @param title title or {@code null} for none
     */
    public SoundTrackSnippet setTitle(String title) {
        this.title = title;
        return this;
    }
}