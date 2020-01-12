package dev.mammad.simplelistapplication.network.request;

import java.util.List;

import dev.mammad.simplelistapplication.model.Category;
import dev.mammad.simplelistapplication.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Encapsulates the remote API call to retrieve product and their categories.
 */
public class ProductRequest {

    /**
     * Performs the actual mechanics of API call and un-marshalling the JSON response into
     * our domain entities.
     *
     * @param callback The callback interface enabling us to handle the remote call in an async
     *                 fashion.
     */
    public static void getProducts(Callback<List<Category>> callback) {
        ProductService productService = ServiceGenerator.createService(ProductService.class);
        Call<List<Category>> getProducts = productService.getProducts();

        getProducts.enqueue(callback);
    }

    /**
     * The corresponding Retrofit client interface.
     */
    interface ProductService {

        /**
         * Sends a GET request to the API.
         *
         * @return Collection of categories and their products.
         */
        @GET(".")
        Call<List<Category>> getProducts();
    }
}
