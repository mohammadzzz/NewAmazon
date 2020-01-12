package dev.mammad.simplelistapplication.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import dev.mammad.simplelistapplication.R;
import dev.mammad.simplelistapplication.adapter.ProductRecyclerAdapter;
import dev.mammad.simplelistapplication.component.BaseFragment;
import dev.mammad.simplelistapplication.component.CategoryBottomDialogFragment;
import dev.mammad.simplelistapplication.model.Product;
import dev.mammad.simplelistapplication.ui.detail.DetailFragment;

/**
 * The Main fragment.
 * <p>
 * This fragment contains a recycler view that has all products inside it.
 * <p>
 * There is also a FAB that can filter the products by their category
 */
public class MainFragment extends BaseFragment {

    /**
     * The id of main fragment.
     */
    public static final int FRAGMENT_MAIN_ID = 1001;

    /**
     * The RecyclerView that will show all the products
     */
    private RecyclerView recyclerView;
    /**
     * The list of all Products
     *
     * @see Product
     */
    private List<Product> allProducts = new ArrayList<>();

    /**
     * The ViewModel that stores everything inside.
     *
     * @see ListViewModel
     */
    private ListViewModel listViewModel;

    /**
     * The adapter that connects list of products to recyclerView
     *
     * @see ProductRecyclerAdapter
     */
    private ProductRecyclerAdapter productAdapter;

    /**
     * The layout that contains the recycler view and refresh its data with swipe down gesture
     */
    private SwipeRefreshLayout swipeContainer;

    /**
     * The Bottom sheet dialog that show the categories and can filter products with it
     *
     * @see CategoryBottomDialogFragment
     */
    private CategoryBottomDialogFragment dialog;
    private ImageView errorImage;

    /**
     * New instance main fragment.
     *
     * @return the main fragment
     */
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

        // Create an instance of ViewModel which attached to MainActivity lifetime
        listViewModel = ViewModelProviders.of(mainActivity).get(ListViewModel.class);
        initViews();
        setupSwipeContainer();
        prepareRecyclerView();
        getAllProducts();
    }

    /**
     * Initialize view
     */

    private void initViews() {
        recyclerView = mainView.findViewById(R.id.recycler_view);
        swipeContainer = mainView.findViewById(R.id.swipe_container);
        errorImage = mainView.findViewById(R.id.main_fragment_network_error);

        errorImage.setOnClickListener(v -> getAllProducts());

        FloatingActionButton fab = mainView.findViewById(R.id.fab);

        // shows the dialog to select the category and filter products
        fab.setOnClickListener(v -> {
            dialog = CategoryBottomDialogFragment.newInstance();
            dialog.setOnCategoryClickListener(item -> {
                allProducts.clear();
                allProducts.addAll(item.getProducts());
                productAdapter.notifyDataSetChanged();
            });
            dialog.show(getChildFragmentManager(), CategoryBottomDialogFragment.TAG);
        });
    }

    /**
     * Getting all the products from ViewModel
     *
     * @see ListViewModel
     */
    private void getAllProducts() {
        swipeContainer.setRefreshing(true);
        listViewModel.getAllProducts().observe(mainActivity, products -> {
            swipeContainer.setRefreshing(false);
            allProducts.clear();
            allProducts.addAll(products);
            productAdapter.notifyDataSetChanged();
        });

        // In case of network error, we show a toast
        listViewModel.getError().observe(mainActivity, s -> {
            if (!s.equals("Success")) {
                swipeContainer.setRefreshing(false);
                showNetworkError();
                Toast.makeText(mainActivity, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            } else {
                hideNetworkError();
            }
        });
    }

    private void showNetworkError() {
        swipeContainer.setVisibility(View.GONE);
        errorImage.setVisibility(View.VISIBLE);
    }

    private void hideNetworkError() {
        swipeContainer.setVisibility(View.VISIBLE);
        errorImage.setVisibility(View.GONE);
    }

    /**
     * Preparing RecyclerView and sets its adapter
     *
     * @see ProductRecyclerAdapter
     */
    private void prepareRecyclerView() {
        LinearLayoutManager categoryListLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(categoryListLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        productAdapter = new ProductRecyclerAdapter(getContext(),
                this.allProducts,
                (product, productImage) -> startFragment(DetailFragment.newInstance(
                        product,
                        ViewCompat.getTransitionName(productImage))
                        , productImage));
        recyclerView.setAdapter(productAdapter);
    }

    private void setupSwipeContainer() {
        swipeContainer.setOnRefreshListener(this::getAllProducts);
    }

    /**
     * Returning the fragment id with unique Id
     *
     * @return FragmentId
     */

    @Override
    public int getFragmentID() {
        return FRAGMENT_MAIN_ID;
    }

    @Override
    public CharSequence getFragmentTitle() {
        return "Products";
    }

}