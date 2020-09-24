package com.pazeto.comercio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pazeto.comercio.R;
import com.pazeto.comercio.adapter.ClientProductAdapter;
import com.pazeto.comercio.db.FirebaseHandler;
import com.pazeto.comercio.utils.Constants;
import com.pazeto.comercio.vo.Client;

import java.util.ArrayList;
import java.util.List;

public class ListClientsActivity extends DefaultActivity implements ClientProductAdapter.RecyclerViewClickListener {

    private static final String TAG = ListClientsActivity.class.getName();
    public static String IS_TO_SELECT_CLIENT = "com.pazeto.comercio.ListClientsActivity.IS_TO_SELECT";

    RecyclerView clietRecyclerView;
    ClientProductAdapter customAdapter;
    private boolean isToSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loadClients();

        if (getIntent().getExtras() != null) {
            isToSelect = getIntent().getExtras().getBoolean(IS_TO_SELECT_CLIENT);
            if (isToSelect) {
                setFinishOnTouchOutside(false);
                setTheme(R.style.PopupTheme);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_list);

        clietRecyclerView = findViewById(R.id.clients_recycler_view);
        clietRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdapter = new ClientProductAdapter(ClientProductAdapter.CLIENT, ListClientsActivity.this);
        clietRecyclerView.setAdapter(customAdapter);


    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Client client = (Client) customAdapter.getItem(position);
        String id = client.getId();
        if (isToSelect) {
            onClientSelected(client);
        } else {
            Intent iClient = new Intent(ListClientsActivity.this, EditClientActivity.class);
            iClient.putExtra(Constants.INTENT_EXTRA_CLIENT, id);
            startActivityForResult(iClient, 1);
        }
    }

    private void onClientSelected(Client client) {
        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_EXTRA_CLIENT, client);
        intent.putExtra(ListClientsActivity.IS_TO_SELECT_CLIENT, isToSelect);
        setResult(RESULT_OK, intent);
        finish();
    }


    public void loadClients() {
        new FirebaseHandler(getApplicationContext()).getClientsCollectionReference().get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Client> clientList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Client client = document.toObject(Client.class);
                            client.setId(document.getId());
                            clientList.add(client);
                        }
                        customAdapter.setItems(clientList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (isToSelect) {
                Client newSelectedClient = (Client) data.getSerializableExtra(Constants.INTENT_EXTRA_CLIENT);
                onClientSelected(newSelectedClient);
            } else {
                loadClients();
            }
        }

    }

    public void addNewClient(View v) {
        startActivityForResult(new Intent(ListClientsActivity.this,
                EditClientActivity.class), 1);
    }


}
