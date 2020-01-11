package dev.mammad.simplelistapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import dev.mammad.simplelistapplication.component.BaseFragment;
import dev.mammad.simplelistapplication.ui.detail.DetailFragment;
import dev.mammad.simplelistapplication.ui.main.MainFragment;

/**
 * The main activity.
 * The activity that contains all other fragments
 *
 * @author moham
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The toolbar which shows back icon and page title
     *
     * @see MainActivity#initToolbar()
     * @see MainActivity#setActionBarTitle(CharSequence)
     * @see MainActivity#onCreateActionButtons(int)
     */
    private Toolbar toolbar;

    /**
     * The variable that checks if back button pressed two times or not.
     * If user press back button, this switch become true for 2 seconds.
     * If user press back one more time, the app will close, otherwise it becomes false again.
     *
     * @see MainActivity#onBackPressed()
     */
    private boolean doubleBackToExitPressedOnce = false;


    /**
     * The TextView that shows page title and is in toolbar
     *
     * @see MainActivity#setActionBarTitle(CharSequence)
     * @see MainFragment#getFragmentTitle()
     * @see DetailFragment#getFragmentTitle()
     */
    private TextView appTitle;

    /**
     * The method that starts a fragment and replaces a fragment with another one with animation.
     * <p>
     * If sharedElement equals to null, there will be no transition for shared element
     *
     * @param fragment      the fragment to start and replace with current one
     * @param sharedElement the shared element
     */
    public void startFragment(Fragment fragment, View sharedElement) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        /*
          Sets animation between two fragments transition
         */
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_from_right,
                R.anim.enter_from_right, R.anim.exit_from_left);

        if (sharedElement != null) {
            if (ViewCompat.getTransitionName(sharedElement) != null) {
                transaction.addSharedElement(sharedElement, ViewCompat.getTransitionName(sharedElement));
            }
        }

        transaction.replace(R.id.container, fragment, String.valueOf(fragment.getId()));

        /*
        Adding the fragment to back stack in order to come back to it with back button
         */
        transaction.addToBackStack(String.valueOf(fragment.getId()));

        try {
            fragmentManager.executePendingTransactions();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        if (savedInstanceState == null) {
            startFragment(MainFragment.newInstance(), null);
        }
    }

    /**
     * Initializing the toolbar view and set the back icon
     * Also sets the listener on back button to call the {{@link #onBackPressed()}}
     */
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_white));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * Overrides default {@link AppCompatActivity#onBackPressed()}
     * <p>
     * Gets the visible fragment {@link #getVisibleFragment()} and forces the double back for exit
     * if the user was on {@link MainFragment} otherwise, calls the default
     */
    @Override
    public void onBackPressed() {
        Fragment visibleFragment = getVisibleFragment();
        if (visibleFragment != null) {
            if (visibleFragment instanceof MainFragment) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, R.string.press_back_again, Toast.LENGTH_LONG).show();

                // Wait for 2 seconds for user to press back again
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * The method that returns current visible fragment.
     *
     * @return Fragment
     * @see #onBackPressed()
     */
    private Fragment getVisibleFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
        }
        return null;
    }

    /**
     * Each time a fragment appears, this method make sure that we have the correct icons
     * on toolbar.
     * <p>
     * Can also be useful for Bottom Navigation
     *
     * @param fragmentID the current fragment id
     * @see BaseFragment#onResume()
     */
    public void onCreateActionButtons(int fragmentID) {
        switch (fragmentID) {
            case Consts.FRAGMENT_MAIN:
                toolbar.setNavigationIcon(null);
                break;
            case Consts.FRAGMENT_DETAIL:
                toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_white));
                break;
        }
    }

    /**
     * Sets action bar title with the given title.
     *
     * @param fragmentTitle the fragment title
     */
    public void setActionBarTitle(CharSequence fragmentTitle) {
        appTitle = toolbar.findViewById(R.id.toolbar_title);
        appTitle.setText(fragmentTitle);
        appTitle.setTextColor(getResources().getColor(R.color.white));
    }
}
