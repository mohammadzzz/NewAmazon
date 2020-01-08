package dev.mammad.simplelistapplication.component;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import dev.mammad.simplelistapplication.MainActivity;

public abstract class BaseFragment extends Fragment {

    protected View mainView;
    protected MainActivity mainActivity;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.onCreateActionButtons(getFragmentID());
        mainActivity.setActionBarTitleAndColor(getFragmentTitle());
    }

    public void startFragment(BaseFragment baseFragment) {
        mainActivity.startFragment(baseFragment);
    }

    public abstract int getFragmentID();

    public CharSequence getFragmentTitle() {
        return "";
    }

}