package com.pazeto.market.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.pazeto.market.R;

public class Home extends Activity {

	LinearLayout btCadProd, btClients;
	LinearLayout btListStock, btSales;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		btCadProd = (LinearLayout) findViewById(R.id.ll_cad_prod);
		btSales = (LinearLayout) findViewById(R.id.ll_btSell);
		btClients = (LinearLayout) findViewById(R.id.ll_btn_client);
		btListStock = (LinearLayout) findViewById(R.id.ll_bt_add_prod_stock);
		btCadProd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(Home.this, ListProductsActivity.class);
				startActivity(i);

			}
		});

		btSales.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(Home.this, EditSaleActivity.class);
				startActivity(i);

			}
		});
		btListStock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(Home.this, ListProductStockActivity.class);
				startActivity(i);

			}
		});
		btClients.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(Home.this, ListClientsActivity.class);
				startActivity(i);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void dropTables(View v) {
		// SQLiteDatabase db = null;
		// try {
		// db.execSQL(UsuarioDB.DROP_TABLE_USUARIO);
		// db.execSQL(Client.DROP_TABLE_CLIENTE);
		// db.execSQL(Product.DROP_TABLE_PRODUCT);
		// db.execSQL(ProductDay.DROP_TABLE_PRODUCTDAY);
		// db.execSQL(Sale.DROP_TABLE_SALE);
		// Log.d("DBFacade", "Criou Tabelas");
		// } catch (Exception e) {
		// Log.e("db", "Failure on create tables " + e);
		// }

		System.out.println("OI");
		// para testar o egrador de pdf
		// Utils.createPDF();
		// rpgen.main(null);

	}
}
