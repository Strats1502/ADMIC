package mx.gob.admic.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import de.hdodenhof.circleimageview.CircleImageView;
import mx.gob.admic.R;
import mx.gob.admic.activities.ReproductorActivity;
import mx.gob.admic.fragments.AyudaFragment;
import mx.gob.admic.model.YoutubeVideo;

public class RVYoutubeAdapter extends RecyclerView.Adapter<RVYoutubeAdapter.YoutubeAdapterViewHolder> {
    private List<YoutubeVideo> videos;
    private Context context;
    private String channelTitle;
    private String channelImageURL;

    public RVYoutubeAdapter(Context context, List<YoutubeVideo> videos, String channelTitle, String channelImageURL) {
        this.context = context;
        this.videos = videos;
        this.channelTitle = channelTitle;
        this.channelImageURL = channelImageURL;
    }

    @Override
    public YoutubeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_youtube, parent, false);
        YoutubeAdapterViewHolder youtubeAdapterViewHolder = new YoutubeAdapterViewHolder(itemView);
        return youtubeAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(YoutubeAdapterViewHolder holder, int position) {
        Picasso.with(context).load(videos.get(position).getSnippet().getThumbnails().getHigh().getUrl()).into(holder.imageViewThumbnail);
        Picasso.with(context).load(channelImageURL).into(holder.circleImageViewImagenUsuario);
        holder.textViewTituloVideo.setText(videos.get(position).getSnippet().getTitle());
        holder.textViewTituloCanal.setText(channelTitle);
        holder.textViewDescripcionVideo.setText(videos.get(position).getSnippet().getDescription());
        holder.idVideo = videos.get(position).getSnippet().getResourceId().getVideoId();
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class YoutubeAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;
        CircleImageView circleImageViewImagenUsuario;
        TextView textViewTituloVideo;
        TextView textViewTituloCanal;
        TextView textViewDescripcionVideo;
        String idVideo;

        YoutubeAdapterViewHolder(View itemView) {
            super(itemView);

            imageViewThumbnail = (ImageView) itemView.findViewById(R.id.imageview_youtube_thumbnail);
            circleImageViewImagenUsuario = (CircleImageView) itemView.findViewById(R.id.circleimageview_imagen_usuario_youtube);
            textViewTituloVideo = (TextView) itemView.findViewById(R.id.textview_titulo_video);
            textViewTituloCanal = (TextView) itemView.findViewById(R.id.textview_titulo_canal);
            textViewDescripcionVideo = (TextView) itemView.findViewById(R.id.textview_descripción_video);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(context, ReproductorActivity.class);
                        intent.putExtra("idVideo", idVideo);
                        context.startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            });

        }

    }
}
