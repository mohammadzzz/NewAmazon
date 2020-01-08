package dev.mammad.simplelistapplication.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.mammad.simplelistapplication.model.Category;
import dev.mammad.simplelistapplication.network.request.ProductRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {

    private MutableLiveData<List<Category>> category;
    private MutableLiveData<String> error;

    public MutableLiveData<List<Category>> getCategory() {
        if (category == null) {
            category = new MutableLiveData<>();
            ProductRequest.getProducts(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            error.setValue("Success");
                            category.setValue(response.body());
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
        }
        return category;
    }

    public MutableLiveData<String> getError() {
        if (error == null) {
            error = new MutableLiveData<>();
        }
        return error;
    }
}