package mx.gob.admic.model;

/**
 * Created by codigus on 29/11/2017.
 */

public class YoutubeVideoThumbnailList {
    private YoutubeVideoThumbnail medium;
    private YoutubeVideoThumbnail high;
    private YoutubeVideoThumbnail standard;
    private YoutubeVideoThumbnail maxres;

    public YoutubeVideoThumbnail getMedium() {
        return medium;
    }

    public void setMedium(YoutubeVideoThumbnail medium) {
        this.medium = medium;
    }

    public YoutubeVideoThumbnail getHigh() {
        return high;
    }

    public void setHigh(YoutubeVideoThumbnail high) {
        this.high = high;
    }

    public YoutubeVideoThumbnail getStandard() {
        return standard;
    }

    public void setStandard(YoutubeVideoThumbnail standard) {
        this.standard = standard;
    }

    public YoutubeVideoThumbnail getMaxres() {
        return maxres;
    }

    public void setMaxres(YoutubeVideoThumbnail maxres) {
        this.maxres = maxres;
    }
}
