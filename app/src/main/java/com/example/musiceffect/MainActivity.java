package com.example.musiceffect;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.example.musiceffect.utils.Blur;
import com.example.musiceffect.helper.VisualizerHelper;
import com.example.musiceffect.widget.EffectView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private VisualizerHelper visualizerHelper;

    private ImageView mBGView;
    private EffectView mEffectView;
    private View mAlbumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBGView = findViewById(R.id.bg);
        mEffectView = findViewById(R.id.effect);
        mAlbumView = findViewById(R.id.album);

        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.test_bezier).setOnClickListener(this);
        findViewById(R.id.ancient).setOnClickListener(this);
        findViewById(R.id.reverberation).setOnClickListener(this);
        findViewById(R.id.bass).setOnClickListener(this);
        findViewById(R.id.electronic).setOnClickListener(this);
        findViewById(R.id.surround).setOnClickListener(this);
        findViewById(R.id.valve).setOnClickListener(this);
        findViewById(R.id.Lonelye).setOnClickListener(this);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mAlbumView, "rotation", 0f, 360f);
        objectAnimator.setDuration(20 * 1000);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.start();

        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.mipmap.album);
        Bitmap bitmap = drawable.getBitmap();
        Bitmap blurBitmap = Blur.fastblur(this, bitmap, 200);
        mBGView.setImageBitmap(blurBitmap);

//        ColorCaptureUtil colorCaptureUtil = new ColorCaptureUtil(new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                ArrayList<Integer> result = (ArrayList<Integer>) msg.obj;
//                mEffectView.setColor(result);
//            }
//        });
//        colorCaptureUtil.getBitmapColors(bitmap, 0, 0, blurBitmap.getWidth() , blurBitmap.getHeight() );

//        mEffectView.setAncientEffectDrawable();
        mEffectView.setAncientEffectDrawable();

        Palette.PaletteAsyncListener listener = new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int vibrant = palette.getVibrantColor(0x000000);
                int vibrantLight = palette.getLightVibrantColor(0x000000);
                int vibrantDark = palette.getDarkVibrantColor(0x000000);
                int muted = palette.getMutedColor(0x000000);
                int mutedLight = palette.getLightMutedColor(0x000000);
                int mutedDark = palette.getDarkMutedColor(0x000000);
                mEffectView.setColor(mutedLight);
                mEffectView.setColors(new int[]{mutedLight, muted, vibrantLight, vibrant});
            }
        };
        Palette.generateAsync(bitmap, listener);
//        int vibrant = palette.getVibrantColor(0x000000);
//        int vibrantLight = palette.getLightVibrantColor(0x000000);
//        int vibrantDark = palette.getDarkVibrantColor(0x000000);
//        int muted = palette.getMutedColor(0x000000);
//        int mutedLight = palette.getLightMutedColor(0x000000);
//        int mutedDark = palette.getDarkMutedColor(0x000000);

    }

    private void play() {
        if (visualizerHelper == null) {
            visualizerHelper = new VisualizerHelper();
        }
        visualizerHelper.init(this, new VisualizerHelper.DataCallback() {
            @Override
            public void onCall(byte[] data) {
                mEffectView.setData(data);
            }
        });

    }

    @Override
    public void onClick(View v) {
        mAlbumView.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.play:
                play();
                break;
            case R.id.test_bezier:
                mEffectView.setAncientEffectDrawable3();
                mAlbumView.setVisibility(View.GONE);
                break;
            case R.id.ancient:
                mEffectView.setAncientEffectDrawable();
                break;
            case R.id.reverberation:
                mEffectView.setReverberationEffectDrawable();
                break;
            case R.id.bass:
                mEffectView.setBlastBassEffectDrawable();
                break;
            case R.id.electronic:
                mEffectView.setElectronicEffectDrawable();
                break;
            case R.id.surround:
                mEffectView.setSurroundEffectDrawable();
                break;
            case R.id.valve:
                mEffectView.setValveEffectDrawable();
                break;
            case R.id.Lonelye:
                mEffectView.setLonelyEffectDrawable();
                break;
        }
    }
}
