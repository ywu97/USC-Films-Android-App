package com.example.uscfilms;

public class SliderData {
    // image url is used to
    // store the url of image
    private String imgUrl;
    private String id;
    private String category;

    // Constructor method.
    public SliderData(String imgUrl, String id, String category) {
        this.imgUrl = imgUrl;
        this.id = id;
        this.category = category;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrl;
    }
    public String getId() {return id; }
    public String getCategory() {return category;}
    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public void setId(String id) {this.id = id;}
    public void setCategory(String category) {this.category = category;}
}
