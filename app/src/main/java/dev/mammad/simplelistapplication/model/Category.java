package dev.mammad.simplelistapplication.model;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * The Category model.
 * <p>
 * The model that comes from API and holds all categories and products
 */
public class Category {

    /**
     * The name of each category.
     */
    @Json(name = "name")
    private String name;

    /**
     * Each category has their own products.
     * This property, holds all products inside it.
     */
    @Json(name = "products")
    private List<Product> products = null;

    /**
     * Gets the name of each category.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets products of this category.
     *
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }

}
