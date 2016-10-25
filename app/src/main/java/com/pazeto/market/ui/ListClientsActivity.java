package com.pazeto.market.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pazeto.market.R;
import com.pazeto.market.adapter.CustomCursorAdapter;
import com.pazeto.market.vo.Client;

public class ListClientsActivity extends DefaultActivity {

	private static final String TAG = "listCursorClients";
	ListView clientListView;
	CustomCursorAdapter customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_list);

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
						.listCursorClients(), CustomCursorAdapter.CLIENT);
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

	public void addNewClient(View v){
		startActivityForResult(new Intent(ListClientsActivity.this,
				EditClientActivity.class), 1);
	}
}
