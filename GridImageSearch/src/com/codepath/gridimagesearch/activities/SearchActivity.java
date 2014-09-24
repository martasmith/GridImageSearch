package com.codepath.gridimagesearch.activities;

import java.util.ArrayList;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapters.EndlessScrollListener;
import com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;


public class SearchActivity extends Activity {
	private EditText etQuery;
	private GridView gvResults;
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;
	private final int REQUEST_CODE = 10;
	private String searchFilters,imageSizeValue,colorFilterValue,imageTypeValue,siteFilterValue;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        searchFilters = "";
        //creates the data source
        imageResults = new ArrayList<ImageResult>();
        //attaches the data source to an adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        //link the adapter to the adapterview (gridview)
        gvResults.setAdapter(aImageResults);
    }
    
    private void setupViews() {
    	etQuery = (EditText) findViewById(R.id.etQuery);
    	gvResults = (GridView) findViewById(R.id.gvResults);
    	gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//launch the image display activity
				//create an intent
				Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
				//get the image result to display
				ImageResult currentRes = imageResults.get(position);
				//pass image result to the intent
				// in order to pass an entire object via an intent, our model must be serializable or parcelable
				i.putExtra("result", currentRes);
				//launch the new activity
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
    
	protected void customLoadMoreDataFromApi(int offset) {
	   	String query = etQuery.getText().toString();
	   	AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + 
        		query + 
        		"&rsz=8" + 
        		"&start=" + offset +
        		searchFilters;
        Log.d("DEBUG", "query: " + searchUrl);
        client.get(searchUrl, new JsonHttpResponseHandler() {
       	 @Override
       	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
       		//JSONArray imageResultsJson = null;
       		try {
       			Object responseData = response.getJSONObject("responseData");
       			if (responseData != JSONObject.NULL)
       			{
       				JSONArray imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
           			if (imageResultsJson.length() == 0) {
           				Toast.makeText(getBaseContext(), "Your Search returned no results. Please try a different search term!", Toast.LENGTH_LONG).show();
           			}
           			// when changing the adapter, it modifies the underlying data
           			aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
       			}
       			
       		} catch (JSONException e) {
       			e.printStackTrace();
       		}
       		//Log.i("INFO",imageResults.toString());
       	}
       	 
       	@Override
       		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
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
	        	if (extras.containsKey("imageSizeValue") && (extras.getString("imageSizeValue") != "")) {
	        		imageSizeValue = extras.getString("imageSizeValue");
	        		searchFilters += "&imgsz="+imageSizeValue;
	        	}
	        	if (extras.containsKey("colorFilterValue") && (extras.getString("colorFilterValue") != "")) {
	        		colorFilterValue = extras.getString("colorFilterValue");
	        		searchFilters += "&imgcolor="+colorFilterValue;
	        	}
	        	if (extras.containsKey("imageTypeValue") && (extras.getString("imageTypeValue") != "")) {
	        		imageTypeValue = extras.getString("imageTypeValue");
	        		searchFilters += "&imgtype="+imageTypeValue;
	        	}
	        	if (extras.containsKey("siteFilterValue") && (extras.getString("sizeFilterValue") != "")) {
	        		siteFilterValue = extras.getString("siteFilterValue");
	        			searchFilters += "&as_sitesearch="+siteFilterValue;
	        	}
	        } 
		}
		
	}
     
    public void onImageSearch(View v) {
    	aImageResults.clear();
    	customLoadMoreDataFromApi(0);
      
    }
    
    public void onSettings(MenuItem mi) {
    	//We need to start an intent for result, pass search query + filter options
    	Intent i = new Intent(SearchActivity.this, FilterSettingsActivity.class);
    	//get the search query from the edit text
    	i.putExtra("imageSizeValue", imageSizeValue);
    	i.putExtra("colorFilterValue", colorFilterValue);
    	i.putExtra("imageTypeValue", imageTypeValue);
    	i.putExtra("siteFilterValue", siteFilterValue);
    	//send filter settings back to the filter page
    	startActivityForResult(i,REQUEST_CODE);	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
