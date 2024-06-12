package dev.pedrogonzalez.radioonline.ui.sanpedro;

import android.media.AudioAttributes;
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

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import dev.pedrogonzalez.radioonline.R;

public class SanPedroFragment extends Fragment {

    private MediaPlayer mPlayer;
    private ImageButton playPause;
    private boolean isBuffering = false;
    private static final int RECONNECT_DELAY = 5000; // 5 seconds delay for reconnection
    private static final String STREAM_URL = "http://audio2.radionacional.gov.py/sanpedro";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_san_pedro, container, false);

        playPause = vista.findViewById(R.id.playpause);

        configureCookieManager();  // Configurar el CookieManager antes de cualquier solicitud HTTP
        initializeMediaPlayer();
        prepareMediaSanPedro();

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

    private void configureCookieManager() {
        // Configurar un CookieManager si no existe uno
        if (CookieHandler.getDefault() == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);
            Log.i("CookieHandler", "CookieManager configured.");
        } else {
            Log.i("CookieHandler", "Existing CookieHandler: " + CookieHandler.getDefault().toString());
        }
    }

    public void prepareMediaSanPedro() {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(STREAM_URL);
            mPlayer.setVolume(0.85f, 1.0f);
            mPlayer.setAudioStreamType(AudioAttributes.CONTENT_TYPE_MUSIC);

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
                handleConnectionError();
                return true;
            });

            mPlayer.setOnCompletionListener(mp -> {
                // Cuando la reproducción se completa, intentamos reiniciar el streaming
                prepareMediaSanPedro();
            });

            mPlayer.prepareAsync();
        } catch (IOException ex) {
            Log.e("MediaPlayer Error", "IOException preparing MediaPlayer", ex);
            playPause.setImageResource(R.drawable.ic_play);
        } catch (IllegalArgumentException ex) {
            Log.e("MediaPlayer Error", "IllegalArgumentException preparing MediaPlayer", ex);
            playPause.setImageResource(R.drawable.ic_play);
        } catch (SecurityException ex) {
            Log.e("MediaPlayer Error", "SecurityException preparing MediaPlayer", ex);
            playPause.setImageResource(R.drawable.ic_play);
        } catch (IllegalStateException ex) {
            Log.e("MediaPlayer Error", "IllegalStateException preparing MediaPlayer", ex);
            playPause.setImageResource(R.drawable.ic_play);
        }
    }

    private void handleConnectionError() {
        // Mostrar el icono de reproducción
        playPause.setImageResource(R.drawable.ic_play);

        // Esperar unos segundos antes de intentar reconectar
        playPause.postDelayed(() -> {
            if (!mPlayer.isPlaying()) {
                prepareMediaSanPedro();
            }
        }, RECONNECT_DELAY);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPlayer != null) {
            mPlayer.release();
        }
    }
}
