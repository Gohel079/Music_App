package com.madhavesh.gohel.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {

    Button btn_Pause,btn_prev,btn_next;
    TextView txt_song;
    SeekBar seekBar;
    String sname;

     static MediaPlayer mediaPlayer;
     int position;
     ArrayList<File> mySong;
     Thread updateSeekBar;




    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);


        btn_next =(Button)findViewById(R.id.next);
        btn_prev =(Button)findViewById(R.id.prev);
        btn_Pause =(Button)findViewById(R.id.pause);

        seekBar=(SeekBar)findViewById(R.id.seekbar);

        txt_song =(TextView)findViewById(R.id.txtlabel);

        getSupportActionBar().setTitle("Music Player");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateSeekBar =new Thread()
        {
            @Override
            public void run()
            {
                //super.run();

                int totalDuration =  mediaPlayer.getDuration();
                int currentPosition =  0;

                while(currentPosition < totalDuration)
                {
                    try {

                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySong= (ArrayList)bundle.getParcelableArrayList("songs");

        sname = mySong.get(position).getName().toString();

        String viewSongName =  i.getStringExtra("songName");

        txt_song.setText(viewSongName);
        txt_song.setSelected(true);

            position = bundle.getInt("position",0);

        Uri u =  Uri.parse(mySong.get(position).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(),u);

        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        updateSeekBar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.yellow_dark), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.yellow_dark),PorterDuff.Mode.SRC_IN);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btn_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 seekBar.setMax(mediaPlayer.getDuration());

                 if(mediaPlayer.isPlaying())
                 {
                     btn_Pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                     mediaPlayer.pause();
                 }
                 else
                 {
                        btn_Pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                        mediaPlayer.start();
                 }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position+1)%mySong.size());
                 Uri u = Uri.parse(mySong.get(position).toString());

                 mediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                 sname = mySong.get(position).getName().toString();

                 txt_song.setText(sname);

                 mediaPlayer.start();

            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position-1) < 0 ) ? (mySong.size()-1):(position-1);

                Uri u = Uri.parse(mySong.get(position).toString());

              mediaPlayer =   MediaPlayer.create(getApplicationContext(),u);

              sname  = mySong.get(position).getName().toString();

              txt_song.setText(sname);

              mediaPlayer.start();
            }
        });
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}