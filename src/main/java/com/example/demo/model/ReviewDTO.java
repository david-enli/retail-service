package com.example.demo.model;

public class ReviewDTO {
    private Integer itemID;
    private Integer rating;
    private String reviewText;
    public ReviewDTO(Integer itemID, Integer rating, String reviewText) {
        this.itemID = itemID;
        this.rating = rating;
        this.reviewText = reviewText;
    }
    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }



}