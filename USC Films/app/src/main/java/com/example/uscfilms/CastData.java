package com.example.uscfilms;

public class CastData {
    private String imgUrl;
    private String name;

    // Constructor method.
    public CastData(String imgUrl, String name) {
        this.imgUrl = imgUrl;
        this.name = name;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrl;
    }
    public String getName() {return name; }
    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public void setName(String name) {this.name = name;}
}
