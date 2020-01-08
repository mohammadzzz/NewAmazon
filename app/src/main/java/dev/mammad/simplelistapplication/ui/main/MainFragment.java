package dev.mammad.simplelistapplication.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import dev.mammad.simplelistapplication.Consts;
import dev.mammad.simplelistapplication.R;
import dev.mammad.simplelistapplication.adapter.CategoryRecyclerAdapter;
import dev.mammad.simplelistapplication.component.BaseFragment;
import dev.mammad.simplelistapplication.model.Category;
import dev.mammad.simplelistapplication.model.Product;
import dev.mammad.simplelistapplication.ui.detail.DetailFragment;

public class MainFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<Product> products = new ArrayList<>();

    private LinearLayoutManager categoryListLayoutManager;
    private ListViewModel listViewModel;
    private CategoryRecyclerAdapter categoryAdapter;
    private SwipeRefreshLayout swipeContainer;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewModel = ViewModelProviders.of(getActivity()).get(ListViewModel.class);
        recyclerView = mainView.findViewById(R.id.recycler_view);
        swipeContainer = mainView.findViewById(R.id.swipeContainer);
        setupSwipeContainer();
        getProducts();

    }

    private void getProducts() {
        swipeContainer.setRefreshing(true);
        listViewModel.getCategory().observe(getActivity(), categories -> {
            swipeContainer.setRefreshing(false);
            prepareRecyclerView(categories);
        });
        listViewModel.getError().observe(getActivity(), s -> {
            if (!s.equals("Success")) {
                swipeContainer.setRefreshing(false);

            }
        });
    }

    private void prepareRecyclerView(List<Category> categories) {
        categoryListLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(categoryListLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryAdapter = new CategoryRecyclerAdapter(getContext(),
                this.products,
                product -> startFragment(DetailFragment.newInstance(product)));
        recyclerView.setAdapter(categoryAdapter);
        this.products.clear();
        for (Category cat : categories) {
            this.products.addAll(cat.getProducts());
        }
        categoryAdapter.notifyDataSetChanged();
    }

    private void setupSwipeContainer() {
        swipeContainer.setOnRefreshListener(() -> getProducts());
    }

    @Override
    public int getFragmentID() {
        return Consts.FRAGMENT_MAIN;
    }

    @Override
    public CharSequence getFragmentTitle() {
        return "Products";
    }
}