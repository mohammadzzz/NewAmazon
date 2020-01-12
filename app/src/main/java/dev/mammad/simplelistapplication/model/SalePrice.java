package dev.mammad.simplelistapplication.model;

import com.squareup.moshi.Json;

/**
 * This class has the price of a product with its currency.
 *
 * @see Product#getSalePrice()
 */
public class SalePrice {

    /**
     * The price of the product.
     */
    @Json(name = "amount")
    private String amount;

    /**
     * The currency of the price
     */
    @Json(name = "currency")
    private String currency;

    /**
     * Gets amount.
     *
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Gets currency.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }
}
