package com.codepath.gridimagesearch.activities;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		//Remove action bar on the image display activity
		getActionBar().hide();
		//pull out the fullUrl from the intent
		ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
		//find the image view
		ImageView ivFullImage = (ImageView) findViewById(R.id.ivFullImage);
		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(Html.fromHtml(result.title));
		//load the image url into the image view using picasso
		Picasso.with(this).load(result.fullUrl).into(ivFullImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
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
