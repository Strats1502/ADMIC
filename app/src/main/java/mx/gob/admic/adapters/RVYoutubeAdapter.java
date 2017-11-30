package mx.gob.admic.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.util.List;

import mx.gob.admic.R;
import mx.gob.admic.model.YoutubeVideo;

/**
 * Created by codigus on 28/11/2017.
 */

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
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class YoutubeAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;

        YoutubeAdapterViewHolder(View itemView) {
            super(itemView);

            imageViewThumbnail = (ImageView) itemView.findViewById(R.id.imageview_youtube_thumbnail);
        }

    }
}
