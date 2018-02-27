package com.example.android.miwok;

/**
 * Created by genetrinks on 2/7/18.
 */

public class Word {

    private static final int NO_IMAGE_ASSIGNED = 0;
    private String mMiwokTranslation;
    private String mDefaultTranslation;
    private int mDrawableImageResourceID;
    private int mRawAudioResourceID;

    // constructor
    public Word(String defaultTranslation, String miwokTranslation) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        // we'll leave the resourceID as 0 in this case
    }


    // constructor with image
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceID) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mDrawableImageResourceID = imageResourceID;
    }

    // constructor with image and audio file
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceID, int audioResourceID) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mDrawableImageResourceID = imageResourceID;
        mRawAudioResourceID = audioResourceID;
    }

    //Getters
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public int getDrawableImageResourceID() {
        return mDrawableImageResourceID;
    }

    public int getRawAudioResourceID() {
        return mRawAudioResourceID;
    }


    public boolean hasImage() {
        return (mDrawableImageResourceID != NO_IMAGE_ASSIGNED);
    }

    @Override
    public String toString() {
        return "Word{" +
                "mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mDrawableImageResourceID=" + mDrawableImageResourceID +
                ", mRawAudioResourceID=" + mRawAudioResourceID +
                '}';
    }


}
