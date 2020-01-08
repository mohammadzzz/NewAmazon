package dev.mammad.simplelistapplication.network.request;

import java.util.List;

import dev.mammad.simplelistapplication.model.Category;
import dev.mammad.simplelistapplication.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

public class ProductRequest {
    public static void getProducts(Callback<List<Category>> callback) {
        ProductService productService = ServiceGenerator.createService(ProductService.class);
        Call<List<Category>> getProducts = productService.getProducts();
        getProducts.enqueue(callback);
    }

    public interface ProductService {
        @GET(".")
        Call<List<Category>> getProducts();
    }
}
