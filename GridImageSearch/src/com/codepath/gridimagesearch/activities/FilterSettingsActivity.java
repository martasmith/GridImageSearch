
package com.codepath.gridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.gridimagesearch.R;

public class FilterSettingsActivity extends Activity {
    private Spinner sprImageSize, sprColorFilter, sprImageType;
    private TextView tvSiteFilter;

    private ArrayAdapter<CharSequence> aSprImageSize, aSprColorFilter, aSprImageType;
    private String imageSizeValue, colorFilterValue, imageTypeValue, siteFilterValue;
    int spinnerPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_settings);
        // get data sources
        sprImageSize = (Spinner) findViewById(R.id.sprImageSize);
        sprColorFilter = (Spinner) findViewById(R.id.sprColorFilter);
        sprImageType = (Spinner) findViewById(R.id.sprImageType);
        tvSiteFilter = (TextView) findViewById(R.id.etSiteFilter);
        // create data source adapters
        aSprImageSize = ArrayAdapter.createFromResource(this, R.array.imageSizeArr,
                R.layout.item_custom_textview);
        aSprColorFilter = ArrayAdapter.createFromResource(this, R.array.colorFilterArr,
                R.layout.item_custom_textview);
        aSprImageType = ArrayAdapter.createFromResource(this, R.array.imageTypeArr,
                R.layout.item_custom_textview);
        // add data source to adapter
        sprImageSize.setAdapter(aSprImageSize);
        sprColorFilter.setAdapter(aSprColorFilter);
        sprImageType.setAdapter(aSprImageType);
        // populate data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("imageSizeValue") && (extras.getString("imageSizeValue") != "")) {
                imageSizeValue = extras.getString("imageSizeValue");
                spinnerPos = aSprImageSize.getPosition(imageSizeValue);
                sprImageSize.setSelection(spinnerPos);
            }
            if (extras.containsKey("colorFilterValue")
                    && (extras.getString("colorFiterValue") != "")) {
                colorFilterValue = extras.getString("colorFilterValue");
                int spinnerPos = aSprColorFilter.getPosition(colorFilterValue);
                sprColorFilter.setSelection(spinnerPos);
            }
            if (extras.containsKey("imageTypeValue") && (extras.getString("imageTypeValue") != "")) {
                imageTypeValue = extras.getString("imageTypeValue");
                spinnerPos = aSprImageType.getPosition(imageTypeValue);
                sprImageType.setSelection(spinnerPos);
            }
            if (extras.containsKey("siteFilterValue")
                    && (extras.getString("siteFilterValue") != "")) {
                siteFilterValue = extras.getString("siteFilterValue");
                tvSiteFilter.setText(siteFilterValue);
            }
        }
    }

    public void onFilterSave(View v) {
        // get the currently selected data from the spinners and the EditText
        imageSizeValue = sprImageSize.getSelectedItem().toString();
        colorFilterValue = sprColorFilter.getSelectedItem().toString();
        imageTypeValue = sprImageType.getSelectedItem().toString();
        siteFilterValue = tvSiteFilter.getText().toString();
        // start an intent
        Intent data = new Intent(FilterSettingsActivity.this, SearchActivity.class);
        // pass selected data via the intent
        data.putExtra("imageSizeValue", imageSizeValue);
        data.putExtra("colorFilterValue", colorFilterValue);
        data.putExtra("imageTypeValue", imageTypeValue);
        data.putExtra("siteFilterValue", siteFilterValue);
        // startActivityForResult(i,REQUEST_CODE);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onFilterReset(View v) {
        // set the currently selected data of the spinners and the EditText to default
        sprImageSize.setSelection(0);
        sprColorFilter.setSelection(0);
        sprImageType.setSelection(0);
        tvSiteFilter.setText("");
    }
}
