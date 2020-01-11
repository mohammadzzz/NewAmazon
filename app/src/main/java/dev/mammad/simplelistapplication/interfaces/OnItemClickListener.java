package dev.mammad.simplelistapplication.interfaces;

import android.widget.ImageView;

import dev.mammad.simplelistapplication.model.Product;

public interface OnItemClickListener {

    void onClickListener(Product product, ImageView productImage);

}
