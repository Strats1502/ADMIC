package mx.gob.jovenes.guanajuato.sesion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import mx.gob.jovenes.guanajuato.model.Usuario;

/**
 * Created by code on 7/03/17.
 */

public class Sesion {

    private static SharedPreferences prefs;

    private static final String ID_USUARIO = "id_usuario";
    private static final String EMAIL  = "email";
    private static final String API_TOKEN = "api_token";
    private static final String NOMBRE = "nombre";
    private static final String ID_DATOS_USUARIO = "id_datos_usuario";
    private static final String ID_GENERO = "id_genero";
    private static final String FECHA_NACIMIENTO = "fecha_nacimiento";
    private static final String ID_OCUPACION = "id_ocupacion";
    private static final String CODIGO_POSTAL = "codigo_postal";
    private static final String TELEFONO = "telefono";
    private static final String CURP = "curp";
    private static final String ID_ESTADO = "id_estado";
    private static final String ID_MUNICIPIO = "id_municipio";
    private static final String RUTA_IMAGEN = "ruta_imagen";

    public Sesion(Context ctx){

        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static int getIdUsuario(){
        return prefs.getInt(ID_USUARIO, 0);
    }

    public static String getEmail(){
        return prefs.getString(EMAIL, null);
    }

    public static String getApiToken(){
        return prefs.getString(API_TOKEN, null);
    }

    public static String getNombre(){
        return prefs.getString(NOMBRE, null);
    }

    public static int getIdDatosUsuario(){
        return prefs.getInt(ID_DATOS_USUARIO, 0);
    }

    public static int getIdGenero(){
        return prefs.getInt(ID_GENERO, 0);
    }

    public static String getFechaNacimiento(){
        return prefs.getString(FECHA_NACIMIENTO, null);
    }

    public static int getIdOcupacion(){
        return prefs.getInt(ID_OCUPACION, 0);
    }

    public static int getCodigoPostal(){
        return prefs.getInt(CODIGO_POSTAL, 0);
    }

    public static String getTelefono(){
        return prefs.getString(TELEFONO, null);
    }

    public static String getCurp(){
        return prefs.getString(CURP, null);
    }

    public static int getIdEstado(){
        return prefs.getInt(ID_ESTADO, 0);
    }
    public static int getIdMunicipio(){
        return prefs.getInt(ID_MUNICIPIO, 0);
    }
    public static String getRutaImagen (){
        return prefs.getString(RUTA_IMAGEN, null);
    }


    /**
     * Permite cargar una instancia de Usuario como el objeto de sesión en el momento.
     * @param usuario
     */
    public static void cargarSesion(Usuario usuario) {
        prefs.edit().putInt(ID_USUARIO, usuario.getId()).apply();
        prefs.edit().putInt(ID_DATOS_USUARIO, usuario.getIdDatosUsuario()).apply();
        prefs.edit().putInt(ID_ESTADO, usuario.getIdEstado()).apply();
        prefs.edit().putInt(ID_MUNICIPIO, usuario.getIdMunicipio()).apply();
        prefs.edit().putInt(ID_GENERO, usuario.getIdGenero()).apply();
        prefs.edit().putInt(ID_OCUPACION, usuario.getIdOcupacion()).apply();
        prefs.edit().putInt(CODIGO_POSTAL, usuario.getCodigo_postal()).apply();
        prefs.edit().putString(EMAIL, usuario.getEmail()).apply();
        prefs.edit().putString(API_TOKEN, usuario.getApiToken()).apply();
        prefs.edit().putString(NOMBRE, usuario.getNombre()).apply();
        if (usuario.getFechaNacimiento() != null) {
            prefs.edit().putString(FECHA_NACIMIENTO, usuario.getFechaNacimiento().toString()).apply();
        }
        prefs.edit().putString(TELEFONO, usuario.getTelefono()).apply();
        prefs.edit().putString(CURP, usuario.getCurp()).apply();
        prefs.edit().putString(RUTA_IMAGEN, usuario.getRutaImagen()).apply();


    }

    /**
     * Elimina todos los datos guardados en la sesión
     */
    public static void logout(){
        prefs.edit().remove(ID_USUARIO).apply();
        prefs.edit().remove(ID_DATOS_USUARIO).apply();
        prefs.edit().remove(ID_ESTADO).apply();
        prefs.edit().remove(ID_MUNICIPIO).apply();
        prefs.edit().remove(ID_GENERO).apply();
        prefs.edit().remove(ID_OCUPACION).apply();
        prefs.edit().remove(CODIGO_POSTAL).apply();
        prefs.edit().remove(EMAIL).apply();
        prefs.edit().remove(API_TOKEN).apply();
        prefs.edit().remove(NOMBRE).apply();
        prefs.edit().remove(FECHA_NACIMIENTO).apply();
        prefs.edit().remove(TELEFONO).apply();
        prefs.edit().remove(CURP).apply();
        prefs.edit().remove(RUTA_IMAGEN).apply();
    }

}