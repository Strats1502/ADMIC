package mx.gob.admic.model;

import java.util.List;

/**
 * Created by codigus on 29/11/2017.
 */

public class YoutubeVideoSnippet {
    private String publishedAt;
    private String channelId;
    private String title;
    private String description;
    private YoutubeVideoThumbnailList thumbnails;
    private String channelTitle;
    private String playlistId;
    private int position;
    private YoutubeVideoResourceId resourceId;

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YoutubeVideoThumbnailList getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(YoutubeVideoThumbnailList thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public YoutubeVideoResourceId getResourceId() {
        return resourceId;
    }

    public void setResourceId(YoutubeVideoResourceId resourceId) {
        this.resourceId = resourceId;
    }
}
