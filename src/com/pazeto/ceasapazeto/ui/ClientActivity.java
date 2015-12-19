package com.pazeto.ceasapazeto.ui;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.db.DBFacade;
import com.pazeto.ceasapazeto.vo.Client;

public class ClientActivity extends Activity {

	protected static final String TAG = "AddClient";
	EditText etName, etLastName, etTel, etCellPhone1, etCellPhone2, etAddres,
			etHangar, etCity, etDescription;
	DBFacade db;
	SQLiteDatabase sql;
	Client currentClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_client);

		db = new DBFacade(this);
		sql = db.getWritableDatabase();

		etName = (EditText) findViewById(R.id.etClientName);
		etLastName = (EditText) findViewById(R.id.etClientLastName);
		etHangar = (EditText) findViewById(R.id.etClientHangar);// erro aki ??
		etTel = (EditText) findViewById(R.id.etClientTel);
		etCellPhone1 = (EditText) findViewById(R.id.etClientCellphone1);
		etCellPhone2 = (EditText) findViewById(R.id.etClientCellphone2);
		etAddres = (EditText) findViewById(R.id.etClientAddress);
		etCity = (EditText) findViewById(R.id.etClientCity);

		long idClient = getIntent().getLongExtra(Client.ID, -1);
		System.out.println("AQUIIII"+idClient);
		if (idClient != -1) {
			loadClient(db.getClient(idClient));
		}else{
			currentClient = new Client();
		}
	}

	private void loadClient(Client client) {
		currentClient = client;
		if (client != null) {
			etName.setText(currentClient.getName());
			etLastName.setText(currentClient.getLastname());
			// TODO colocar todos os campos
		}
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
		case R.id.save:
			persistClient();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void persistClient() {
		currentClient.setName(etName.getText().toString());
		currentClient.setLastname(etLastName.getText().toString());
		currentClient.setCity(etCity.getText().toString());
		currentClient.setTelephone(etTel.getText().toString());
		currentClient.setCellPhone1(etCellPhone1.getText().toString());
		currentClient.setCellPhone2(etCellPhone2.getText().toString());
		// TODO falta outros atributos aki

		if (db.persistClient(currentClient, sql)) {
			setResult(RESULT_OK);
			finish();
		} else {
			Log.d(TAG, "N�o salvou cliente.");
		}
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
