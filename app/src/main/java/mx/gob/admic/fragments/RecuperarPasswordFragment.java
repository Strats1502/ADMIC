package mx.gob.admic.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.utilities.Utilities;
import com.google.gson.Gson;

import java.util.HashMap;

import mx.gob.admic.R;
import mx.gob.admic.api.Response;
import mx.gob.admic.api.UsuarioAPI;
import mx.gob.admic.application.MyApplication;
import mx.gob.admic.connection.ClienteHttp;
import mx.gob.admic.model.Usuario;
import mx.gob.admic.model.models_tmp.RecuperarPass;
import mx.gob.admic.utils.EditTextValidations;
import mx.gob.admic.utils.OKDialog;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Autor: Uriel Infante
 * Fragment de Recuperar password, se muestra cuando el usuario presiona "Olvidaste tu contraseña?.
 * La interfaz solicita el correo electrónico para enviar un código de recuperación.
 * Fecha: 02/05/2016
 */
public class RecuperarPasswordFragment extends Fragment implements View.OnClickListener {
    private EditText correoEt;
    private Button recuperarPasswordBtn;
    private ProgressDialog progressDialog;
    private SharedPreferences prefs;
    private UsuarioAPI usuarioAPI;
    private ImageButton btnBack;


    /**
     * Inicialización de las preferencias.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        usuarioAPI = ((MyApplication) getActivity().getApplication()).getRetrofitInstance().create(UsuarioAPI.class);
    }

    /**
     * Método para hacer el inflate de la vista, se declaran también los elementos visuales.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recuperar_pass, container, false);

        //Declaración de los elementos visuales.
        recuperarPasswordBtn = (Button) v.findViewById(R.id.btn_recuperar_password);
        correoEt = (EditText) v.findViewById(R.id.et_correo);
        btnBack = (ImageButton) v.findViewById(R.id.btn_back);

        btnBack.setOnClickListener((View) -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            getActivity().onBackPressed();
        });

        recuperarPasswordBtn.setOnClickListener(this);

        EditTextValidations.removeErrorTyping(correoEt);

        return v;
    }


    /*
    Se ejecuta la tarea para enviar el código.
     */
    @Override
    public void onClick(View view) {
        boolean campoVacio = EditTextValidations.esCampoVacio(correoEt);
        boolean esEmailValido = EditTextValidations.esEmailValido(correoEt);

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (!campoVacio && esEmailValido) {
            String correo = correoEt.getText().toString();
            progressDialog = ProgressDialog.show(getActivity(), getString(R.string.espera), getString(R.string.verificando), true);
            Call<Response<Boolean>> call = usuarioAPI.recuperarPassword(correo);
            call.enqueue(new Callback<Response<Boolean>>() {
                @Override
                public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Response<Boolean> resp = response.body();
                    if (resp.success) {
                        OKDialog.showOKDialog(getActivity(), getString(R.string.correo_enviado), getString(R.string.enviado));
                    } else {
                        OKDialog.showOKDialog(getActivity(), getString(R.string.error), resp.errors[0]);
                    }
                }

                @Override
                public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    OKDialog.showOKDialog(getActivity(), getString(R.string.error), getString(R.string.hubo_error));
                }
            });
        }

    }

}
