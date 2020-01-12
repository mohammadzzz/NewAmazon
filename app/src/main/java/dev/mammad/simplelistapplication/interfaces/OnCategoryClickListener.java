package dev.mammad.simplelistapplication.interfaces;

import dev.mammad.simplelistapplication.model.Category;

/**
 * The interface to get the item which clicked in bottom sheet dialog.
 *
 * @see dev.mammad.simplelistapplication.ui.main.MainFragment
 */
public interface OnCategoryClickListener {
    /**
     * On click method which gives the clicked category to show its products.
     *
     * @param category the selected category
     */
    void onClick(Category category);

}
