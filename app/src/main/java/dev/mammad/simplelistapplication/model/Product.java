package dev.mammad.simplelistapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import dev.mammad.simplelistapplication.config.Configurations;


/**
 * The Product model.
 * <p>
 * This model holds all Products af a specific category
 * This class also implements Parcelable, so we can pass it to next fragment.
 *
 * @see dev.mammad.simplelistapplication.ui.detail.DetailFragment#newInstance(Product, String)
 */
public class Product implements Parcelable {

    /**
     * The constant CREATOR.
     */
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    /**
     * The name of the Product.
     */
    @Json(name = "name")
    private String name;

    /**
     * The Url of the products image.
     */
    @Json(name = "url")
    private String url;

    /**
     * The sale price of the product with its currency
     */
    @Json(name = "salePrice")
    private SalePrice salePrice;

    private Product(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    /**
     * Gets the name of product.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the image url of product.
     * <p>
     * Adds base url to get the full path to image
     *
     * @return the url
     */
    public String getUrl() {
        return Configurations.getBaseUrl() + url;
    }

    /**
     * Gets sale price of product.
     *
     * @return the sale price
     */
    public SalePrice getSalePrice() {
        return salePrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
    }
}
