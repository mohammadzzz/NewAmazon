package dev.mammad.simplelistapplication;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import dev.mammad.simplelistapplication.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;
    private TextView appTitle;

    public void startFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_from_right,
                R.anim.enter_from_right, R.anim.exit_from_left);

        transaction.replace(R.id.container, fragment, String.valueOf(fragment.getId()));
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
            startFragment(MainFragment.newInstance());
        }
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_white));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

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
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            } else {
                super.onBackPressed();
            }
        }
    }

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

    public void setActionBarTitleAndColor(CharSequence fragmentTitle) {
        appTitle = toolbar.findViewById(R.id.toolbar_title);
        appTitle.setText(fragmentTitle);
        appTitle.setTextColor(getResources().getColor(R.color.white));
    }
}
