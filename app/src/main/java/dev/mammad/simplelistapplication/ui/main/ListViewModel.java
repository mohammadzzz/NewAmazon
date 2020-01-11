package dev.mammad.simplelistapplication.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dev.mammad.simplelistapplication.model.Category;
import dev.mammad.simplelistapplication.model.Product;
import dev.mammad.simplelistapplication.network.request.ProductRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * The ViewModel that stores categories and products.
 */
public class ListViewModel extends ViewModel {

    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private MutableLiveData<List<Product>> products = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    /**
     * Gets all products.
     * <p>
     * Calls an api and stores its response and saves categories and products.
     * <p>
     * In network errors, it sets {@link #error} and alerts the view to show correct error
     *
     * @return the all products
     * @see ProductRequest
     */
    MutableLiveData<List<Product>> getAllProducts() {
        ProductRequest.getProducts(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        error.setValue("Success");
                        categories.setValue(response.body());
                        List<Product> tmpProducts = new ArrayList<>();
                        for (Category cat : response.body()) {
                            tmpProducts.addAll(cat.getProducts());
                        }
                        products.setValue(tmpProducts);
                    } else {
                        error.setValue("No Response");
                    }
                } else {
                    error.setValue(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                error.setValue(t.getMessage());
            }
        });

        return products;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    MutableLiveData<String> getError() {
        return error;
    }

    /**
     * Gets categories.
     *
     * @return the categories
     */
    public MutableLiveData<List<Category>> getCategories() {

        return categories;
    }
}