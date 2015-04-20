package com.pazeto.ceasapazeto.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.db.DBFacade;
import com.pazeto.ceasapazeto.vo.Client;

public class AddClient extends Activity {

	protected static final String TAG = "AddClient";
	EditText etName, etLastName, etTel, etCellPhone1, etCellPhone2, etAddres,
			etHangar, etCity, etDescription;
	DBFacade db;
	SQLiteDatabase sql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_client);

		etName = (EditText) findViewById(R.id.etClientName);
		etLastName = (EditText) findViewById(R.id.etClientLastName);
		etHangar = (EditText) findViewById(R.id.etClientHangar);// erro aki ??
		etTel = (EditText) findViewById(R.id.etClientTel);
		etCellPhone1 = (EditText) findViewById(R.id.etClientCellphone1);
		etCellPhone2 = (EditText) findViewById(R.id.etClientCellphone2);
		etAddres = (EditText) findViewById(R.id.etClientAddress);
		etCity = (EditText) findViewById(R.id.etClientCity);

		db = new DBFacade(this);
		sql = db.getWritableDatabase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
			Client client = new Client();
			client.setName(etName.getText().toString());
			client.setLastname(etLastName.getText().toString());
			client.setCity(etCity.getText().toString());
			client.setTelephone(etTel.getText().toString());
			client.setCellPhone1(etCellPhone1.getText().toString());
			client.setCellPhone2(etCellPhone2.getText().toString());
			// TODO falta outros atributos aki

			if (db.insertClient(client, sql)) {
				setResult(RESULT_OK);
				finish();
			} else {
				Log.d(TAG, "N�o salvou cliente.");
			}
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		if (sql != null) {
			sql.close();
		}
		if (db != null) {
			db.close();
		}

		super.onDestroy();
	}
}
