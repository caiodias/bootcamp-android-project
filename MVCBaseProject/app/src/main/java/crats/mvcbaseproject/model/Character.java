package crats.mvcbaseproject.model;

import android.graphics.Bitmap;

/**
 * Created by arsh on 2017-12-16.
 */

public class Character {

    private String Name;
    private int ID;
    private String Desc;
    private Bitmap Image;
    private int AvailableComics;


    public Character(String name, int ID, String descrp, Bitmap image, int availableComics){
        Name = name;
        ID = ID;
        Desc = descrp;
        Image = image;
        AvailableComics = availableComics;

    }

    public String getCharName(){
        return Name;
    }

    public int getID(){
        return ID;
    }

    public String getDecrp(){
        return Desc;
    }

    public Bitmap getImage(){
        return Image;
    }

    public int getAvailableComics(){
        return AvailableComics;
    }



}
