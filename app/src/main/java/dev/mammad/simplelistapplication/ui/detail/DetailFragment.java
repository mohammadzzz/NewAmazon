package dev.mammad.simplelistapplication.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import dev.mammad.simplelistapplication.Consts;
import dev.mammad.simplelistapplication.R;
import dev.mammad.simplelistapplication.component.BaseFragment;
import dev.mammad.simplelistapplication.model.Product;

public class DetailFragment extends BaseFragment {
    private Product product;
    private ImageView image;
    private TextView name, price;

    public static DetailFragment newInstance(Product product) {

        Bundle args = new Bundle();
        args.putParcelable("product", product);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
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
            product = getArguments().getParcelable("product");
        }

        image = view.findViewById(R.id.detail_fragment_header_image);
        name = view.findViewById(R.id.detail_fragment_header_name);
        price = view.findViewById(R.id.detail_fragment_header_price);
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
