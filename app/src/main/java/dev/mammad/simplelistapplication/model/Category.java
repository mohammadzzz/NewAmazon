package dev.mammad.simplelistapplication.model;

import com.squareup.moshi.Json;

import java.util.List;

public class Category {

    @Json(name = "id")
    private String id;
    @Json(name = "name")
    private String name;
    @Json(name = "description")
    private String description;
    @Json(name = "products")
    private List<Product> products = null;

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

    public List<Product> getProducts() {
        return products;
    }

}
