package dev.pedrogonzalez.radioonline.ui.fm;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import dev.pedrogonzalez.radioonline.R;


public class FmFragment extends Fragment {

    private MediaPlayer mPlayer;
    private ImageButton playPause;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View vista =  inflater.inflate(R.layout.fragment_fm, container, false);

        mPlayer = new MediaPlayer();
        playPause = vista.findViewById(R.id.playpause);

        playPause.setOnClickListener(view -> {
            if (mPlayer.isPlaying())
            {
                mPlayer.stop();
                playPause.setImageResource(R.drawable.ic_play);
            }else {
                mPlayer.start();
                playPause.setImageResource(R.drawable.ic_pause);
            }
        });
        prepareMediaFm();
        return vista;
    }

    public void prepareMediaFm(){
        try {
            mPlayer.setDataSource("http://201.217.50.222:8084/951fm");
            mPlayer.setVolume(85,100);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(MediaPlayer::start);
            mPlayer.prepareAsync();
        }catch (Exception ex)
        {
            mPlayer.setOnErrorListener((mediaPlayer, i, i1) -> false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlayer.release();
    }
}