package mx.gob.admic.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.gob.admic.R;
import mx.gob.admic.activities.SegundaActivity;
import mx.gob.admic.adapters.RVMessagesAdapter;
import mx.gob.admic.api.ChatAPI;
import mx.gob.admic.api.Response;
import mx.gob.admic.application.MyApplication;
import mx.gob.admic.model.DatosMensajes;
import mx.gob.admic.model.Mensaje;
import mx.gob.admic.sesion.Sesion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by code on 9/02/17.
 */

public class ChatFragment extends CustomFragment {
    private RecyclerView recyclerViewMessages;
    private ImageButton buttonSend;
    private EditText editTextMessage;
    private RVMessagesAdapter adapter;
    private List<Mensaje> mensajes;
    private ChatAPI chatAPI;
    private Retrofit retrofit;
    private int PAGE = 1;
    private LinearLayoutManager llm;
    private IntentFilter intentFilter;
    public static ChatFragment chat;

    private static final String ERROR_MENSAJES = "Vaya!, parece que tenemos un problema...\n" +
                                                        "Intenta más tarde o verifica tu conexión a internet";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        chatAPI = retrofit.create(ChatAPI.class);

        intentFilter = new IntentFilter("mx.gob.admic.MENSAJE_RECIBIDO");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mensajeRecibido, intentFilter);

        chat = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerViewMessages = (RecyclerView) v.findViewById(R.id.rv_messages);
        buttonSend = (ImageButton) v.findViewById(R.id.button_send);
        editTextMessage = (EditText) v.findViewById(R.id.edittext_message);
        mensajes = new ArrayList<>();
        adapter = new RVMessagesAdapter(getContext(), mensajes);

        llm = new LinearLayoutManager(getActivity());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(false);

        recyclerViewMessages.setLayoutManager(llm);
        recyclerViewMessages.setAdapter(adapter);

        primeraLlamada();

        buttonSend.setOnClickListener((View) -> {
                if (editTextMessage.getText().toString().length() != 0) {
                    mensajes.add(0, new Mensaje(editTextMessage.getText().toString(), 1));

                    adapter.notifyData(mensajes);

                    Call<Response<Boolean>> call = chatAPI.enviarMensaje(Sesion.getUsuario().getApiToken(), editTextMessage.getText().toString());

                    call.enqueue(new Callback<Response<Boolean>>() {
                        @Override
                        public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {

                        }

                        @Override
                        public void onFailure(Call<Response<Boolean>> call, Throwable t) {

                        }
                    });

                    editTextMessage.getText().clear();

                }
        });

        recyclerViewMessages.addOnScrollListener(new SetOnScrollListener());

        return v;
    }

    private void primeraLlamada() {
        Call<Response<DatosMensajes>> call = chatAPI.obtenerMensajes(Sesion.getUsuario().getApiToken(), PAGE);

        call.enqueue(new Callback<Response<DatosMensajes>>() {
            @Override
            public void onResponse(Call<Response<DatosMensajes>> call, retrofit2.Response<Response<DatosMensajes>> response) {
                PAGE++;
                if (response.body() != null) {
                    mensajes = response.body().data.getData();
                    /*llm = new LinearLayoutManager(getActivity());
                    llm.setReverseLayout(true);
                    llm.setStackFromEnd(false);
                    adapter = new RVMessagesAdapter(getContext(), mensajes);
                    recyclerViewMessages.setLayoutManager(llm);
                    recyclerViewMessages.setAdapter(adapter);*/
                    adapter.notifyData(mensajes);
                }
            }

            @Override
            public void onFailure(Call<Response<DatosMensajes>> call, Throwable t) {
                try {
                    primeraLlamada();
                } catch (Exception exception) {
                    AlertDialog.Builder mensajeError = new AlertDialog.Builder(getContext());
                    mensajeError.setMessage(ERROR_MENSAJES);
                    mensajeError.show();
                }
            }
        });
    }

    private void generarLlamada() {
        Call<Response<DatosMensajes>> call = chatAPI.obtenerMensajes(Sesion.getUsuario().getApiToken(), PAGE);

        call.enqueue(new Callback<Response<DatosMensajes>>() {
            @Override
            public void onResponse(Call<Response<DatosMensajes>> call, retrofit2.Response<Response<DatosMensajes>> response) {
                PAGE++;
                List<Mensaje> auxiliar = response.body().data.getData();

                //if (response.body().data.getLastPage() < PAGE) {
                    adapter.agregarMensajes(auxiliar);

                //}
            }

            @Override
            public void onFailure(Call<Response<DatosMensajes>> call, Throwable t) {
                try {
                    generarLlamada();
                } catch (Exception exception) {
                    AlertDialog.Builder mensajeError = new AlertDialog.Builder(getContext());
                    mensajeError.setMessage(ERROR_MENSAJES);
                    mensajeError.show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver mensajeRecibido = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Mensaje mensaje = new Gson().fromJson(intent.getExtras().getString("mensaje"), Mensaje.class);
            mensajes.add(0, mensaje);
            adapter.notifyDataSetChanged();
        }
    };

    public static boolean estaEnChat() {
         if (chat != null) {
            if (chat.isVisible() && !SegundaActivity.arePaused) {
                return true;
            }
        }
        return false;
    }

    private class SetOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int visibleItemCount = llm.getChildCount();
            int totalItemCount = llm.getItemCount();
            int firstVisibleItemPosition = llm.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                generarLlamada();
            }

        }
    }

}
