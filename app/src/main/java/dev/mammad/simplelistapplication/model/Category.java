package dev.mammad.simplelistapplication.model;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * An entity representing the product categories.
 */
public class Category {

    /**
     * The name of each category.
     */
    @Json(name = "name")
    private String name;

    /**
     * Collection of products belonging to the current category.
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
