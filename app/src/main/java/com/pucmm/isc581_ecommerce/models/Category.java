package com.pucmm.isc581_ecommerce.models;

import java.util.ArrayList;

public class Category {

    private String id;
    private String name;
    private String imageUrl;
    private ArrayList<Product> products;

    public Category() {
    }

    public Category(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.products = new ArrayList<Product>();
    }

    public Category(String name, String imageUrl, ArrayList<Product> products) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.products = products;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}