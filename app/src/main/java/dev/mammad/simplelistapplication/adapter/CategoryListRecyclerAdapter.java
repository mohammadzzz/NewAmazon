package dev.mammad.simplelistapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.mammad.simplelistapplication.R;
import dev.mammad.simplelistapplication.component.CategoryBottomDialogFragment;
import dev.mammad.simplelistapplication.interfaces.OnCategoryClickListener;
import dev.mammad.simplelistapplication.model.Category;

/**
 * The adapter to connect the data of categories into recycler view of the bottom dialog.
 *
 * @see CategoryBottomDialogFragment
 */
public class CategoryListRecyclerAdapter extends RecyclerView.Adapter<CategoryListRecyclerAdapter.CategoryViewHolder> {

    private final OnCategoryClickListener listener;
    private final List<Category> categoryList;

    /**
     * Instantiates a new Category list recycler adapter.
     *
     * @param categoryList the category list
     * @param listener     the listener
     */
    public CategoryListRecyclerAdapter(List<Category> categoryList,
                                       OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item,
                parent,
                false);
        return new CategoryViewHolder(view);
    }

    /**
     * Sets the categories by the given position
     *
     * @param holder   The category holder
     * @param position The position of the category
     */
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryNameTextView.setText(category.getName());
        holder.categoryNameTextView.setOnClickListener(v -> listener.onClick(category));
    }

    @Override
    public int getItemCount() {
        if (categoryList == null || categoryList.isEmpty()) {
            return 0;
        }
        return categoryList.size();
    }

    /**
     * The Category view holder.
     */
    class CategoryViewHolder extends RecyclerView.ViewHolder {
        /**
         * The name of the category
         */
        private final TextView categoryNameTextView;

        /**
         * Instantiates a new Category view holder.
         *
         * @param itemView the item view
         */
        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.category_list_item_name);
        }
    }
}
