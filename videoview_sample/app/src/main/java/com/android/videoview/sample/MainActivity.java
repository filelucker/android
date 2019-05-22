package com.android.videoview.sample;


import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends ActionBarActivity {

    static final String HLS_STREAMING_SAMPLE = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
    VideoView sampleVideoView;
    MediaController mediaController;
    ProgressDialog progressDialog;
    SeekBar seekBar;
    ImageView playPauseButton, forwardButton, rewindButton;
    ImageView stopButton;
    TextView runningTime;
    int currentPosition;
    boolean isRunning = false;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sampleVideoView = (VideoView) findViewById(R.id.videoView);
        sampleVideoView.setVideoURI(Uri.parse(HLS_STREAMING_SAMPLE));

        playPauseButton = (ImageView) findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(playPauseClickListener);

        forwardButton = (ImageView) findViewById(R.id.forwardButton);
        forwardButton.setOnClickListener(forwardButtonClickListener);
        forwardButton.setEnabled(false);

        rewindButton = (ImageView) findViewById(R.id.rewindButton);
        rewindButton.setOnClickListener(rewindButtonClickListener);
        rewindButton.setEnabled(false);

        stopButton = (ImageView) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(stopClickListener);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        runningTime = (TextView) findViewById(R.id.runningTime);
        runningTime.setText("00:00");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("SampleVideoView");
        progressDialog.setMessage("Buffering...");
        progressDialog.show();

        sampleVideoView.setOnPreparedListener(prepareMediaPlayer);

        sampleVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                Toast.makeText(getBaseContext(), "Play finished", Toast.LENGTH_LONG).show();
            }
        });
    }

    private MediaPlayer.OnPreparedListener prepareMediaPlayer = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {

            progressDialog.dismiss();
            seekBar.setMax(sampleVideoView.getDuration());
            sampleVideoView.start();

            sampleVideoView.postDelayed(refreshTime, 1000);
            playPauseButton.setImageResource(R.mipmap.pause_button);
            rewindButton.setEnabled(true);
            forwardButton.setEnabled(true);
        }
    };

    private View.OnClickListener stopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playPauseButton.setImageResource(R.mipmap.play_button);
            sampleVideoView.stopPlayback();
            currentPosition = 0;
            rewindButton.setEnabled(false);
            forwardButton.setEnabled(false);
        }
    };

    private View.OnClickListener forwardButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sampleVideoView.resume();
            currentPosition = currentPosition + 5000;
            sampleVideoView.seekTo(currentPosition);
            playPauseButton.setImageResource(R.mipmap.pause_button);
        }
    };

    private View.OnClickListener rewindButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sampleVideoView.resume();
            currentPosition = currentPosition - 5000;
            sampleVideoView.seekTo(currentPosition);
            playPauseButton.setImageResource(R.mipmap.pause_button);
        }
    };

    private View.OnClickListener playPauseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.playPauseButton) {
                //Play video
                if (!isRunning) {
                    isRunning = true;
                    sampleVideoView.resume();
                    sampleVideoView.seekTo(currentPosition);
                    playPauseButton.setImageResource(R.mipmap.pause_button);
                    rewindButton.setEnabled(true);
                    forwardButton.setEnabled(true);
                }
                //Pause video
                else {
                    isRunning = false;
                    sampleVideoView.pause();
                    currentPosition = sampleVideoView.getCurrentPosition();
                    playPauseButton.setImageResource(R.mipmap.play_button);
                    rewindButton.setEnabled(false);
                    forwardButton.setEnabled(false);
                }
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            sampleVideoView.seekTo(seekBar.getProgress());
        }
    };

    private Runnable refreshTime = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(sampleVideoView.getCurrentPosition());

            if (sampleVideoView.isPlaying())
                sampleVideoView.postDelayed(refreshTime, 1000);

            int time = sampleVideoView.getCurrentPosition() / 1000;
            int minute = time / 60;
            int second = time % 60;

            runningTime.setText(minute + ":" + second);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
