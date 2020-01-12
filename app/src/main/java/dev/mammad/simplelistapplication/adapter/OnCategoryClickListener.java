package dev.mammad.simplelistapplication.adapter;

import dev.mammad.simplelistapplication.model.Category;

/**
 * Callback interface to get the clicked in bottom sheet dialog.
 *
 * @see dev.mammad.simplelistapplication.ui.main.MainFragment
 */
public interface OnCategoryClickListener {

    /**
     * Would be called when user clicks on a specific category.
     *
     * @param category the selected category
     */
    void onClick(Category category);

}
