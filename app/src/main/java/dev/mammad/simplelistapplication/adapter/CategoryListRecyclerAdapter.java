package dev.mammad.simplelistapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.mammad.simplelistapplication.R;
import dev.mammad.simplelistapplication.interfaces.OnCategoryClickListener;
import dev.mammad.simplelistapplication.model.Category;

public class CategoryListRecyclerAdapter extends RecyclerView.Adapter<CategoryListRecyclerAdapter.CategoryViewHolder> {

    private final Context context;
    private final OnCategoryClickListener listener;
    private final List<Category> categoryList;

    public CategoryListRecyclerAdapter(Context context,
                                       List<Category> categoryList,
                                       OnCategoryClickListener listener) {
        this.context = context;
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

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryNameTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.category_list_item_name);
        }
    }
}
