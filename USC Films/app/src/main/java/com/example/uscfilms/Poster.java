package com.example.uscfilms;

public class Poster {
    String id;
    String name;
    String image;
    String category;

    public Poster(String id, String name, String image,String category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCategory() {return category; }

    public void setCategory(String category) {this.category = category;}
}
