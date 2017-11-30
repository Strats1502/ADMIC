package mx.gob.admic.model;

/**
 * Created by codigus on 29/11/2017.
 */

public class YoutubeChannel {
    private String kind;
    private String etag;
    private String id;
    private YoutubeChannelContentDetails contentDetails;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public YoutubeChannelContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(YoutubeChannelContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }
}
