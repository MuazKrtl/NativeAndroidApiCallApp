package com.example.invio.Classes;

public class MessageToCat {

    Cat cat;
    boolean isFavorite;

    public Cat getCat() {
        return cat;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
    public MessageToCat(Cat cat, boolean isFavorite){ this.cat = cat; this.isFavorite = isFavorite; }


}
