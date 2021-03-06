package mx.gob.admic.api;

import java.util.ArrayList;

import mx.gob.admic.model.Evento;
import mx.gob.admic.model.EventoResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by codigus on 11/5/2017.
 */

public interface EventoAPI {

    @GET("eventos")
    Call<Response<ArrayList<Evento>>> obtenerEventos(
            @Query("timestamp") String timestamp
    );

    @POST("eventos/marcar")
    Call<Response<EventoResponse>> marcarEvento (
            @Query("id_evento") int idEvento,
            @Query("api_token") String apiToken,
            @Query("latitud") double latitud,
            @Query("longitud") double longitud
    );

    @POST("eventos/notificacion")
    Call<Response<Boolean>> enviarCorreo (
            @Query("api_token") String apitoken,
            @Query("id_evento") int idvento
    );
}
