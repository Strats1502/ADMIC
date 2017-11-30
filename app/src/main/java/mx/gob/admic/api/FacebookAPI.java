package mx.gob.admic.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by codigus on 28/11/2017.
 */

public interface FacebookAPI {

    @GET("/admicleon/posts")
    Call<ResponseFacebook> getPosts(
            @Query("access_token") String accessToken
    );

    @GET("/admicleon/posts")
    Call<ResponseFacebook> getNextPosts (
            @Query("access_token") String accessToken,
            @Query("after") String after
    );

}
