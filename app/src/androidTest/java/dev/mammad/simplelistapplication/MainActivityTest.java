package dev.mammad.simplelistapplication;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

import dev.mammad.simplelistapplication.adapter.ProductRecyclerAdapter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.http.Fault.MALFORMED_RESPONSE_CHUNK;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.Arrays.copyOf;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * UI Tests for the {@link MainActivity}.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    /**
     * A sample successful API response with just two products and two categories.
     */
    private static final String TWO_PRODUCTS_RESPONSE = readFile("assets/two-products.json");

    /**
     * A sample successful API response with no products and no categories.
     */
    private static final String NO_PRODUCTS_RESPONSE = readFile("assets/no-products.json");

    /**
     * An extended response to test scroll behavior.
     */
    private static final String SCROLLABLE_RESPONSE = readFile("assets/scrollable-response.json");

    /**
     * Activity under test.
     */
    @Rule
    public ActivityTestRule<MainActivity> activity = new ActivityTestRule<>(MainActivity.class);

    /**
     * Setups a Wiremock server to mimic different API behaviors before all tests and
     * tears it down after all those tests.
     */
    @Rule
    public WireMockRule server = new WireMockRule(wireMockConfig().port(8080), false);

    /**
     * Activates the test profile. The test profile may have different configurations compared to
     * the actual profile.
     */
    @BeforeClass
    public static void activateTestProfile() {
        System.setProperty("test-profile", "on");
    }

    /**
     * Disables the Tests profile after all tests.
     */
    @AfterClass
    public static void disableTheTestProfile() {
        System.clearProperty("test-profile");
    }

    @Test
    public void mainActivity_WhenReadTimesOut_ShouldDisplayNoInternetImage() {
        stubFor(get("/").willReturn(aResponse().withFixedDelay(2000)));
        reloadActivity();

        sleepUninterruptibly(2, SECONDS);

        onView(withId(R.id.swipe_container)).check((v, ex) ->
                assertFalse(((SwipeRefreshLayout) v).isRefreshing())
        );
    }

    @Test
    public void mainActivity_FaultyResponse_ShouldDisplayNoInternetImage() {
        stubFor(get("/").willReturn(aResponse().withFault(MALFORMED_RESPONSE_CHUNK)));
        reloadActivity();

        onView(withId(R.id.swipe_container)).check((v, ex) ->
                assertFalse(((SwipeRefreshLayout) v).isRefreshing())
        );

        onView(withId(R.id.main_fragment_network_error)).check(matches(isDisplayed()));
    }

    @Test
    public void ok_WithTwoProductsResponse_ShouldDisplayThoseTwoItems() {
        whenReturningTwoProducts();

        // At application start, we should have two products
        onView(withId(R.id.recycler_view)).check((v, ex) -> {
            RecyclerView view = (RecyclerView) v;
            ProductRecyclerAdapter adapter = (ProductRecyclerAdapter) view.getAdapter();

            // The number of items
            //noinspection ConstantConditions
            int itemCount = adapter.getItemCount();
            assertEquals(2, itemCount);

            //noinspection ConstantConditions
            int[] placeholder = activity.getActivity().getDrawable(R.drawable.placeholder).getState();

            // Inspecting the First Item
            assertEquals("Bread", getTitleOf(view, 0));
            assertArrayEquals(placeholder, getImageOf(view, 0));

            // Inspecting the Second Item
            assertEquals("Cola", getTitleOf(view, 1));
            assertArrayEquals(placeholder, getImageOf(view, 1));
        });
    }

    @Test
    public void clickingOnOneProduct_WhenThereAreProducts_ShouldShowTheDetailedView() {
        whenReturningTwoProducts();

        // Clicking on one Product
        onView(withText("Cola")).perform(click());

        // There is no category fab in the detailed page
        onView(withId(R.id.category_list)).check((v, ex) -> {
            assertNull(v);
            assertNotNull(ex);
        });

        onView(withId(R.id.detail_fragment_header_price)).check(matches(withText("0.81EUR")));
        onView(withId(R.id.detail_fragment_header_name)).check(matches(withText("Cola")));
    }

    @Test
    public void clickingOnFab_WhenThereAreCategories_ShouldDisplayThem() {
        whenReturningTwoProducts();

        ViewInteraction theFab = onView(withId(R.id.fab));

        // At first, there is no category list
        onView(withId(R.id.category_list)).check((v, ex) -> {
            assertNull(v);
            assertNotNull(ex);
        });

        // Clicking on the category fab
        theFab
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .perform(click());

        // Then category list should contain two items as expected
        onView(withId(R.id.category_list))
                .check(matches(isDisplayed()))
                .check((v, ex) -> {
                    RecyclerView view = (RecyclerView) v;

                    assertEquals("Food", getCategoryTitleOf(view, 0));
                    assertEquals("Drink", getCategoryTitleOf(view, 1));
                });
    }

    @Test
    public void ok_WhenGettingEmptyList_ShouldShowEmptyListErrorText() {
        stubFor(get("/").willReturn(aResponse().withBody(NO_PRODUCTS_RESPONSE).withStatus(200)));
        reloadActivity();
        onView(withId(R.id.main_fragment_empty_list_error))
                .check(matches(isDisplayed()));
        onView(withId(R.id.main_fragment_network_error))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void clickingOnBack_ShouldShowFirstFragment() {
        whenReturningTwoProducts();

        // Clicking on one Product
        onView(withText("Cola")).perform(click());
        Espresso.pressBack();

        onView(withId(R.id.recycler_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickingOnCategory_WhenThereAreCategories_ShouldFilterOutProducts() {
        whenReturningTwoProducts();

        // Clicking on the Fab and then selecting the Drink category
        onView(withId(R.id.fab)).perform(click());
        onView(withText("Drink")).perform(click());

        // First, Category list should be disappeared
        onView(withId(R.id.category_list)).check((v, ex) -> {
            assertNull(v);
            assertNotNull(ex);
        });

        // And we should have just one product
        onView(withId(R.id.recycler_view)).check((v, ex) -> {
            RecyclerView view = (RecyclerView) v;
            ProductRecyclerAdapter adapter = (ProductRecyclerAdapter) view.getAdapter();

            // The number of items after filter
            //noinspection ConstantConditions
            int itemCount = adapter.getItemCount();
            assertEquals(1, itemCount);

            //noinspection ConstantConditions
            int[] placeholder = activity.getActivity().getDrawable(R.drawable.placeholder).getState();

            assertEquals("Cola", getTitleOf(view, 0));
            assertArrayEquals(placeholder, getImageOf(view, 0));
        });
    }

    @Test
    public void ok_WhenPullToRefresh_ShouldReFetchTheData() throws InterruptedException {
        whenReturningTwoProducts();

        // At first, it contains two products
        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(2)));

        // Telling the API to return more on subsequent calls
        stubFor(get("/").willReturn(aResponse().withBody(SCROLLABLE_RESPONSE).withStatus(200)));

        // Pulling to Refresh
        onView(withId(R.id.swipe_container)).perform(swipeDown());

        // Now it contains 4 products within the current scroll
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(4)));
    }

    private View getViewAtGivenPosition(RecyclerView recyclerView, int position) {
        //noinspection ConstantConditions
        return recyclerView.getLayoutManager().findViewByPosition(position);
    }

    private CharSequence getTitleOf(RecyclerView view, int position) {
        View item = getViewAtGivenPosition(view, position);
        return ((TextView) item.findViewById(R.id.product_title)).getText();
    }

    private int[] getImageOf(RecyclerView view, int position) {
        View item = getViewAtGivenPosition(view, position);
        return ((ImageView) item.findViewById(R.id.product_image)).getDrawable().getState();
    }

    private void whenReturningTwoProducts() {
        stubFor(get("/").willReturn(aResponse().withBody(TWO_PRODUCTS_RESPONSE).withStatus(200)));
        reloadActivity();
    }

    private void reloadActivity() {
        Intent intent = new Intent();
        activity.launchActivity(intent);
    }

    private CharSequence getCategoryTitleOf(RecyclerView view, int position) {
        View item = getViewAtGivenPosition(view, position);
        return ((TextView) item.findViewById(R.id.category_list_item_name)).getText();
    }

    private static String readFile(String path) {
        //noinspection ConstantConditions
        try (InputStream resource = MainActivity.class.getClassLoader().getResourceAsStream(path)) {
            byte[] buffer = new byte[8192];
            int read;
            StringBuilder content = new StringBuilder();
            while ((read = resource.read(buffer)) != -1) {
                content.append(new String(copyOf(buffer, read)));
            }
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read the file", e);
        }
    }
}
