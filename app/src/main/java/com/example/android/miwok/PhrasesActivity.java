package com.example.android.miwok;

import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

    private final int NO_IMAGE = 0;
    MediaPlayer mMediaPlayer = null;
    AudioManager mAudioManager;

    //mediaplayer oncompletion listener
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }

        }
    };

    //audiofocus change listener
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback because your Audio Focus was
                // temporarily stolen, but will be back soon.
                if (!(mMediaPlayer == null)) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    }
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Stop playback, because you lost the Audio Focus.
                // i.e. the user started some other playback app
                mAudioManager.abandonAudioFocus(this);
                if (mMediaPlayer != null) {
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
            } else if (focusChange ==
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // pause playback here.
                if (!(mMediaPlayer == null)) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    }
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Resume playback, because you hold the Audio Focus
                // again!
                if (!(mMediaPlayer == null)) {
                    mMediaPlayer.start();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //set our audiomanager and the focus on click listener
        mAudioManager = (AudioManager) PhrasesActivity.this.getSystemService(Context.AUDIO_SERVICE);

        // we're going to create an array to hold the number words
        final ArrayList<Word> words = new ArrayList<Word>();

        // now we'll fill in the array
        words.add(new Word("Where are you going?", "minto wuksus", NO_IMAGE, R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә", NO_IMAGE, R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset..", NO_IMAGE, R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?", NO_IMAGE, R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit", NO_IMAGE, R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?", NO_IMAGE, R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", NO_IMAGE, R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm", NO_IMAGE, R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis", NO_IMAGE, R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem", NO_IMAGE, R.raw.phrase_come_here));


        // create a wordadapter whose data source is the arraylist of type Word
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_phrases);

        // get a reference to the listview and add the adapter to the listview
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        //set an onclicklistener...
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (getAudioFocus()) {
                    playAudioFile(words.get(position).getRawAudioResourceID());
                    Log.v("PhrasesActivity", "Current Word" + words.get(position));
                }
            }
        });

    }

    private boolean getAudioFocus(){
        // requesting audiofocus is different for pre-Oreo and Oreo+ devices
        // So we need to have 2 different focus requests

        int result;
        //O stands for Oreo, i.e. API 26
        //So here we check the Device's API version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //If 26+, we proceed with the suggested method from the doc,
            //which requires an AudioFocusRequest object in the parameter
            AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setOnAudioFocusChangeListener(mOnAudioFocusChangeListener)
                    .build();
            result = mAudioManager.requestAudioFocus(audioFocusRequest);
        } else {
            //Else for devices lower than 26
            result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return true;
        } else {
            return false;}

    }


    private void playAudioFile(int audioFileID) {


        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;

        }
        mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, audioFileID);
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
        mMediaPlayer.start();



    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("PhrasesActivity", "Paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
    }
}

