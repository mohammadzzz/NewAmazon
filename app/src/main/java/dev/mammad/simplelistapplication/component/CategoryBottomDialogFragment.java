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

public class CategoryBottomDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "BottomDialog";

    private ItemClickListener mListener;
    private RecyclerView categoryRecyclerView;
    private LinearLayoutManager categoryListLayoutManager;
    private CategoryListRecyclerAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private ListViewModel viewModel;

    public static CategoryBottomDialogFragment newInstance() {
        return new CategoryBottomDialogFragment();
    }

    public void setOnCategoryClickListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(ListViewModel.class);
        categoryRecyclerView = view.findViewById(R.id.category_list);
        setUpRecyclerView();
        viewModel.getCategories().observe(getActivity(), categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
            categoryAdapter.notifyDataSetChanged();
        });
    }

    private void setUpRecyclerView() {
        categoryListLayoutManager = new LinearLayoutManager(getActivity());
        categoryRecyclerView.setLayoutManager(categoryListLayoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        categoryAdapter = new CategoryListRecyclerAdapter(getContext(),
                this.categoryList,
                category -> {
                    mListener.onItemClick(category);
                    dismiss();
                });

        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ItemClickListener {
        void onItemClick(Category item);
    }
}