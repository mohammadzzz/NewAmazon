package dev.mammad.simplelistapplication.adapter;

import android.widget.ImageView;

import dev.mammad.simplelistapplication.model.Product;

/**
 * The interface to get the selected product of the product list in main fragment.
 *
 * @see dev.mammad.simplelistapplication.ui.main.MainFragment
 */
public interface OnItemClickListener {

    /**
     * Would be called when user clicks on a particular product.
     *
     * @param product      the selected product
     * @param productImage the product image to create the animation
     */
    void onClickListener(Product product, ImageView productImage);

}
