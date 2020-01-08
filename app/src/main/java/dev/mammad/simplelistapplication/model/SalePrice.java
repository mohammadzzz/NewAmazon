package dev.mammad.simplelistapplication.model;

import com.squareup.moshi.Json;

public class SalePrice {

    @Json(name = "amount")
    private String amount;
    @Json(name = "currency")
    private String currency;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
