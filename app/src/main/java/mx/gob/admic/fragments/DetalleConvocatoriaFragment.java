package mx.gob.admic.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import mx.gob.admic.R;
import mx.gob.admic.adapters.RVDocumentoAdapter;
import mx.gob.admic.api.ConvocatoriaAPI;
import mx.gob.admic.api.Response;
import mx.gob.admic.application.MyApplication;
import mx.gob.admic.model.Convocatoria;
import mx.gob.admic.model.Documento;
import mx.gob.admic.sesion.Sesion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by esva on 19/04/17.
 */

public class DetalleConvocatoriaFragment extends Fragment {
    private static String ID_CONVOCATORIA = "id_convocatoria";
    private Convocatoria convocatoria;
    private ImageView imgConvocatoria;
    private TextView tvDescripcionConvocatoria;
    private TextView tvFechaInicioConvocatoria;
    private TextView tvFechaCierreConvocatoria;
    private TextView tvEmptyDocumentos;
    private RecyclerView rvDocumentosConvocatoria;
    private TextView textViewURLConvocatoria;
    private ArrayList<Documento> documentos;
    private RVDocumentoAdapter adapter;
    private Context context;
    private Realm realm;
    public static Button btnQuieroMasInformacion;
    private ConvocatoriaAPI convocatoriaAPI;
    private Retrofit retrofit;
    private final static String $ERROR_MENSAJE = "Fallo en enviar o ya se encuentra inscrito";
    public final static String $MENSAJE_ENVIADO = "Gracias por interesarte en la convocatoria, en breve te llegará un correo electrónico con más información.";

    public static DetalleConvocatoriaFragment newInstance(int idConvocatoria) {
        DetalleConvocatoriaFragment detalleConvocatoriaFragment = new DetalleConvocatoriaFragment();

        //Guarda todos los datos del fragment anterior en una variable Bundle
        Bundle args = new Bundle();
        args.putInt(ID_CONVOCATORIA, idConvocatoria);
        detalleConvocatoriaFragment.setArguments(args);

        return detalleConvocatoriaFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        realm = MyApplication.getRealmInstance();
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        convocatoriaAPI = retrofit.create(ConvocatoriaAPI.class);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detalle_convocatoria, container, false);
        convocatoria = realm.where(Convocatoria.class).equalTo("idConvocatoria", getArguments().getInt(ID_CONVOCATORIA)).findFirst();

        imgConvocatoria = (ImageView) v.findViewById(R.id.img_convocatoria);
        tvDescripcionConvocatoria = (TextView) v.findViewById(R.id.tv_descripcion_convocatoria);
        tvFechaInicioConvocatoria = (TextView) v.findViewById(R.id.tv_fecha_inicio_convocatoria);
        tvFechaCierreConvocatoria = (TextView) v.findViewById(R.id.tv_fecha_cierre_convocatoria);
        tvEmptyDocumentos = (TextView) v.findViewById(R.id.tv_empty_documentos);
        rvDocumentosConvocatoria = (RecyclerView) v.findViewById(R.id.rv_documentos_convocatoria);
        textViewURLConvocatoria = (TextView) v.findViewById(R.id.textview_url_convocatoria);
        btnQuieroMasInformacion = (Button) v.findViewById(R.id.btn_quiero_mas_informacion);
        adapter = new RVDocumentoAdapter(getActivity(), convocatoria.getDocumentos());

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);

        rvDocumentosConvocatoria.setLayoutManager(llm);

        Picasso.with(context)
                .load(convocatoria.getRutaImagen())
                .into(imgConvocatoria);

        tvDescripcionConvocatoria.setText(convocatoria.getDescripcion());


        tvFechaInicioConvocatoria.setText("Fecha inicio: " + getFechaCast(convocatoria.getFechaInicio()));
        tvFechaCierreConvocatoria.setText("Fecha cierre: " + getFechaCast(convocatoria.getFechaCierre()));
        rvDocumentosConvocatoria.setAdapter(adapter);

        if( convocatoria.getDocumentos().size() == 0) {
            rvDocumentosConvocatoria.setVisibility(View.GONE);
            tvEmptyDocumentos.setVisibility(View.VISIBLE);
        }


        btnQuieroMasInformacion.setOnClickListener((View) -> {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Enviando correo");
            progressDialog.setMessage("Espera mientras enviamos un correo a tu cuenta registrada");
            progressDialog.show();

            Call<Response<Boolean>> call = convocatoriaAPI.enviarCorreo(Sesion.getUsuario().getApiToken(), convocatoria.getIdConvocatoria());

            call.enqueue(new Callback<Response<Boolean>>() {
                @Override
                public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                    Snackbar.make(getView(), $ERROR_MENSAJE, 7000).show();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                    Snackbar.make(getView(), $MENSAJE_ENVIADO, 7000).show();
                    btnQuieroMasInformacion.setEnabled(false);
                    btnQuieroMasInformacion.setBackgroundResource(R.drawable.bordered_button_gray);
                    MyApplication.contadorCorreosConvocatorias.start();
                    progressDialog.dismiss();
                }
            });
        });

        textViewURLConvocatoria.setOnClickListener(v1 -> {
            if (convocatoria.getUrlConvocatoria() != null) {
                enlace(getValidURL(convocatoria.getUrlConvocatoria()));
            } else {
                Snackbar.make(getView(), "Vaya!, parece que esta convocatoria no cuenta con un enlace", Snackbar.LENGTH_LONG).show();
            }

        });

        return v;
    }

    private String getFechaCast(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat miFormato = new SimpleDateFormat("dd/MM/yyyy");

        try {
            String reformato = miFormato.format(formato.parse(fecha));
            return reformato;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(convocatoria.getTitulo());
    }

    public void enlace(String link){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    private String getValidURL(String url) {
        String returnURL;
        if (url.startsWith("www")) {
            returnURL = "http://" + url;
            return returnURL;
        } else {
            return url;
        }
    }

}
