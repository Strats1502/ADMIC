package mx.gob.admic.api;

import java.util.List;

import mx.gob.admic.model.YoutubeChannel;
import mx.gob.admic.model.YoutubeVideo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by codigus on 29/11/2017.
 */

public interface YoutubeAPI {

    @GET("channels")
    Call<ResponseYoutube<List<YoutubeChannel>>> getChannel(
            @Query("part") String part,
            @Query("id") String id,
            @Query("key") String key
    );

    @GET("playlistItems")
    Call<ResponseYoutube<List<YoutubeVideo>>> getVideos (
            @Query("part") String part,
            @Query("maxResults") int maxResults,
            @Query("playlistId") String playListId,
            @Query("key") String key
    );

}
