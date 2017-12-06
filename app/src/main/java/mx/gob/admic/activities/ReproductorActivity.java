package mx.gob.admic.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import mx.gob.admic.R;

/**
 * Created by codigus on 30/11/2017.
 */

public class ReproductorActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{
    private YouTubePlayerFragment videoView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        videoView = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.fragment_youtube_activity);

        videoView.initialize("AIzaSyDHDnSpC4e1VwrYYjQeb1sAQrr_d2U0zPY", this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        //youTubePlayer.cueVideo();
        youTubePlayer.loadVideo(getIntent().getExtras().get("idVideo").toString());
        //youTubePlayer.setShowFullscreenButton(false);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
