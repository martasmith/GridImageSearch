
package com.codepath.gridimagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapters.EndlessScrollListener;
import com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch.fragments.FilterSettingsFragment;
import com.codepath.gridimagesearch.fragments.FilterSettingsFragment.OnFilterSettingsFragmentListener;
import com.codepath.gridimagesearch.models.ImageResult;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends FragmentActivity implements OnFilterSettingsFragmentListener {
    // private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private final int REQUEST_CODE = 10;
    private String searchFilters, imageSizeValue, colorFilterValue, imageTypeValue,
    siteFilterValue, searchStr;
    private SearchView searchView;
    private StaggeredGridView gvResults;
    private RelativeLayout rlSplashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();

        // getWindow().getDecorView().setBackgroundResource(R.drawable.srch_background);
        // creates the data source
        imageResults = new ArrayList<ImageResult>();
        // attaches the data source to an adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        // link the adapter to the adapterview (gridview)
        gvResults.setAdapter(aImageResults);
    }

    private void setupViews() {
        rlSplashScreen = (RelativeLayout) findViewById(R.id.rlSplashScreen);
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        gvResults.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // launch the image display activity
                // create an intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                // get the image result to display
                ImageResult currentRes = imageResults.get(position);
                // pass image result to the intent
                // in order to pass an entire object via an intent, our model must be serializable
                // or parcelable
                i.putExtra("result", currentRes);
                // launch the new activity
                startActivity(i);
            }
        });

        // set up scrolling mechanism
        gvResults.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(totalItemsCount);
            }
        });

    }

    // Check if there is network connectivity
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected void customLoadMoreDataFromApi(int offset) {

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "The internet is currently unavailable. Please try again later!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (searchStr == null) {
            Toast.makeText(getApplicationContext(), "Please enter a search term", Toast.LENGTH_LONG)
            .show();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();

        searchFilters = getSearchFilters();

        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" +
                searchStr +
                "&rsz=8" +
                "&start=" + offset +
                searchFilters;
        Log.d("martas", "query: " + searchUrl);

        client.get(searchUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJson = null;
                try {

                    if (!response.isNull("responseData")) {
                        imageResultsJson = response.getJSONObject("responseData")
                                .getJSONArray("results");
                        if (imageResultsJson.length() == 0) {
                            Toast.makeText(
                                    getBaseContext(),
                                    "Your Search returned no results. Please try a different search term!",
                                    Toast.LENGTH_LONG).show();
                        }
                        // when changing the adapter, it modifies the underlying data
                        aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Log.i("INFO",imageResults.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                Toast.makeText(SearchActivity.this,
                        "Uable to connect to the internet", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable e) {
                Toast.makeText(SearchActivity.this,
                        "Uable to fetch search results from Google API", Toast.LENGTH_LONG).show();
                Log.d("ERROR", e.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // clear out filters to be regenerated
            searchFilters = "";
            // check if intent results are passed back and collect data
            // Retrieve data Strings from Intent
            Bundle extras = data.getExtras();
            if (extras != null) {
                if (extras.containsKey("imageSizeValue")
                        && (extras.getString("imageSizeValue") != "")) {
                    imageSizeValue = extras.getString("imageSizeValue");
                    searchFilters += "&imgsz=" + imageSizeValue;
                }
                if (extras.containsKey("colorFilterValue")
                        && (extras.getString("colorFilterValue") != "")) {
                    colorFilterValue = extras.getString("colorFilterValue");
                    searchFilters += "&imgcolor=" + colorFilterValue;
                }
                if (extras.containsKey("imageTypeValue")
                        && (extras.getString("imageTypeValue") != "")) {
                    imageTypeValue = extras.getString("imageTypeValue");
                    searchFilters += "&imgtype=" + imageTypeValue;
                }
                if (extras.containsKey("siteFilterValue")
                        && (extras.getString("sizeFilterValue") != "")) {
                    siteFilterValue = extras.getString("siteFilterValue");
                    searchFilters += "&as_sitesearch=" + siteFilterValue;
                }
            }
            doImageSearch();
        }
    }

    public void doImageSearch() {
        aImageResults.clear();
        if (rlSplashScreen.getVisibility() == View.VISIBLE) {
            switchSplashScreenOff(true);
        }
        customLoadMoreDataFromApi(0);
    }

    public void switchSplashScreenOff(boolean switchFlag) {
        if (switchFlag) {
            rlSplashScreen.setVisibility(View.GONE);
            gvResults.setVisibility(View.VISIBLE);
        } else {
            rlSplashScreen.setVisibility(View.VISIBLE);
            gvResults.setVisibility(View.GONE);
        }
    }

    private String getSearchFilters() {
        searchFilters = "";
        if (!TextUtils.isEmpty(imageSizeValue)) {
            searchFilters += "&imgsz=" + imageSizeValue;
        }
        if (!TextUtils.isEmpty(colorFilterValue)) {
            searchFilters += "&imgcolor=" + colorFilterValue;
        }
        if (!TextUtils.isEmpty(imageTypeValue)) {
            searchFilters += "&imgtype=" + imageTypeValue;
        }
        if (!TextUtils.isEmpty(siteFilterValue)) {
            searchFilters += "&as_sitesearch=" + siteFilterValue;
        }
        return searchFilters;
    }

    private void showFilterSettings() {
        FragmentManager fm = getFragmentManager();
        FilterSettingsFragment fragment = FilterSettingsFragment.newInstance(imageSizeValue,
                colorFilterValue, imageTypeValue, siteFilterValue);
        fragment.show(fm, "Filter_Settings_fragment");
    }

    public void onSettings(MenuItem mi) {
        showFilterSettings();
    }

    @Override
    public void OnFilterSet(String imgSize, String colorFilter, String imgType, String siteFilter) {
        imageSizeValue = imgSize;
        colorFilterValue = colorFilter;
        imageTypeValue = imgType;
        siteFilterValue = siteFilter;
        doImageSearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.miSearch);
        searchView = (SearchView) searchItem.getActionView();

        searchItem.setOnActionExpandListener(new OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // TODO Auto-generated method stub
                if (rlSplashScreen.getVisibility() != View.VISIBLE) {
                    switchSplashScreenOff(false);
                }
                return true;
            }
        });

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchStr = query;
                doImageSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
