package com.pazeto.market.ui;

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

import com.pazeto.market.R;
import com.pazeto.market.adapter.CustomCursorAdapter;
import com.pazeto.market.db.DBFacade;
import com.pazeto.market.vo.Client;

public class ListClientsActivity extends Activity {

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
						Intent iClient = new Intent(ListClientsActivity.this, EditClientActivity.class);
						iClient.putExtra(Client.ID, id);
						startActivityForResult(iClient, 1);
					}
				});
				customAdapter = new CustomCursorAdapter(ListClientsActivity.this, db
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
			startActivityForResult(new Intent(ListClientsActivity.this,
					EditClientActivity.class), 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
