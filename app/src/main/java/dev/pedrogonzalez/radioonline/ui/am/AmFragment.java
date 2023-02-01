package dev.pedrogonzalez.radioonline.ui.am;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import dev.pedrogonzalez.radioonline.R;

public class AmFragment extends Fragment {

    private MediaPlayer mPlayer;
    private ImageButton playPause;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_am, container, false);

        playPause = vista.findViewById(R.id.playpause);
        initializeMediaPlayer();
        prepareMediaAm();

        return vista;

    }
    private void initializeMediaPlayer(){
        mPlayer = new MediaPlayer();
        playPause.findViewById(R.id.playpause);

        playPause.setOnClickListener(view -> {
            if (mPlayer.isPlaying())
            {
                mPlayer.pause();
                playPause.setImageResource(R.drawable.ic_play);
            }else {
                mPlayer.start();
                playPause.setImageResource(R.drawable.ic_pause);
            }
        });
    }


    public void prepareMediaAm(){
        try {
            mPlayer.reset();
            mPlayer.setDataSource("http://201.217.50.222:8085/920");
            mPlayer.setVolume(85,100);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(MediaPlayer::start);
            mPlayer.setOnBufferingUpdateListener((mp, percent) -> {
                double ratio = percent / 100.0;
                int bufferingLevel = (int)(mp.getDuration() * ratio);
                Log.i("buffering", "Upload Streaming" + bufferingLevel);
            });
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