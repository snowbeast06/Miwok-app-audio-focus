package com.example.android.miwok;


import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {


    MediaPlayer mMediaPlayer = null;
    AudioManager mAudioManager;

    //mediaplayer oncompletion listener
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

        releaseMediaPlayer();
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
                    releaseMediaPlayer();
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




    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        //set our audiomanager and the focus on click listener
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // we're going to create an array to hold the number words
        final ArrayList<Word> words = new ArrayList<Word>();

        // now we'll fill in the array
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));


        // create a wordadapter whose data source is the arraylist of type Word
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        // get a reference to the listview and add the adapter to the listview
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        //set an onclicklistener...
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (getAudioFocus()) {
                    playAudioFile(words.get(position).getRawAudioResourceID());
                    Log.v("NumbersActivity", "Current Word" + words.get(position));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
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


        releaseMediaPlayer();

        mMediaPlayer = MediaPlayer.create(getActivity(), audioFileID);
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
        mMediaPlayer.start();



    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
