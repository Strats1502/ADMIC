package mx.gob.admic.application;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import mx.gob.admic.R;
import mx.gob.admic.fragments.DetalleConvocatoriaFragment;
import mx.gob.admic.fragments.DetalleEventoFragment;
import mx.gob.admic.fragments.HomeFragment;
import mx.gob.admic.sesion.Sesion;
import mx.gob.admic.utils.OKDialog;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Uriel on 6/03/17.
 */

/**
 * Clase de la aplicación que se ejecutará en el dispositivo (punto de partida).
 */
public class MyApplication extends MultiDexApplication {
    private Retrofit retrofit;
    private Retrofit retrofitFacebook;
    private Retrofit retrofitYoutube;

    public static Realm realm;
    public static String LAST_UPDATE_CONVOCATORIAS = "last_update_convocatorias";
    public static String LAST_UPDATE_REGIONES = "last_update_regiones";
    public static String LAST_UPDATE_EVENTOS = "last_update_eventos";
    public static String LAST_UPDATE_EMPRESAS = "last_update_empresas";
    public static final String LAST_UPDATE_PUBLICIDAD = "last_update_publicidad";

    //dirección publica
    //public static final String BASE_URL = "http://200.23.39.11/admicweb/public/api/";

    //uriel publica
    //public static final String BASE_URL = "http://10.0.7.131/GuanajovenWeb/public/api/";

    //public static final String BASE_URL = "http://10.0.7.119/GuanajovenWeb/public/api/";
    public static final String BASE_URL = "http://10.0.7.40/admicweb/public/api/";
    public static final String FACEBOOK_BASE_URL = "https://graph.facebook.com/v2.11/";
    public static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/";
    //public static final String BASE_URL = "http://10.0.7.40/GuanajovenWeb/public/api/";


    //public static final String BASE_URL = "http://guanajovenapp.guanajuato.gob.mx/api/";

    private static int minutes = 5;
    private static long TIEMPO_RESTANTE_CORREOS_CONVOCATORIAS = minutes * 60000;
    private static long TIEMPO_RESTANTE_CORREOS_EVENTOS = minutes * 60000;
    private static long TIEMPO_RESTANTE_CORREOS_CREDITO = minutes * 60000;

    public static CountDownTimer contadorCorreosConvocatorias = new CountDownTimer(TIEMPO_RESTANTE_CORREOS_CONVOCATORIAS, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            TIEMPO_RESTANTE_CORREOS_CONVOCATORIAS = millisUntilFinished;

            Date  date = new Date(TIEMPO_RESTANTE_CORREOS_CONVOCATORIAS);
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            String formatted = formatter.format(date);

            DetalleConvocatoriaFragment.btnQuieroMasInformacion.setText("Espera para enviar otro correo - " + formatted);
            DetalleConvocatoriaFragment.btnQuieroMasInformacion.setEnabled(false);
        }

        @Override
        public void onFinish() {
            TIEMPO_RESTANTE_CORREOS_CONVOCATORIAS = minutes * 60000;
            DetalleConvocatoriaFragment.btnQuieroMasInformacion.setText(R.string.btn_quiero_mas_informacion);
            DetalleConvocatoriaFragment.btnQuieroMasInformacion.setEnabled(true);
        }
    };

    public static CountDownTimer contadorCorreosEventos = new CountDownTimer(TIEMPO_RESTANTE_CORREOS_EVENTOS, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            TIEMPO_RESTANTE_CORREOS_EVENTOS = millisUntilFinished;
            Date date = new Date(TIEMPO_RESTANTE_CORREOS_EVENTOS);
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            String formatted = formatter.format(date);

            DetalleEventoFragment.botonMeInteresa.setText("Espera para enviar otro correo - " + formatted);
            DetalleEventoFragment.botonMeInteresa.setEnabled(false);
        }

        @Override
        public void onFinish() {
            TIEMPO_RESTANTE_CORREOS_EVENTOS = minutes * 60000;
            DetalleEventoFragment.botonMeInteresa.setText(R.string.me_interesa);
            DetalleEventoFragment.botonMeInteresa.setEnabled(true);
        }
    };

    public static String TIEMPO_CORREOS_CREDITO;
    public static boolean enviarCorreos = true;

    public static CountDownTimer contadorCorreosCredito = new CountDownTimer(TIEMPO_RESTANTE_CORREOS_CREDITO, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            TIEMPO_RESTANTE_CORREOS_CREDITO = millisUntilFinished;
            Date date = new Date(TIEMPO_RESTANTE_CORREOS_CREDITO);
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            String formatted = formatter.format(date);
            TIEMPO_CORREOS_CREDITO = formatted;

            enviarCorreos = false;
        }

        @Override
        public void onFinish() {
            TIEMPO_RESTANTE_CORREOS_CREDITO = minutes * 60000;
            enviarCorreos = true;
        }
    };
    /**
     * Punto de partida que ejecuta la app al iniciar.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //Método para iniciar la instancia de la sesión.
        Sesion.sessionStart(this);

        //Instancia de gson utilizada por Retrofit para usarse en otra sección de la app.
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setDateFormat("d/M/yyyy").create();

        //Instancia de retrofit, utilizada en la app.
         retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
         retrofitFacebook = new Retrofit.Builder().baseUrl(FACEBOOK_BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
         retrofitYoutube = new Retrofit.Builder().baseUrl(YOUTUBE_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        //Iniciar Firebase
        Firebase.setAndroidContext(this);
        //Soporte para caracteristicas offline
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

    }

    public static Realm getRealmInstance() {
        return realm;
    }

    public Retrofit getRetrofitInstance(){
        return retrofit;
    }

    public Retrofit getRetrofitFacebook() {
        return retrofitFacebook;
    }

    public Retrofit getRetrofitYoutube() {
        return retrofitYoutube;
    }

}
