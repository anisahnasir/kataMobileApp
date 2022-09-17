package com.kata.kidspe.Model;

public class ImageItem {

    public String categoryID, imageLink, name, digit, imageDark;

    public ImageItem() {
    }

    public ImageItem(String categoryID, String imageLink, String name, String digit, String imageDark) {
        this.categoryID = categoryID;
        this.imageLink = imageLink;
        this.name = name;
        this.digit = digit;
        this.imageDark = imageDark;
    }

    public String getImageDark() {
        return imageDark;
    }

    public void setImageDark(String imageDark) {
        this.imageDark = imageDark;
    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
