package mx.gob.admic.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import mx.gob.admic.R;
import mx.gob.admic.adapters.RVDocumentoEventoAdapter;
import mx.gob.admic.api.EventoAPI;
import mx.gob.admic.api.Response;
import mx.gob.admic.application.MyApplication;
import mx.gob.admic.model.Evento;
import mx.gob.admic.model.EventoResponse;
import mx.gob.admic.sesion.Sesion;
import mx.gob.admic.utils.DateUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class DetalleEventoFragment extends Fragment implements OnMapReadyCallback {
    private static String ID_EVENTO = "id_evento";
    private Evento evento;
    private MapFragment mapaEvento;
    private TextView tvNombreEvento;
    private TextView tvDireccionEvento;
    private TextView tvDescripcionEvento;
    private TextView tvFechaEvento;
    private Button botonEstoyEnEvento;
    public static Button botonMeInteresa;
    private TextView textViewEventoCaducado;
    private TextView textViewYaHasSidoRegistrado;
    private Realm realm;
    private LocationManager locationManager;
    private static final int PERMISSION_REQUEST_CODE = 321;
    private double latitud;
    private double longitud;
    private Retrofit retrofit;
    private EventoAPI eventoAPI;
    private RecyclerView recyclerViewDocumentos;
    private RVDocumentoEventoAdapter adapter;
    private TextView textViewNoHayDocumentos;

    private Criteria criteria;

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String SYSTEM_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final String SYSTEM_DATE_FORMAT_24_HRS = "yyyy-MM-dd HH:mm:ss";

    public static DetalleEventoFragment newInstance(int idEvento) {
        DetalleEventoFragment detalleEventoFragment = new DetalleEventoFragment();
        Bundle args = new Bundle();
        args.putInt(ID_EVENTO, idEvento);
        detalleEventoFragment.setArguments(args);
        return detalleEventoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = MyApplication.getRealmInstance();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        eventoAPI = retrofit.create(EventoAPI.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_detalle_evento, container, false);

        evento = realm.where(Evento.class).equalTo("idEvento", getArguments().getInt(ID_EVENTO)).findFirst();

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        mapaEvento = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapa_evento);
        mapaEvento.getMapAsync(this);

        tvNombreEvento = (TextView) v.findViewById(R.id.tv_nombre_evento);
        tvDireccionEvento = (TextView) v.findViewById(R.id.tv_direccion_evento);
        tvDescripcionEvento = (TextView) v.findViewById(R.id.tv_descripcion_evento);
        tvFechaEvento = (TextView) v.findViewById(R.id.tv_fechas_evento);
        botonEstoyEnEvento = (Button) v.findViewById(R.id.boton_estoy_en_el_evento);
        botonMeInteresa = (Button) v.findViewById(R.id.boton_me_interesa);
        textViewEventoCaducado = (TextView) v.findViewById(R.id.textview_evento_caducado);
        textViewYaHasSidoRegistrado = (TextView) v.findViewById(R.id.textview_registrado);
        recyclerViewDocumentos = (RecyclerView) v.findViewById(R.id.rv_documentos_evento);
        textViewNoHayDocumentos = (TextView) v.findViewById(R.id.tv_empty_documentos);

        tvNombreEvento.setText(evento.getTitulo());
        tvDireccionEvento.setText(evento.getDireccion());
        tvDescripcionEvento.setText(evento.getDescripcion());

        String fechaEvento = getFechaCast(evento.getFechaInicio()) +
                "-" +
                getFechaCast(evento.getFechaFin());

        tvFechaEvento.setText(fechaEvento);

        if (!usuarioRegistrado()) checkAsist();

        botonEstoyEnEvento.setOnClickListener((View) -> {
            ProgressDialog dialog = ProgressDialog.show(getContext(),
                    "Cargando",
                    "Obteniendo tu localización", true, true);

            Call<Response<EventoResponse>> call = eventoAPI.marcarEvento(evento.getIdEvento(), Sesion.getUsuario().getApiToken(), getLatitud(), getLongitud());

            call.enqueue(new Callback<Response<EventoResponse>>() {
                @Override
                public void onResponse(Call<Response<EventoResponse>> call, retrofit2.Response<Response<EventoResponse>> response) {
                    dialog.dismiss();

                    if (response.body() != null) {
                        if (response.body().errors.length == 0) {
                            int puntos = response.body().data.getPuntosOtorgados();
                            int puntosUsuario = Integer.parseInt(Sesion.getUsuario().getPuntaje());
                            String puntosFinal = String.valueOf(puntos + puntosUsuario);
                            Sesion.getUsuario().setPuntaje(puntosFinal);

                            Snackbar.make(getView(), "Registrado", 10000).show();

                            realm.beginTransaction();
                            evento.setEstaRegistrado(true);
                            realm.commitTransaction();

                        } else if (response.body().errors[0].equals("No te encuentras en el rango del evento")) {
                            Snackbar.make(getView(), "No te encuentras en el rango del evento", 10000).show();
                        } else if (response.body().errors[0].equals("Ya has sido registrado")) {
                            Snackbar.make(getView(), "Ya has sido registrado", 10000).show();

                            realm.beginTransaction();
                            evento.setEstaRegistrado(true);
                            realm.commitTransaction();
                        }
                    } else {
                        Snackbar.make(getView(), "Ops! parece que ocurrio un error, intenta más tarde", 10000).show();
                    }
                }

                @Override
                public void onFailure(Call<Response<EventoResponse>> call, Throwable t) {
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Error de conexión");
                    builder.show();
                }
            });

        });

        botonMeInteresa.setOnClickListener((View) -> {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Enviando correo");
            progressDialog.setMessage("Espera mientras enviamos un correo a tu cuenta registrada");
            progressDialog.show();

            Call<Response<Boolean>> enviarCorreo = eventoAPI.enviarCorreo(Sesion.getUsuario().getApiToken(), evento.getIdEvento());

            enviarCorreo.enqueue(new Callback<Response<Boolean>>() {
                @Override
                public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                    Snackbar.make(getView(), "Fallo en enviar o ya se encuentra inscrito", 7000).show();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                    Snackbar.make(getView(), "Gracias por estar interesado en el evento, en breve te llegará un correo electrónico con más información.", 7000).show();
                    botonMeInteresa.setBackgroundResource(R.drawable.bordered_button_gray);
                    botonMeInteresa.setEnabled(false);
                    MyApplication.contadorCorreosEventos.start();
                    progressDialog.dismiss();
                }
            });

        });



        if (evento.getDocumentosEvento().size() != 0) {
            adapter = new RVDocumentoEventoAdapter(getContext(), evento.getDocumentosEvento());
            recyclerViewDocumentos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewDocumentos.setAdapter(adapter);
        } else {
            recyclerViewDocumentos.setVisibility(View.GONE);
            textViewNoHayDocumentos.setVisibility(View.VISIBLE);
        }

        return v;
    }

    public void checkAsist() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateInStringbegin = evento.getFechaInicio();
        String dateInStringend = evento.getFechaFin();
        String dateInStringToday = formatter.format(new Date());

        try {
            Date fechainicio = formatter.parse(dateInStringbegin);
            Date fechafin = formatter.parse(dateInStringend);
            Date today = formatter.parse(dateInStringToday);

            long timeStampBegin = fechainicio.getTime();
            long timeStampEnd = fechafin.getTime();
            long timeStampToday = today.getTime();

            boolean antesDeFecha = timeStampBegin > timeStampToday;
            boolean enFecha = timeStampBegin < timeStampToday && timeStampToday < timeStampEnd;
            boolean despuesDeFecha = timeStampEnd < timeStampToday;

            System.err.println(antesDeFecha);
            System.err.println(timeStampBegin < timeStampToday);
            System.err.println(despuesDeFecha);


            if (antesDeFecha) {
                botonMeInteresa.setVisibility(View.VISIBLE);
            } else if (despuesDeFecha) {
                textViewEventoCaducado.setVisibility(View.VISIBLE);
            } else if (enFecha) {
                botonEstoyEnEvento.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pedirPermisos();
                } else {
                    try {
                        locationManager.requestSingleUpdate(criteria, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                setLatitud(location.getLatitude());
                                setLongitud(location.getLongitude());
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        }, null);

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        float zoomLevel = (float) 16.0;
        LatLng coordenadas = new LatLng(evento.getLatitud(), evento.getLongitud());
        googleMap.addMarker(new MarkerOptions().position(coordenadas).title(evento.getTitulo()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, zoomLevel));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapaEvento = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapa_evento);
        if (mapaEvento != null)
            getActivity().getFragmentManager().beginTransaction().remove(mapaEvento).commit();
    }

    private void pedirPermisos() {
        String[] permisos = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permisos, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permitir = true;

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int res : grantResults) {
                    permitir = permitir && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                permitir = false;
                break;
        }


        if (permitir) {
            try {
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        setLatitud(location.getLatitude());
                        setLongitud(location.getLongitude());
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                }, null);

            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Snackbar.make(getActivity().findViewById(R.id.segunda_fragment_container), "Permiso denegado", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    private void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    private void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    private double getLatitud() {
        return this.latitud;
    }

    private double getLongitud() {
        return this.longitud;
    }

    private boolean usuarioRegistrado() {
        if (evento.getEstaRegistrado()) {
            botonEstoyEnEvento.setVisibility(View.GONE);
            botonMeInteresa.setVisibility(View.GONE);
            textViewEventoCaducado.setVisibility(View.GONE);
            textViewYaHasSidoRegistrado.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    public static String getFechaCast(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat(SYSTEM_DATE_FORMAT);
        SimpleDateFormat miFormato = new SimpleDateFormat(DATE_FORMAT);

        try {
            String reformato = miFormato.format(formato.parse(fecha));
            return reformato;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
