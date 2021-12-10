package com.pucmm.isc581_ecommerce.models;

import java.util.ArrayList;

public class Order {
    private String id;
    private float total;
    private ArrayList<Product> products;

    public Order() {
    }

    public Order(String id, float total, ArrayList<Product> products) {
        this.id = id;
        this.total = total;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
