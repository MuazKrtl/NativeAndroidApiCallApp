package com.example.invio.Classes;



import java.io.Serializable;

public class Cat implements Serializable {

    String name;
    String image;
    String description;
    String intelligence;
    String isFavorite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getIntelligence() { return intelligence; }

    public void setIntelligence(String intelligence) { this.intelligence = intelligence; }

    public String getIsFavorite() { return isFavorite; }

    public void setIsFavorite(String isFavorite) { this.isFavorite = isFavorite; }
}
