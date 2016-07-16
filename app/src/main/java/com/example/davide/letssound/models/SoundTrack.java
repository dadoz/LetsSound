package com.example.davide.letssound.models;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.ResourceId;

/**
 * Created by davide on 16/07/16.
 */
public class SoundTrack {
    private ResourceId id;
    private String kind;
    private SoundTrackSnippet snippet;

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

    public final class SoundTrackSnippet {

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
//        @com.google.api.client.util.Key
//        private ThumbnailDetails thumbnails;

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
//        public ThumbnailDetails getThumbnails() {
//            return thumbnails;
//        }

        /**
         * A map of thumbnail images associated with the search result. For each object in the map, the
         * key is the name of the thumbnail image, and the value is an object that contains other
         * information about the thumbnail.
         * @param thumbnails thumbnails or {@code null} for none
         */
//        public SoundTrackSnippet setThumbnails(ThumbnailDetails thumbnails) {
//            this.thumbnails = thumbnails;
//            return this;
//        }

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
}
