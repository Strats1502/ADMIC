package mx.gob.admic.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import mx.gob.admic.model.Alarma;
import mx.gob.admic.model.Usuario;
import mx.gob.admic.persistencia.AlarmasDBHelper;
import mx.gob.admic.sesion.Sesion;

/**
 * Created by uriel on 31/07/16.
 */
public class AlarmaBootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        configurarTodasAlarmas(context);
    }

    public static void configurarTodasAlarmas(Context context){

    }
}
