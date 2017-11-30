package mx.gob.admic.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import mx.gob.admic.R;
import mx.gob.admic.adapters.RVYoutubeAdapter;
import mx.gob.admic.api.Response;
import mx.gob.admic.api.ResponseYoutube;
import mx.gob.admic.api.YoutubeAPI;
import mx.gob.admic.application.MyApplication;
import mx.gob.admic.model.YoutubeChannel;
import mx.gob.admic.model.YoutubeVideo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by codigus on 28/11/2017.
 */

public class YoutubeFragment extends Fragment {
    private Retrofit retrofit;
    private YoutubeAPI youtubeAPI;

    private RecyclerView recyclerView;
    private RVYoutubeAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitYoutube();
        youtubeAPI = retrofit.create(YoutubeAPI.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_youtube, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_youtube_videos);

        Call<ResponseYoutube<List<YoutubeChannel>>> callChannel = youtubeAPI.getChannel("contentDetails", "macdemarco", getString(R.string.api_key));

        callChannel.enqueue(new Callback<ResponseYoutube<List<YoutubeChannel>>>() {
            @Override
            public void onResponse(Call<ResponseYoutube<List<YoutubeChannel>>> call, retrofit2.Response<ResponseYoutube<List<YoutubeChannel>>> response) {
                String playlistId = response.body().getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads();

                Call<ResponseYoutube<List<YoutubeVideo>>> callVideos = youtubeAPI.getVideos("snippet", 10, playlistId, getString(R.string.api_key));

                callVideos.enqueue(new Callback<ResponseYoutube<List<YoutubeVideo>>>() {
                    @Override
                    public void onResponse(Call<ResponseYoutube<List<YoutubeVideo>>> call, retrofit2.Response<ResponseYoutube<List<YoutubeVideo>>> response) {
                        adapter = new RVYoutubeAdapter(getContext(), response.body().getItems());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    }

                    @Override
                    public void onFailure(Call<ResponseYoutube<List<YoutubeVideo>>> call, Throwable t) {
                        System.err.println("----------------------------------------------------------2222222");
                        System.err.println(t.getMessage());
                        System.err.println("------------------------------------------------------------------");
                    }
                });

            }

            @Override
            public void onFailure(Call<ResponseYoutube<List<YoutubeChannel>>> call, Throwable t) {

            }
        });

        return v;
    }
}
