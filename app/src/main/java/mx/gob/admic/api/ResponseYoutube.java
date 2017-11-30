package mx.gob.admic.api;

import java.util.List;

import mx.gob.admic.model.YoutubeChannel;
import mx.gob.admic.model.YoutubePageInfo;

/**
 * Created by codigus on 29/11/2017.
 */

public class ResponseYoutube<T> {
    private String kind;
    private String etag;
    private String nextPageToken;
    private YoutubePageInfo pageInfo;
    private T items;

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

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public YoutubePageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(YoutubePageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public T getItems() {
        return items;
    }

    public void setItems(T items) {
        this.items = items;
    }
}
