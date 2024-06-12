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
    private boolean isBuffering = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_am, container, false);

        playPause = vista.findViewById(R.id.playpause);

        initializeMediaPlayer();
        prepareMediaAm();

        return vista;
    }

    private void initializeMediaPlayer() {
        mPlayer = new MediaPlayer();

        playPause.setOnClickListener(view -> {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                playPause.setImageResource(R.drawable.ic_play);
            } else {
                mPlayer.start();
                playPause.setImageResource(R.drawable.ic_pause);
            }
        });
    }

    public void prepareMediaAm() {
        try {
            mPlayer.reset();
            mPlayer.setDataSource("http://audio.radionacional.gov.py/920");
            mPlayer.setVolume(0.85f, 1.0f);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mPlayer.setOnPreparedListener(mp -> {
                mp.start();
                playPause.setImageResource(R.drawable.ic_pause);
            });

            mPlayer.setOnBufferingUpdateListener((mp, percent) -> {
                if (percent < 100) {
                    isBuffering = true;
                    Log.i("buffering", "Buffering: " + percent + "%");
                } else {
                    isBuffering = false;
                    Log.i("buffering", "Buffering completed: " + percent + "%");
                }
            });

            mPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e("MediaPlayer Error", "Error: " + what + ", " + extra);
                playPause.setImageResource(R.drawable.ic_play);
                return true;
            });

            mPlayer.setOnCompletionListener(mp -> {
                // Cuando la reproducción se completa, intentamos reiniciar el streaming
                prepareMediaAm();
            });

            mPlayer.prepareAsync();
        } catch (Exception ex) {
            Log.e("MediaPlayer Error", "Error preparing MediaPlayer", ex);
            playPause.setImageResource(R.drawable.ic_play);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPlayer != null) {
            mPlayer.release();
        }
    }
}
