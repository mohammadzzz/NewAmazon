package dev.mammad.simplelistapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * An entity representing the product categories.
 */
public class Category implements Parcelable {

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

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    protected Category(Parcel in) {
        name = in.readString();
        products = in.createTypedArrayList(Product.CREATOR);
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(products);
    }
}
