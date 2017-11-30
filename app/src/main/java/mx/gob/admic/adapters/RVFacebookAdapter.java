package mx.gob.admic.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.gob.admic.R;
import mx.gob.admic.model.FacebookPost;


/**
 * Created by codigus on 29/11/2017.
 */

public class RVFacebookAdapter extends RecyclerView.Adapter<RVFacebookAdapter.FacebookAdapterViewHolder>{
    private List<FacebookPost> posts;
    private Context context;

    public RVFacebookAdapter(Context context, List<FacebookPost> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public FacebookAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_facebook, parent, false);
        FacebookAdapterViewHolder facebookAdapterViewHolder = new FacebookAdapterViewHolder(view);
        return facebookAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(FacebookAdapterViewHolder holder, int position) {
        if (posts.get(position).getMessage() != null) {
            holder.textViewMensaje.setText(posts.get(position).getMessage());
        } else {
            holder.textViewMensaje.setText(posts.get(position).getStory());
        }

        holder.url = "https://www.facebook.com/" + posts.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void agregarPosts(List<FacebookPost> posts) {
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    class FacebookAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMensaje;
        String url;

        FacebookAdapterViewHolder(View itemView) {
            super(itemView);

            textViewMensaje = (TextView) itemView.findViewById(R.id.textview_mensaje_facebook);


            itemView.setOnClickListener((View) -> {
                Intent intentTwitter = new Intent(Intent.ACTION_VIEW);
                intentTwitter.setData(Uri.parse(url));
                context.startActivity(intentTwitter);
            });
        }


    }
}
