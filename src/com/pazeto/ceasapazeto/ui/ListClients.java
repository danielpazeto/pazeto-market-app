package com.pazeto.ceasapazeto.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.adapter.CustomCursorAdapter;
import com.pazeto.ceasapazeto.db.DBFacade;

public class ListClients extends Activity {

	private static final String TAG = "listClients";
	protected static final int LIST_CLIENTS = 2;
	ListView clientListView;
	Button newClient;
	DBFacade db;
	CustomCursorAdapter customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("Entrou", "Activity listClientes");
		setContentView(R.layout.client_list);
		db = new DBFacade(this);

		newClient = (Button) findViewById(R.id.bt_add_client);
		clientListView = (ListView) findViewById(R.id.listview_clients);

		clientListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("Cliente", "clicked on item: " + position);
			}
		});

		newClient.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(ListClients.this,
						AddClient.class), 1);

			}
		});
		listClients();

	}

	private void listClients() {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				customAdapter = new CustomCursorAdapter(ListClients.this, db
						.listClients(), LIST_CLIENTS);
				clientListView.setAdapter(customAdapter);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			listClients();
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		if (db != null) {
			db.close();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
			startActivityForResult(
					new Intent(ListClients.this, AddClient.class), 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
