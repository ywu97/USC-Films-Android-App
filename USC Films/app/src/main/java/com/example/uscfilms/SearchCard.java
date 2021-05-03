package com.example.uscfilms;

public class SearchCard {
    String SearchCategory;
    String SearchYear;
    String SearchTitleName;
    String SearchStarsRate;
    String SearchImg;
    String SearchId;

    public SearchCard(String searchCategory, String searchYear, String searchTitleName, String searchStarsRate, String searchImg, String searchId) {
        SearchCategory = searchCategory;
        SearchYear = searchYear;
        SearchTitleName = searchTitleName;
        SearchStarsRate = searchStarsRate;
        SearchImg = searchImg;
        SearchId = searchId;
    }

    public String getSearchCategory() {
        return SearchCategory;
    }

    public String getSearchYear() {
        return SearchYear;
    }

    public String getSearchTitleName() {
        return SearchTitleName;
    }

    public String getSearchStarsRate() {
        return SearchStarsRate;
    }

    public String getSearchImg() {
        return SearchImg;
    }

    public String getSearchid() {
        return SearchId;
    }

    public void setSearchCategory(String searchCategory) {
        SearchCategory = searchCategory;
    }

    public void setSearchYear(String searchYear) {
        SearchYear = searchYear;
    }

    public void setSearchTitleName(String searchTitleName) {
        SearchTitleName = searchTitleName;
    }

    public void setSearchStarsRate(String searchStarsRate) {
        SearchStarsRate = searchStarsRate;
    }

    public void setSearchImg(String searchImg) {
        SearchImg = searchImg;
    }

    public void setSearchId(String searchId) {
        SearchId = searchId;
    }
}
