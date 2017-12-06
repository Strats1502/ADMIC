package mx.gob.admic.model;

/**
 * Created by codigus on 30/11/2017.
 */

public class YoutubeChannelThumbnailList {
    private YoutubeChannelThumbnail medium;
    private YoutubeChannelThumbnail high;

    public YoutubeChannelThumbnail getMedium() {
        return medium;
    }

    public void setMedium(YoutubeChannelThumbnail medium) {
        this.medium = medium;
    }

    public YoutubeChannelThumbnail getHigh() {
        return high;
    }

    public void setHigh(YoutubeChannelThumbnail high) {
        this.high = high;
    }
}
