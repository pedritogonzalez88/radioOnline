package dev.pedrogonzalez.radioonline.ui.sanpedro;


import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import dev.pedrogonzalez.radioonline.R;

public class SanPedroFragment extends Fragment {

    private MediaPlayer mPlayer;
    private ImageButton playPause;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_san_pedro, container, false);

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
        prepareMediaSanPedro();

        return vista;
    }

    public void prepareMediaSanPedro(){
        try {
            mPlayer.setDataSource("http://201.217.50.222:8084/sanpedro");
            mPlayer.setVolume(85,100);
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayer.start();
                }
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