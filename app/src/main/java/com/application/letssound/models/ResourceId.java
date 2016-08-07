package com.application.letssound.models;

/**
 * Created by davide on 25/07/16.
 */

import java.io.Serializable;

import io.realm.RealmObject;

/**
 *
 */
public class ResourceId extends RealmObject implements Serializable {
    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * channel. This property is only present if the resourceId.kind value is youtube#channel.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private java.lang.String channelId;

    /**
     * The type of the API resource.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private java.lang.String kind;

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * playlist. This property is only present if the resourceId.kind value is youtube#playlist.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private java.lang.String playlistId;

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * video. This property is only present if the resourceId.kind value is youtube#video.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private java.lang.String videoId;

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * channel. This property is only present if the resourceId.kind value is youtube#channel.
     *
     * @return value or {@code null} for none
     */
    public java.lang.String getChannelId() {
        return channelId;
    }

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * channel. This property is only present if the resourceId.kind value is youtube#channel.
     *
     * @param channelId channelId or {@code null} for none
     */
    public ResourceId setChannelId(java.lang.String channelId) {
        this.channelId = channelId;
        return this;
    }

    /**
     * The type of the API resource.
     *
     * @return value or {@code null} for none
     */
    public java.lang.String getKind() {
        return kind;
    }

    /**
     * The type of the API resource.
     *
     * @param kind kind or {@code null} for none
     */
    public ResourceId setKind(java.lang.String kind) {
        this.kind = kind;
        return this;
    }

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * playlist. This property is only present if the resourceId.kind value is youtube#playlist.
     *
     * @return value or {@code null} for none
     */
    public java.lang.String getPlaylistId() {
        return playlistId;
    }

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * playlist. This property is only present if the resourceId.kind value is youtube#playlist.
     *
     * @param playlistId playlistId or {@code null} for none
     */
    public ResourceId setPlaylistId(java.lang.String playlistId) {
        this.playlistId = playlistId;
        return this;
    }

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * video. This property is only present if the resourceId.kind value is youtube#video.
     *
     * @return value or {@code null} for none
     */
    public java.lang.String getVideoId() {
        return videoId;
    }

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * video. This property is only present if the resourceId.kind value is youtube#video.
     *
     * @param videoId videoId or {@code null} for none
     */
    public ResourceId setVideoId(java.lang.String videoId) {
        this.videoId = videoId;
        return this;
    }
}