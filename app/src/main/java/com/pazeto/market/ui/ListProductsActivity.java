package com.pazeto.market.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pazeto.market.R;
import com.pazeto.market.adapter.CustomCursorAdapter;
import com.pazeto.market.vo.Product;

public class ListProductsActivity extends DefaultActivity {

	protected static final String TAG = "ListProductsActivity";
	ListView productListView;
	CustomCursorAdapter customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list);
		productListView = (ListView) findViewById(R.id.listview_products);
		productListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent iProd = new Intent(ListProductsActivity.this, EditProductActivity.class);
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
				customAdapter = new CustomCursorAdapter(ListProductsActivity.this, db
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

	public void addNewProduct(View v){
		startActivityForResult(new Intent(ListProductsActivity.this,
				EditProductActivity.class), 1);
	}

}