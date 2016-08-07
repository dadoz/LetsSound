package com.application.letssound.models;

/**
 * Created by davide on 25/07/16.
 */

import java.io.Serializable;

import io.realm.RealmObject;

/**
 *
 */
public class Thumbnail extends RealmObject implements Serializable {
    /**
     * (Optional) Height of the thumbnail image.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private java.lang.Long height;

    /**
     * The thumbnail image's URL.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private java.lang.String url;

    /**
     * (Optional) Width of the thumbnail image.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private java.lang.Long width;

    /**
     * (Optional) Height of the thumbnail image.
     * @return value or {@code null} for none
     */
    public java.lang.Long getHeight() {
        return height;
    }

    /**
     * (Optional) Height of the thumbnail image.
     * @param height height or {@code null} for none
     */
    public Thumbnail setHeight(java.lang.Long height) {
        this.height = height;
        return this;
    }

    /**
     * The thumbnail image's URL.
     * @return value or {@code null} for none
     */
    public java.lang.String getUrl() {
        return url;
    }

    /**
     * The thumbnail image's URL.
     * @param url url or {@code null} for none
     */
    public Thumbnail setUrl(java.lang.String url) {
        this.url = url;
        return this;
    }

    /**
     * (Optional) Width of the thumbnail image.
     * @return value or {@code null} for none
     */
    public java.lang.Long getWidth() {
        return width;
    }

    /**
     * (Optional) Width of the thumbnail image.
     * @param width width or {@code null} for none
     */
    public Thumbnail setWidth(java.lang.Long width) {
        this.width = width;
        return this;
    }

}
