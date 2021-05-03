package com.example.uscfilms;

public class ReviewCards {
    String userNameAndDate;
    String rating;
    String reviewText;
    public ReviewCards(String userNameAndDate, String rating, String reviewText) {
        this.userNameAndDate = userNameAndDate;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public String getUserNameAndDate() {
        return userNameAndDate;
    }

    public void setUserNameAndDate(String userNameAndDate) {
        this.userNameAndDate = userNameAndDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
