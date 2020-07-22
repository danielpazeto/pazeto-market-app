package com.pazeto.comercio.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.pazeto.comercio.R;
import com.pazeto.comercio.db.DBFacade;
import com.pazeto.comercio.vo.Product;

public class ProductActivity extends Activity {

	protected static final String TAG = "addProcudt";
	Product currentProduct;

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

		long productId = getIntent().getLongExtra(Product.ID, -1);
		if (productId != -1) {
			loadProduct(db.getProduct(productId));
		} else {
			currentProduct = new Product();
		}

	}

	private void loadProduct(Product prod) {
		etName.setText(prod.getName());
		etDesc.setText(prod.getDescription());
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
			String nameProd = etName.getText().toString();
			String descProd = etDesc.getText().toString();

			if (nameProd.length() > 0 && descProd.length() > 0) {
				Product prod = new Product();
				prod.setName(nameProd);
				prod.setDescription(descProd);

				if (db.insertProduct(prod)) {
					setResult(RESULT_OK);
					finish();
				} else {
					Log.d(TAG, "N�o salvou produto.");
				}
				return true;
			}else{
				//TODO RAISE EXCEPTION AVISANDO USUARIO que falta algo no nome ou na desc
				return false;
			}
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
