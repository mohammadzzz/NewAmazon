package dev.mammad.simplelistapplication.ui.main;

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

public class ListViewModel extends ViewModel {

    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private MutableLiveData<List<Product>> products = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public MutableLiveData<List<Product>> getAllProducts() {
        ProductRequest.getProducts(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
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
            public void onFailure(Call<List<Category>> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });

        return products;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public MutableLiveData<List<Category>> getCategories() {

        return categories;
    }
}