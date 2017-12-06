package mx.gob.admic.model;

/**
 * Created by codigus on 30/11/2017.
 */

public class YoutubeChannelSnippet {
    private String title;
    private String description;
    private String publishedAt;
    private YoutubeChannelThumbnailList thumbnails;

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

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public YoutubeChannelThumbnailList getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(YoutubeChannelThumbnailList thumbnails) {
        this.thumbnails = thumbnails;
    }
}
