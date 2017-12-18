package crats.mvcbaseproject.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import crats.mvcbaseproject.R;
import crats.mvcbaseproject.model.Character;

import static android.view.View.GONE;

/**
 * Created by arsh on 2017-12-16.
 */

public class CharacterAdapter extends ArrayAdapter {

    private Context mContext;
    public CharacterAdapter(Context context, ArrayList<Character> characters) {
        super(context, 0, characters);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.character_item, parent, false);
        }
        final Character character = (Character) getItem(position);
        ImageView image = (ImageView) listItemView.findViewById(R.id.character_image);
        TextView charName = (TextView) listItemView.findViewById(R.id.character_name);
        TextView comicsNumber = (TextView) listItemView.findViewById(R.id.comics_number);

        TextView charDescrp = (TextView) listItemView.findViewById(R.id.character_description);

        image.setImageBitmap(character.getImage());
        charName.setText(character.getCharName());
        charDescrp.setText(character.getDecrp());
        comicsNumber.setText("Appeared in " + character.getAvailableComics() + " Comics");
        return listItemView;
    }
}
