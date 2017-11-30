package mx.gob.admic.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.gob.admic.R;
import mx.gob.admic.adapters.RVFacebookAdapter;
import mx.gob.admic.api.FacebookAPI;
import mx.gob.admic.api.ResponseFacebook;
import mx.gob.admic.application.MyApplication;
import mx.gob.admic.model.FacebookPost;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by codigus on 28/11/2017.
 */

public class FacebookFragment extends Fragment {
    private Retrofit retrofit;
    private FacebookAPI facebookAPI;

    private RecyclerView recyclerViewPosts;
    private RVFacebookAdapter adapter;
    private List<FacebookPost> posts;
    private LinearLayoutManager layoutManager;

    private String accessToken;
    private String nextPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitFacebook();
        facebookAPI = retrofit.create(FacebookAPI.class);
        posts = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook, container, false);

        recyclerViewPosts = (RecyclerView) view.findViewById(R.id.recyclerview_facebook_posts);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        accessToken = getString(R.string.facebook_app_id) + getString(R.string.fragment_facebook_pipe) + getString(R.string.facebook_app_secret);

        Call<ResponseFacebook> call = facebookAPI.getPosts(accessToken);

        call.enqueue(new Callback<ResponseFacebook>() {
            @Override
            public void onResponse(Call<ResponseFacebook> call, Response<ResponseFacebook> response) {
                posts.addAll(response.body().getData());
                adapter = new RVFacebookAdapter(getContext(), posts);
                recyclerViewPosts.setAdapter(adapter);
                recyclerViewPosts.setLayoutManager(layoutManager);

                nextPage = response.body().getPaging().getCursors().getAfter();
            }

            @Override
            public void onFailure(Call<ResponseFacebook> call, Throwable t) {
                System.err.println("-------------------------------------------------------");
                System.err.println(t.getMessage());
                System.err.println("-------------------------------------------------------");
            }
        });

        recyclerViewPosts.addOnScrollListener(new SetOnScrollListener());


        return view;
    }

    private void generarLlamada() {
        Call<ResponseFacebook> call = facebookAPI.getNextPosts(accessToken, nextPage);

        call.enqueue(new Callback<ResponseFacebook>() {
            @Override
            public void onResponse(Call<ResponseFacebook> call, Response<ResponseFacebook> response) {
                adapter.agregarPosts(response.body().getData());
            }

            @Override
            public void onFailure(Call<ResponseFacebook> call, Throwable t) {

            }
        });
    }

    private class SetOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                //progressBar.setVisibility(View.VISIBLE);
                generarLlamada();
            } else {
                //progressBar.setVisibility(View.GONE);
            }

        }
    }
}
