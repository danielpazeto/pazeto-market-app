package com.pazeto.ceasapazeto.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.db.DBFacade;
import com.pazeto.ceasapazeto.vo.Product;

public class AddProduct extends Activity {

	protected static final String TAG = "addProcudt";
	EditText etName;
	EditText etDesc;
	DBFacade db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_prod);

		etName = (EditText) findViewById(R.id.cadprod_name);
		etDesc = (EditText) findViewById(R.id.cadprod_desc);
		db = new DBFacade(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit_product_client, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.save:
			Product prod = new Product();
			prod.setName(etName.getText().toString());
			prod.setDescription(etDesc.getText().toString());

			if (db.insertProduct(prod)) {
				setResult(RESULT_OK);
				finish();
			} else {
				Log.d(TAG, "N�o salvou produto.");
			}
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
