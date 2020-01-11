package dev.mammad.simplelistapplication.ui.detail;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.TransitionInflater;

import com.bumptech.glide.Glide;

import dev.mammad.simplelistapplication.Consts;
import dev.mammad.simplelistapplication.R;
import dev.mammad.simplelistapplication.component.BaseFragment;
import dev.mammad.simplelistapplication.model.Product;

/**
 * The Detail fragment.
 * <p>
 * This fragment shows details of a product.
 */
public class DetailFragment extends BaseFragment {
    private static final String EXTRA_TRANSITION_NAME = "transition_name";
    private static final String EXTRA_PRODUCT_ITEM = "product_item";

    /**
     * The Product to show
     */
    private Product product;
    /**
     * The name of the item to animate
     */
    private String transitionName;

    /**
     * Creates a new {@link DetailFragment} with the given product and transition name
     *
     * @param product        the product
     * @param transitionName the transition name
     * @return the detail fragment
     */
    public static DetailFragment newInstance(Product product, String transitionName) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PRODUCT_ITEM, product);
        args.putString(EXTRA_TRANSITION_NAME, transitionName);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set element transition if the sdk is more than 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext())
                    .inflateTransition(android.R.transition.move));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);
            product = getArguments().getParcelable(EXTRA_PRODUCT_ITEM);
        }

        ImageView image = view.findViewById(R.id.detail_fragment_header_image);
        TextView name = view.findViewById(R.id.detail_fragment_header_name);
        TextView price = view.findViewById(R.id.detail_fragment_header_price);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image.setTransitionName(transitionName);
        }

        Glide.with(this)
                .load(product.getUrl())
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .into(image);

        name.setText(product.getName());
        price.setText(new StringBuilder().append(product.getSalePrice().getAmount())
                .append(product.getSalePrice().getCurrency()));
    }

    @Override
    public int getFragmentID() {
        return Consts.FRAGMENT_DETAIL;
    }

    @Override
    public CharSequence getFragmentTitle() {
        return product.getName();
    }
}
