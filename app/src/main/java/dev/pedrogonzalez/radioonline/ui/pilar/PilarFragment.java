package dev.pedrogonzalez.radioonline.ui.pilar;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import dev.pedrogonzalez.radioonline.R;


public class PilarFragment extends Fragment {

    private MediaPlayer mPlayer;
    private ImageButton playPause;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        onCreate(savedInstanceState);
        View vista =  inflater.inflate(R.layout.fragment_pilar, container, false);

        playPause = vista.findViewById(R.id.playpause);

        initializeMediaPlayer();
        prepareMediaPilar();

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

    public void prepareMediaPilar(){
        try {
            mPlayer.reset();
            mPlayer.setDataSource("http://201.217.50.222:8085/700am");
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