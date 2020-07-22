package com.pazeto.comercio.widgets;


import android.content.Context;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;

public class ClientAutoCompletEditText extends AutoCompleteTextView {

    HashMap<Long, String> hmClients;
    Context ctx;

    public ClientAutoCompletEditText(Context context) {
        super(context);
        ctx = context;
    }

//    /**
//     * Inicializa o autocomplete do cliente
//     */
//    public void setClient() {
//        int j = 0;
//        hmClients = new HashMap<>();
//        Cursor clients = db.listCursorClients();
//        String[] myListClients = new String[clients.getCount()];
//        while (clients.moveToNext()) {
//            long idClient = clients.getInt(clients.getColumnIndex(Client.ID));
//            String name = clients
//                    .getString(clients.getColumnIndex(Client.NAME));
//            myListClients[j] = name;
//            hmClients.put(idClient, name);
//            j++;
//        }
//
//        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
//                ctx, R.layout.dropdown_list_custom,
//                myListClients);
//
//        AutoCompleteTextView autoCompProduct = (AutoCompleteTextView) findViewById(R.id.OutputClient);
//
//        autoCompProduct.addTextChangedListener(this);
//        autoCompProduct.setThreshold(0);
//        autoCompProduct.setAdapter(mAdapter);
//    }

}
