package com.ad.caraudiotrack;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends Activity {
    private Button mPlayBtn;
    private Button mPauseBtn;

    private byte[] mAudioData;
    private AudioTrack mAudioTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayBtn = findViewById(R.id.play);
        mPauseBtn = findViewById(R.id.pause);
        mPlayBtn.setEnabled(false);

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayBtn.setEnabled(false);
                if(mAudioTrack != null){
                    mAudioTrack.play();
                }else{
//                    mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
//                            mAudioData.length, AudioTrack.MODE_STATIC);

                    AudioAttributes.Builder attr = (new AudioAttributes.Builder())
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                            .setUsage(AudioAttributes.USAGE_MEDIA);
                    AudioFormat.Builder format = (new AudioFormat.Builder())
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(8000)
                            .setChannelMask( AudioFormat.CHANNEL_OUT_MONO);

                    mAudioTrack = new AudioTrack(attr.build(), format.build(), mAudioData.length, AudioTrack.MODE_STATIC,AudioManager.AUDIO_SESSION_ID_GENERATE);

                    mAudioTrack.write(mAudioData, 0, mAudioData.length);
                    mAudioTrack.play();
                }
            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAudioTrack != null){
                    mAudioTrack.pause();
                }

                //releaseAudioTrack();
                mPlayBtn.setEnabled(true);
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = getResources().openRawResource(R.raw.bingyu);
                    try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
                        for (int b; (b = in.read()) != -1;) {
                            out.write(b);
                        }
                        mAudioData = out.toByteArray();
                    } finally {
                        in.close();
                    }
                } catch (IOException e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                mPlayBtn.setEnabled(true);
            }
        }.execute();
    }

    private void releaseAudioTrack() {
        if (mAudioTrack != null) {
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //releaseAudioTrack();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseAudioTrack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseAudioTrack();
    }
}