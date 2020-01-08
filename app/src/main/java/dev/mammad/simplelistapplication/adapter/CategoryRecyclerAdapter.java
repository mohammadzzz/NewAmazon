package dev.mammad.simplelistapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import dev.mammad.simplelistapplication.R;
import dev.mammad.simplelistapplication.interfaces.OnItemClickListener;
import dev.mammad.simplelistapplication.model.Product;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public CategoryRecyclerAdapter(Context context,
                                   List<Product> productList,
                                   OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productList = productList;
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,
                parent,
                false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productTitle.setText(product.getName());

        holder.parent.setOnClickListener(v -> listener.onClickListener(product));

        Glide.with(context)
                .load(product.getUrl())
                .placeholder(R.drawable.placeholder)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        if (productList == null || productList.isEmpty()) {
            return 0;
        }
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        CardView parent;
        TextView productTitle;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.card_view);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
        }
    }

}
