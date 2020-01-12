package dev.mammad.simplelistapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import dev.mammad.simplelistapplication.config.Configurations;

public class Product implements Parcelable {

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
    @Json(name = "id")
    private String id;
    @Json(name = "categoryId")
    private String categoryId;
    @Json(name = "name")
    private String name;
    @Json(name = "url")
    private String url;
    @Json(name = "description")
    private String description;
    @Json(name = "salePrice")
    private SalePrice salePrice;

    private Product(Parcel in) {
        id = in.readString();
        categoryId = in.readString();
        name = in.readString();
        url = in.readString();
        description = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return Configurations.getBaseUrl() + url;
    }

    public SalePrice getSalePrice() {
        return salePrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(categoryId);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(description);
    }
}
