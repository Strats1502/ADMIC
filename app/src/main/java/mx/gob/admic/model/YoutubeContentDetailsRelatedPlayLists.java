package mx.gob.admic.model;

/**
 * Created by codigus on 29/11/2017.
 */

public class YoutubeContentDetailsRelatedPlayLists {
    private String favorites;
    private String uploads;
    private String watchHistory;
    private String watchLater;

    public String getFavorites() {
        return favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
    }

    public String getUploads() {
        return uploads;
    }

    public void setUploads(String uploads) {
        this.uploads = uploads;
    }

    public String getWatchHistory() {
        return watchHistory;
    }

    public void setWatchHistory(String watchHistory) {
        this.watchHistory = watchHistory;
    }

    public String getWatchLater() {
        return watchLater;
    }

    public void setWatchLater(String watchLater) {
        this.watchLater = watchLater;
    }
}
