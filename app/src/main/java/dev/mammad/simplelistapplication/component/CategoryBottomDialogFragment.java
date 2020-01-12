package dev.mammad.simplelistapplication.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import dev.mammad.simplelistapplication.R;
import dev.mammad.simplelistapplication.adapter.CategoryListRecyclerAdapter;
import dev.mammad.simplelistapplication.model.Category;
import dev.mammad.simplelistapplication.ui.main.ListViewModel;

/**
 * Custom bottom sheet dialog to show all categories.
 * <p>
 * By selecting a category, the list in {@link dev.mammad.simplelistapplication.ui.main.MainFragment}
 * will refresh and shows products of this category
 */
public class CategoryBottomDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "BottomDialog";

    /**
     * The interface which gives the selected category
     */
    private CategoryClickListener categoryClickListener;

    /**
     * The view which shows all categories
     */
    private RecyclerView categoryRecyclerView;

    /**
     * The adapter of the categories
     */
    private CategoryListRecyclerAdapter categoryAdapter;

    /**
     * The categories that came from api
     */
    private List<Category> categories = new ArrayList<>();

    /**
     * Instantiates new bottom dialog fragment.
     *
     * @return the dialog fragment
     */
    public static CategoryBottomDialogFragment newInstance() {
        return new CategoryBottomDialogFragment();
    }

    /**
     * Sets the listener for checking the clicks on categories.
     *
     * @param mListener the listener
     * @see dev.mammad.simplelistapplication.ui.main.MainFragment
     */
    public void setOnCategoryClickListener(CategoryClickListener mListener) {
        this.categoryClickListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }

    /**
     * When the dialog created, it shows all the categories inside the recyclerView
     *
     * @param view               The view of the dialog fragment
     * @param savedInstanceState The saved state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListViewModel viewModel = ViewModelProviders.of(getActivity()).get(ListViewModel.class);
        categoryRecyclerView = view.findViewById(R.id.category_list);
        setUpRecyclerView();
        viewModel.getCategories().observe(getActivity(), categories -> {
            this.categories.clear();
            this.categories.addAll(categories);
            categoryAdapter.notifyDataSetChanged();
        });
    }

    /**
     * The method to prepare the recycler view with layout manager and its adapter
     * with the given categories.
     *
     * @see CategoryListRecyclerAdapter
     */
    private void setUpRecyclerView() {
        LinearLayoutManager categoryListLayoutManager = new LinearLayoutManager(getActivity());
        categoryRecyclerView.setLayoutManager(categoryListLayoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        categoryAdapter = new CategoryListRecyclerAdapter(this.categories,
                category -> {
                    categoryClickListener.onCategoryClick(category);
                    dismiss();
                });

        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    /**
     * Release the listener to prevent the memory leak
     */
    @Override
    public void onDetach() {
        super.onDetach();
        categoryClickListener = null;
    }

    /**
     * The interface Item click listener.
     */
    public interface CategoryClickListener {
        /**
         * On category click.
         *
         * @param item the category item
         */
        void onCategoryClick(Category item);
    }
}