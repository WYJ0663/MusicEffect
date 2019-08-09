package com.example.musiceffect.helper;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.example.musiceffect.R;

public class VisualizerHelper {


    private MediaPlayer mediaPlayer;//防止内存被回收
    private Visualizer visualizer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void init(Context context, final DataCallback dataCallback) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (visualizer != null) {
            visualizer.release();
        }
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.ytkl);
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e("yijunwu", "播放出错！");
                    return false;
                }
            });

            visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
            int captureSize = Visualizer.getCaptureSizeRange()[1];
            int captureRate = Visualizer.getMaxCaptureRate() * 3 / 4;
            visualizer.setCaptureSize(captureSize);
            visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                @Override
                public void onWaveFormDataCapture(Visualizer visualizer, final byte[] waveform, int samplingRate) {
//            visualConverter.converter(waveform);
//                    if (dataCallback != null) {
//                        dataCallback.onCall(waveform);
//                    }
                }

                @Override
                public void onFftDataCapture(Visualizer visualizer, final byte[] fft, int samplingRate) {
                    if (dataCallback != null) {
                        dataCallback.onCall(fft);
                    }
                }
            }, captureRate, true, true);
            visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
            visualizer.setEnabled(true);
        } catch (Exception e) {
            Log.e("VisualizerHelper", "请检查录音权限");
        }
    }

    public static interface DataCallback {
        void onCall(byte[] data);
    }


}
