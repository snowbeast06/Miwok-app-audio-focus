package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by genetrinks on 2/7/18.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int mColorResourceID;

    public WordAdapter(Activity context, ArrayList<Word> words, int color) {
        super(context, 0, words);
        mColorResourceID = color;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Word currentWord = getItem(position);

        // Find the Imageview in the list_item.xml layout with the ID image
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        // Get the image resource ID from the current Word object and
        // set this image on the imageview
        imageView.setImageResource(currentWord.getDrawableImageResourceID());

        // if there is no image assigned set the visibility of the imageview to GONE
        if (!currentWord.hasImage()) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }

        // Find the TextView in the list_item.xml layout with the ID miwokWord
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwokWord);

        // Get the miwok Translation from the current Word object and
        // set this text on the name TextView
        miwokTextView.setText(currentWord.getMiwokTranslation());

        // Find the TextView in the list_item.xml layout with the ID englishword
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.englishWord);

        // Get the english Translation from the current Word object and
        // set this text on the name TextView
        defaultTextView.setText(currentWord.getDefaultTranslation());

        // set the proper background color for this linear layout with the text
        RelativeLayout parentLayout = (RelativeLayout) listItemView.findViewById(R.id.textViewParent);
        int color = ContextCompat.getColor(getContext(), mColorResourceID);
        parentLayout.setBackgroundColor(color);

        //return the view
        return listItemView;

    }
}
