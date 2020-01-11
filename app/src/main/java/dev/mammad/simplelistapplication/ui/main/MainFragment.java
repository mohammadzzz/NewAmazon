package dev.mammad.simplelistapplication.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
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
import dev.mammad.simplelistapplication.adapter.ProductRecyclerAdapter;
import dev.mammad.simplelistapplication.component.BaseFragment;
import dev.mammad.simplelistapplication.component.CategoryBottomDialogFragment;
import dev.mammad.simplelistapplication.model.Product;
import dev.mammad.simplelistapplication.ui.detail.DetailFragment;

public class MainFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<Product> allProducts = new ArrayList<>();

    private ListViewModel listViewModel;
    private ProductRecyclerAdapter categoryAdapter;
    private SwipeRefreshLayout swipeContainer;
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewModel = ViewModelProviders.of(mainActivity).get(ListViewModel.class);
        initViews();
        setupSwipeContainer();
        prepareRecyclerView();
        getAllProducts();
    }

    private void initViews() {
        recyclerView = mainView.findViewById(R.id.recycler_view);
        swipeContainer = mainView.findViewById(R.id.swipeContainer);
        FloatingActionButton fab = mainView.findViewById(R.id.fab);

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
        listViewModel.getAllProducts().observe(mainActivity, products -> {
            swipeContainer.setRefreshing(false);
            allProducts.clear();
            allProducts.addAll(products);
            categoryAdapter.notifyDataSetChanged();
        });

        listViewModel.getError().observe(mainActivity, s -> {
            if (!s.equals("Success")) {
                swipeContainer.setRefreshing(false);
                Toast.makeText(mainActivity, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareRecyclerView() {
        LinearLayoutManager categoryListLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(categoryListLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryAdapter = new ProductRecyclerAdapter(getContext(),
                this.allProducts,
                (product, productImage) -> startFragment(DetailFragment.newInstance(
                        product,
                        ViewCompat.getTransitionName(productImage))
                        , productImage));
        recyclerView.setAdapter(categoryAdapter);
    }

    private void setupSwipeContainer() {
        swipeContainer.setOnRefreshListener(this::getAllProducts);
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