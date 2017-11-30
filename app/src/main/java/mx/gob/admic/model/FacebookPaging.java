package mx.gob.admic.model;

/**
 * Created by codigus on 29/11/2017.
 */

public class FacebookPaging {
    private FacebookCursor cursors;
    private String next;

    public FacebookCursor getCursors() {
        return cursors;
    }

    public void setCursors(FacebookCursor cursors) {
        this.cursors = cursors;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
