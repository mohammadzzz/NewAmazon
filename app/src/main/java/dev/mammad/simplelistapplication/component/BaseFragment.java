package dev.mammad.simplelistapplication.component;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import dev.mammad.simplelistapplication.MainActivity;

public abstract class BaseFragment extends Fragment {

    protected View mainView;
    protected MainActivity mainActivity;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.onCreateActionButtons(getFragmentID());
        mainActivity.setActionBarTitleAndColor(getFragmentTitle());
    }

    protected void startFragment(BaseFragment baseFragment, View sharedElement) {
        mainActivity.startFragment(baseFragment, sharedElement);
    }

    public abstract int getFragmentID();

    public CharSequence getFragmentTitle() {
        return "";
    }

}