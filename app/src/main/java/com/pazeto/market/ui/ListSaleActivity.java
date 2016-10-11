package com.pazeto.market.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pazeto.market.R;
import com.pazeto.market.adapter.SaleListAdapter;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.SaleItem;
import com.pazeto.market.widgets.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ListSaleActivity extends DefaultActivity implements TextWatcher {
    private EditText edtCurrentDate;
    private Button btSave;
    private static final String TAG = ListSaleActivity.class.getName();
    private SaleListAdapter adapter;
    HashMap<Long, String> hmClients;
    Client currentClient;
    private long unixDate;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_list_view);

        btSave = (Button) findViewById(R.id.btSave);
        edtCurrentDate = (EditText) findViewById(R.id.tv_date);

        setupAutoCompleteClient();

        edtCurrentDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveSales();
                createAndShowDatePickerDialog();
            }

        });
        btSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveSales();

            }
        });

        // loadSalePerDateAndClient(0, null);
    }

    /**
     * Remove a venda
     *
     * @param v
     */
    public void removeSaleOnClickHandler(View v) {
        final SaleItem itemToRemove = (SaleItem) v.getTag();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Deseja remover o item nº " + v.getId() + ":\n" + itemToRemove.getQuantity() + " - "
                        + adapter.getName(itemToRemove.getIdProduct()) + " - "
                        + itemToRemove.getUnitPrice())
                .setPositiveButton(getResources().getText(R.string.remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adapter.remove(itemToRemove);
                            }
                        })
                .setNegativeButton(getResources().getText(R.string.cancel), null);
        builder.create().show();
    }

    private void setupListViewAdapter(ArrayList<SaleItem> items) {
        adapter = new SaleListAdapter(this, R.layout.add_sale_list_item, items,
                unixDate);
        ListView salesListView = (ListView) findViewById(R.id.sale_ListView);
        salesListView.setAdapter(adapter);
    }

    public void onAddSaleBtnClick(View v) {
        if (isConfigured() && unixDate > 0) {
            adapter.insert(new SaleItem(0, 0, unixDate), 0);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Cliente ou Data inválidos.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void loadSalePerDateAndClient(long date, Client client) {
        ArrayList<SaleItem> salesPerDateAndClient = db.listSalePerDateAndClient(
                date, client, sql);
        if (salesPerDateAndClient.size() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Não há itens para a data: " + date, Toast.LENGTH_SHORT)
                    .show();
        }
        Log.d(TAG, salesPerDateAndClient.size()
                + " resultados para o cliente:  " + client);
        setupListViewAdapter(salesPerDateAndClient);
    }

    protected void createAndShowDatePickerDialog() {
        final DatePickerDialog datePickDialog = new DatePickerDialog(this,
                onSetDatePpickerListener, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickDialog.setTitle("Escolha a data:");
        datePickDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datePickDialog.dismiss();
                    }
                });
        datePickDialog.show();
    }

    private DatePickerDialog.OnDateSetListener onSetDatePpickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            myCalendar.set(Calendar.YEAR, selectedYear);
            myCalendar.set(Calendar.MONTH, selectedMonth);
            myCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);

            String myFormat = "dd/MM/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            edtCurrentDate.setText(sdf.format(myCalendar.getTime()));

            unixDate = Utils.componentTimeToTimestamp(myCalendar);
            if (isConfigured()) {
                loadSalePerDateAndClient(unixDate, currentClient);
            }
        }

    };

    private void saveSales() {
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                SaleItem saleItem = adapter.getItem(i);
                saleItem.setDate(unixDate);
                saleItem.setIdClient(currentClient.getId());
                long id = db.insertSale(saleItem, sql);
                if (id != -1) {
                    System.out.println("salvou com id: " + id);
                    System.out.println("e data" + saleItem.getDate());
                    System.out.println("e id client" + saleItem.getIdClient());
                    adapter.getItem(i).setId(id);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        saveSales();
        super.onBackPressed();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        System.out.println("Cliente autocomplete: " + s.toString());
        if (getKey(s.toString()) != -1) {
            long id = getKey(s.toString());
            currentClient = new Client();
            currentClient.setId(id);
            currentClient.setName(s.toString());
            // holder.productDay.setIdProduct(id);
            System.out.println("Setou id " + id + "  e  name " + s);
            if (isConfigured()) {
                loadSalePerDateAndClient(unixDate, currentClient);
            }
        } else {
            currentClient = null;
        }
    }

    /**
     * Inicializa o autocomplete do cliente
     */
    public void setupAutoCompleteClient() {
        int j = 0;
        hmClients = new HashMap<>();
        Cursor clients = db.listClients();
        String[] myListClients = new String[clients.getCount()];
        while (clients.moveToNext()) {
            long idClient = clients.getInt(clients.getColumnIndex(Client.ID));
            String name = clients
                    .getString(clients.getColumnIndex(Client.NAME));
            myListClients[j] = name;
            hmClients.put(idClient, name);
            j++;
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.dropdown_list_custom,
                myListClients);

        AutoCompleteTextView autoCompProduct = (AutoCompleteTextView) findViewById(R.id.OutputClient);

        autoCompProduct.addTextChangedListener(this);
        autoCompProduct.setThreshold(0);
        autoCompProduct.setAdapter(mAdapter);

    }

    public long getKey(String name) {
        if (hmClients != null) {
            for (long key : hmClients.keySet()) {
                if (hmClients.get(key).equals(name))
                    return key;
            }
        }
        return -1;
    }

    /**
     * Verify if client and date are selected
     *
     * @return true if configured client and date
     */
    public boolean isConfigured() {
        if (currentClient == null) {
            return false;
            // } else if (date == null) {
            // return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveSales();
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setupAutoCompleteClient();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
