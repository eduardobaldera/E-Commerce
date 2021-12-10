package com.pucmm.isc581_ecommerce.models;

public class Product {
    private String uuid;
    private String name;
    private String imageUrl;
    private String categoryID;
    private float price;

    public Product() {

    }

    public Product(String uuid, String name, String imageUrl, float price, String categoryID) {
        this.uuid = uuid;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.categoryID = categoryID;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
