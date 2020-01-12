package dev.mammad.simplelistapplication.component;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import dev.mammad.simplelistapplication.MainActivity;

/**
 * The Base fragment.
 * <p>
 * Each Fragment inherits from BaseFragment
 *
 * @see dev.mammad.simplelistapplication.ui.detail.DetailFragment
 * @see dev.mammad.simplelistapplication.ui.main.MainFragment
 */
public abstract class BaseFragment extends Fragment {

    /**
     * The main view of fragment.
     */
    protected View mainView;

    /**
     * The Main activity that contains this Fragment.
     *
     * @see MainActivity
     */
    protected MainActivity mainActivity;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
    }

    /**
     * Sets the {@link #mainActivity} from the activity that contains it
     *
     * @param context Activity context.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    /**
     * Sets the title and back button for the current fragment
     *
     * @see MainActivity#onCreateActionButtons(int)
     * @see MainActivity#setActionBarTitle(CharSequence)
     */
    @Override
    public void onResume() {
        super.onResume();
        mainActivity.onCreateActionButtons(getFragmentID());
        mainActivity.setActionBarTitle(getFragmentTitle());
    }

    /**
     * Start fragment.
     *
     * @param baseFragment  the base fragment
     * @param sharedElement the shared element
     * @see MainActivity#startFragment(Fragment, View)
     */
    protected void startFragment(BaseFragment baseFragment, View sharedElement) {
        mainActivity.startFragment(baseFragment, sharedElement);
    }

    /**
     * Gets fragment id.
     *
     * @return the fragment id
     * @see #onResume()
     */
    public abstract int getFragmentID();

    /**
     * Gets fragment title.
     * <p>
     * Each fragment can change the title within themselves, otherwise the title will be empty
     *
     * @return the fragment title
     * @see #onResume()
     */
    public CharSequence getFragmentTitle() {
        return "";
    }
}
