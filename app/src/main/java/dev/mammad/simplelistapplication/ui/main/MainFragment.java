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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import dev.mammad.simplelistapplication.Consts;
import dev.mammad.simplelistapplication.R;
import dev.mammad.simplelistapplication.adapter.CategoryRecyclerAdapter;
import dev.mammad.simplelistapplication.component.BaseFragment;
import dev.mammad.simplelistapplication.component.CategoryBottomDialogFragment;
import dev.mammad.simplelistapplication.model.Product;
import dev.mammad.simplelistapplication.ui.detail.DetailFragment;

public class MainFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<Product> allProducts = new ArrayList<>();

    private LinearLayoutManager categoryListLayoutManager;
    private ListViewModel listViewModel;
    private CategoryRecyclerAdapter categoryAdapter;
    private SwipeRefreshLayout swipeContainer;
    private FloatingActionButton fab;
    private CategoryBottomDialogFragment dialog;

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
        initViews();
        setupSwipeContainer();
        prepareRecyclerView();
        getAllProducts();
    }

    private void initViews() {
        recyclerView = mainView.findViewById(R.id.recycler_view);
        swipeContainer = mainView.findViewById(R.id.swipeContainer);
        fab = mainView.findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            dialog = CategoryBottomDialogFragment.newInstance();
            dialog.setOnCategoryClickListener(item -> {
                allProducts.clear();
                allProducts.addAll(item.getProducts());
                categoryAdapter.notifyDataSetChanged();
            });
            dialog.show(getChildFragmentManager(), CategoryBottomDialogFragment.TAG);
        });
    }

    private void getAllProducts() {
        swipeContainer.setRefreshing(true);
        listViewModel.getAllProducts().observe(getActivity(), products -> {
            swipeContainer.setRefreshing(false);
            allProducts.clear();
            allProducts.addAll(products);
            categoryAdapter.notifyDataSetChanged();
        });

        listViewModel.getError().observe(getActivity(), s -> {
            if (!s.equals("Success")) {
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void prepareRecyclerView() {
        categoryListLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(categoryListLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryAdapter = new CategoryRecyclerAdapter(getContext(),
                this.allProducts,
                product -> startFragment(DetailFragment.newInstance(product)));
        recyclerView.setAdapter(categoryAdapter);
    }

    private void setupSwipeContainer() {
        swipeContainer.setOnRefreshListener(() -> getAllProducts());
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