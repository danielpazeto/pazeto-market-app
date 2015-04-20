package com.pazeto.ceasapazeto.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.adapter.CustomCursorAdapter;
import com.pazeto.ceasapazeto.db.DBFacade;

public class ListProducts extends Activity {

	protected static final String TAG = "ListProducts";
	protected static final int LIST_PRODUCT = 1;
	ListView productListView;
	DBFacade db;
	CustomCursorAdapter customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("Entrou", "Activity listProducts");
		setContentView(R.layout.product_list);
		db = new DBFacade(this);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		productListView = (ListView) findViewById(R.id.listview_products);
		productListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(TAG, "clicked on item: " + position);
			}
		});

		listProducts();

	}

	private void listProducts() {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				customAdapter = new CustomCursorAdapter(ListProducts.this, db
						.listProducts(), LIST_PRODUCT);
				productListView.setAdapter(customAdapter);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			listProducts();
		}
		Log.d(TAG, requestCode + " / " + resultCode + "  / " + data);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.new_item:
			startActivityForResult(new Intent(ListProducts.this,
					AddProduct.class), 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		if (db != null) {
			db.close();
		}

		super.onDestroy();
	}

}
