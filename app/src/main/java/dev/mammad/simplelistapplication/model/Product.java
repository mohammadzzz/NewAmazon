package dev.mammad.simplelistapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import dev.mammad.simplelistapplication.Consts;

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

    protected Product(Parcel in) {
        id = in.readString();
        categoryId = in.readString();
        name = in.readString();
        url = in.readString();
        description = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return Consts.BASE_URL + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SalePrice getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(SalePrice salePrice) {
        this.salePrice = salePrice;
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
