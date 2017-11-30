package mx.gob.admic.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mx.gob.admic.R;
import mx.gob.admic.fragments.AyudaFragment;
import mx.gob.admic.model.YoutubeVideo;

public class RVYoutubeAdapter extends RecyclerView.Adapter<RVYoutubeAdapter.YoutubeAdapterViewHolder> {
    private List<YoutubeVideo> videos;
    private Context context;

    public RVYoutubeAdapter(Context context, List<YoutubeVideo> videos) {
        this.context = context;
        this.videos = videos;
    }

    @Override
    public YoutubeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_youtube, parent, false);
        YoutubeAdapterViewHolder youtubeAdapterViewHolder = new YoutubeAdapterViewHolder(itemView);
        return youtubeAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(YoutubeAdapterViewHolder holder, int position) {
        Picasso.with(context).load(videos.get(position).getSnippet().getThumbnails().getMedium().getUrl()).into(holder.imageViewThumbnail);
        holder.textViewTituloVideo.setText(videos.get(position).getSnippet().getTitle());
        holder.textViewDescripcionVideo.setText(videos.get(position).getSnippet().getDescription());
        holder.idVideo = videos.get(position).getSnippet().getResourceId().getVideoId();
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class YoutubeAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewTituloVideo;
        TextView textViewDescripcionVideo;
        String idVideo;

        YoutubeAdapterViewHolder(View itemView) {
            super(itemView);

            imageViewThumbnail = (ImageView) itemView.findViewById(R.id.imageview_youtube_thumbnail);
            textViewTituloVideo = (TextView) itemView.findViewById(R.id.textview_titulo_video);
            textViewDescripcionVideo = (TextView) itemView.findViewById(R.id.textview_descripción_video);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    try {
                        Fragment fragment = new AyudaFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("idVideo", idVideo);
                        fragment.setArguments(bundle);
                        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.segunda_fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }
}
