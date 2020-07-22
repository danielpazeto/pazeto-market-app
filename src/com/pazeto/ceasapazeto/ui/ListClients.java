package com.pazeto.comercio.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pazeto.comercio.R;
import com.pazeto.comercio.adapter.CustomCursorAdapter;
import com.pazeto.comercio.db.DBFacade;
import com.pazeto.comercio.vo.Client;

public class ListClients extends Activity {

	private static final String TAG = "listClients";
	ListView clientListView;
	DBFacade db;
	CustomCursorAdapter customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		db = new DBFacade(this);
		listClients();

	}

	private void listClients() {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				clientListView = (ListView) findViewById(R.id.listview_clients);
				clientListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent iClient = new Intent(ListClients.this, ClientActivity.class);
						iClient.putExtra(Client.ID, id);
						startActivityForResult(iClient, 1);
					}
				});
				customAdapter = new CustomCursorAdapter(ListClients.this, db
						.listClients(), CustomCursorAdapter.CLIENT);
				clientListView.setAdapter(customAdapter);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			listClients();
		}

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
			startActivityForResult(new Intent(ListClients.this,
					ClientActivity.class), 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
