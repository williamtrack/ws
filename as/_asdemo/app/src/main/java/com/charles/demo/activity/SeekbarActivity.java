package com.charles.demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.charles.demo.R;

import java.util.Timer;
import java.util.TimerTask;

public class SeekbarActivity extends AppCompatActivity {


    private Button start,end,pause;
    MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    Timer timer = new Timer();;
    TimerTask timerTask;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar);

        start = (Button) findViewById(R.id.start);
        end = (Button) findViewById(R.id.end);
        pause = (Button) findViewById(R.id.pause);
        seekBar = (SeekBar) findViewById(R.id.seekBar);


        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        // mediaPlayer = MediaPlayer.create(Main3Activity.this, Uri.parse(url));
        //mediaPlayer = MediaPlayer.create(SeekbarActivity.this, R.raw.test);

        videoView = findViewById(R.id.video_view);
        videoView.setVideoPath("/sdcard/BOE/video/test.mp4");
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setLooping(true);
                mp.setVolume(100, 100);
                mediaPlayer = mp;

                //歌曲的播放时长
                seekBar.setMax(mediaPlayer.getDuration());
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("messgae","start");

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("messgae","stop");

                //设置歌曲的进度为进度条的位置
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
    }



    public void click(View view){
        int id = view.getId();
        switch(id){
            case R.id.start:
                Log.d("William", "SeekbarActivity-click: ===" );
                start();
                break;
            case R.id.end:
                stop();
                break;
            case R.id.pause:
                pause();
                break;
        }
    }
    public void start(){
        mediaPlayer.start();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //歌曲当前播放位置
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        };

        //1.等多久再执行什么
        //2.每隔多久执行一次什么
        timer.schedule(timerTask,0,100);
        start.setEnabled(false);
    }

    public void pause(){
        String pause1 = pause.getText().toString().trim();
        if("pause".equals(pause1)){
            mediaPlayer.pause();
            pause.setText("resume");
        }else{
            mediaPlayer.start();
            pause.setText("pause");
        }
    }

    public void stop(){
        if(mediaPlayer!=null){
            timer.cancel();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            start.setEnabled(true);
        }
    }

}