package com.example.davide.letssound.models;

import java.io.Serializable;

/**
 * Created by davide on 25/07/16.
 */
public class ThumbnailDetails implements Serializable {

    /**
     * The default image for this resource.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key("default")
    private Thumbnail default__;

    /**
     * The high quality image for this resource.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private Thumbnail high;

    /**
     * The maximum resolution quality image for this resource.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private Thumbnail maxres;

    /**
     * The medium quality image for this resource.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private Thumbnail medium;

    /**
     * The standard quality image for this resource.
     * The value may be {@code null}.
     */
    @com.google.api.client.util.Key
    private Thumbnail standard;

    /**
     * The default image for this resource.
     * @return value or {@code null} for none
     */
    public Thumbnail getDefault() {
        return default__;
    }

    /**
     * The default image for this resource.
     * @param default__ default__ or {@code null} for none
     */
    public ThumbnailDetails setDefault(Thumbnail default__) {
        this.default__ = default__;
        return this;
    }

    /**
     * The high quality image for this resource.
     * @return value or {@code null} for none
     */
    public Thumbnail getHigh() {
        return high;
    }

    /**
     * The high quality image for this resource.
     * @param high high or {@code null} for none
     */
    public ThumbnailDetails setHigh(Thumbnail high) {
        this.high = high;
        return this;
    }

    /**
     * The maximum resolution quality image for this resource.
     * @return value or {@code null} for none
     */
    public Thumbnail getMaxres() {
        return maxres;
    }

    /**
     * The maximum resolution quality image for this resource.
     * @param maxres maxres or {@code null} for none
     */
    public ThumbnailDetails setMaxres(Thumbnail maxres) {
        this.maxres = maxres;
        return this;
    }

    /**
     * The medium quality image for this resource.
     * @return value or {@code null} for none
     */
    public Thumbnail getMedium() {
        return medium;
    }

    /**
     * The medium quality image for this resource.
     * @param medium medium or {@code null} for none
     */
    public ThumbnailDetails setMedium(Thumbnail medium) {
        this.medium = medium;
        return this;
    }

    /**
     * The standard quality image for this resource.
     * @return value or {@code null} for none
     */
    public Thumbnail getStandard() {
        return standard;
    }

    /**
     * The standard quality image for this resource.
     * @param standard standard or {@code null} for none
     */
    public ThumbnailDetails setStandard(Thumbnail standard) {
        this.standard = standard;
        return this;
    }

    /**
     *
     */
    public class Thumbnail implements Serializable {
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
}