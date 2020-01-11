package dev.mammad.simplelistapplication;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dev.mammad.simplelistapplication.adapter.ProductRecyclerAdapter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.http.Fault.MALFORMED_RESPONSE_CHUNK;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String SAMPLE_RESPONSE = "[\n" +
            "  {\n" +
            "    \"id\":\"36802\",\n" +
            "    \"name\":\"Food\",\n" +
            "    \"description\":\"\",\n" +
            "    \"products\":[\n" +
            "      {\n" +
            "        \"id\":\"1\",\n" +
            "        \"categoryId\":\"36802\",\n" +
            "        \"name\":\"Bread\",\n" +
            "        \"url\":\"/Bread.jpg\",\n" +
            "        \"description\":\"\",\n" +
            "        \"salePrice\":{\n" +
            "          \"amount\":\"0.81\",\n" +
            "          \"currency\":\"EUR\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\":\"36803\",\n" +
            "    \"name\":\"Drink\",\n" +
            "    \"description\":\"\",\n" +
            "    \"products\":[\n" +
            "      {\n" +
            "        \"id\":\"1\",\n" +
            "        \"categoryId\":\"36803\",\n" +
            "        \"name\":\"Cola\",\n" +
            "        \"url\":\"/Cola.jpg\",\n" +
            "        \"description\":\"\",\n" +
            "        \"salePrice\":{\n" +
            "          \"amount\":\"0.81\",\n" +
            "          \"currency\":\"EUR\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";
    @Rule
    public ActivityTestRule<MainActivity> activity = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public WireMockRule server = new WireMockRule(wireMockConfig().port(8080), false);

    @BeforeClass
    public static void activateTestProfile() {
        System.setProperty("test-profile", "on");
    }

    @Test
    public void readTimesOut_ShouldDisplayTheToast() {
        stubFor(get("/").willReturn(aResponse().withFixedDelay(2000)));
        reloadActivity();

        sleepUninterruptibly(2, SECONDS);

        onView(withId(R.id.swipeContainer)).check((v, ex) ->
                assertFalse(((SwipeRefreshLayout) v).isRefreshing())
        );
    }

    @Test
    public void faultyResponse_ShouldDisplayTheToast() {
        stubFor(get("/").willReturn(aResponse().withFault(MALFORMED_RESPONSE_CHUNK)));
        reloadActivity();

        onView(withId(R.id.swipeContainer)).check((v, ex) ->
                assertFalse(((SwipeRefreshLayout) v).isRefreshing())
        );
    }

    @Test
    public void okResponse_ShouldDisplayTwoItems() {
        stubFor(get("/").willReturn(aResponse().withBody(SAMPLE_RESPONSE).withStatus(200)));
        reloadActivity();

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

    private void reloadActivity() {
        Intent intent = new Intent();
        activity.launchActivity(intent);
    }
}
