package mx.gob.admic.api;

import java.util.List;

import mx.gob.admic.model.FacebookPaging;
import mx.gob.admic.model.FacebookPost;


public class ResponseFacebook {
    List<FacebookPost> data;
    FacebookPaging paging;

    public List<FacebookPost> getData() {
        return data;
    }

    public void setData(List<FacebookPost> data) {
        this.data = data;
    }

    public FacebookPaging getPaging() {
        return paging;
    }

    public void setPaging(FacebookPaging paging) {
        this.paging = paging;
    }
}
