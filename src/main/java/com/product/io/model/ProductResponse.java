package com.product.io.model;

import java.util.Map;

public class ProductResponse {


    private String name;
    private String description;
    private Map<String,Float> price;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Float> getPrice() {
        return price;
    }

    public void setPrice(Map<String, Float> price) {
        this.price = price;
    }
}
