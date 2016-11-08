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
import com.pazeto.market.vo.Product;

public class ListClientsActivity extends DefaultActivity {

    private static final String TAG = ListClientsActivity.class.getName();
    public static String IS_TO_SELECT_CLIENT = "selectClient";

    ListView clientListView;
    CustomCursorAdapter customAdapter;
    private boolean isToSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isToSelect = getIntent().getExtras().getBoolean(IS_TO_SELECT_CLIENT);
        if (isToSelect) {
            setTheme(R.style.PopupTheme);
            setTitle(R.string.choose_client);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_list);

        clientListView = (ListView) findViewById(R.id.listview_clients);
        clientListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (isToSelect) {
                    Intent intent = new Intent();
                    intent.putExtra(Client.ID, id);
                    intent.putExtra(ListClientsActivity.IS_TO_SELECT_CLIENT, isToSelect);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent iClient = new Intent(ListClientsActivity.this, EditClientActivity.class);
                    iClient.putExtra(Client.ID, id);
                    startActivityForResult(iClient, 1);
                }
            }
        });

        listClients();
    }

    private void listClients() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
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

    public void addNewClient(View v) {
        startActivityForResult(new Intent(ListClientsActivity.this,
                EditClientActivity.class), 1);
    }
}
