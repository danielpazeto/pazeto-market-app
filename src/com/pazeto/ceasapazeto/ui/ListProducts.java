package com.pazeto.market.ui;

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

import com.pazeto.market.R;
import com.pazeto.market.adapter.CustomCursorAdapter;
import com.pazeto.market.db.DBFacade;
import com.pazeto.market.vo.Product;

public class ListProducts extends Activity {

	protected static final String TAG = "ListProducts";
	ListView productListView;
	DBFacade db;
	CustomCursorAdapter customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.product_list);
		db = new DBFacade(this);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		productListView = (ListView) findViewById(R.id.listview_products);
		productListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent iProd = new Intent(ListProducts.this, ProductActivity.class);
				iProd.putExtra(Product.ID, id);
				startActivityForResult(iProd, 1);
			}
		});

		listProducts();

	}

	private void listProducts() {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				customAdapter = new CustomCursorAdapter(ListProducts.this, db
						.listProducts(), CustomCursorAdapter.PRODUCT);
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
					ProductActivity.class), 1);
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
