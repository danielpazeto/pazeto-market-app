package com.pazeto.market.ui;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pazeto.market.R;
import com.pazeto.market.adapter.SaleReportAdapter;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.SaleItem;
import com.pazeto.market.widgets.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GenerateReportActivity extends DefaultActivity implements TextWatcher {
    private EditText edtInitialDate;
    private EditText edtFinalDate;
    private static final String TAG = GenerateReportActivity.class.getName();
    private SaleReportAdapter adapter;
    HashMap<Long, String> hmClients;
    private Client currentClient;

    enum DATE_TYPE{
        INITIAL,FINAL
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_sale_view);

        edtInitialDate = (EditText) findViewById(R.id.edt_initial_date);
        edtFinalDate = (EditText) findViewById(R.id.edt_final_date);

        setupAutoCompleteClient();

        edtInitialDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createAndShowDatePickerDialog(DATE_TYPE.INITIAL);
            }

        });
        edtFinalDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createAndShowDatePickerDialog(DATE_TYPE.FINAL);
            }

        });
        setupListViewAdapter();
    }

//    /**
//     * Remove a venda
//     *
//     * @param v
//     */
//    public void removeSaleOnClickHandler(View v) {
//        final SaleItem itemToRemove = (SaleItem) v.getTag();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(
//                "Deseja remover o item nº " + v.getId() + ":\n" + itemToRemove.getQuantity() + " - "
//                        + adapter.getName(itemToRemove.getIdProduct()) + " - "
//                        + itemToRemove.getUnitPrice())
//                .setPositiveButton(getResources().getText(R.string.remove),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                adapter.remove(itemToRemove);
//                            }
//                        })
//                .setNegativeButton(getResources().getText(R.string.cancel), null);
//        builder.create().show();
//    }

    private void setupListViewAdapter() {
        adapter = new SaleReportAdapter(this, R.layout.report_sale_list_item);
        ListView salesListView = (ListView) findViewById(R.id.sale_ListView);
        salesListView.setAdapter(adapter);
    }


    SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);

    public void onGenerateReport(View v) throws ParseException {
        Calendar calendarFrom = Calendar.getInstance();
        Calendar calendarUntil = Calendar.getInstance();
        calendarFrom.setTime(dateFormat.parse(edtInitialDate.getText().toString()));
        calendarUntil.setTime(dateFormat.parse(edtFinalDate.getText().toString()));
        long initialDate = Utils.calendarIntanceToStartSecondsDay(calendarFrom);
        long finalDate = Utils.calendarIntanceToStartSecondsDay(calendarUntil);
        if (initialDate > 0 && finalDate > 0 && currentClient != null) {
            HashMap<Long, List<SaleItem>> salesPerDateAndClient = db.listSaleAndStockPerDateAndClient(initialDate,
                    finalDate, currentClient, sql);

            Iterator it = salesPerDateAndClient.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Log.d(TAG, "***** Date = "+pair.getKey());
                it.remove(); // avoids a ConcurrentModificationException
                List<SaleItem> l = (List) pair.getValue();
                adapter.addSectionHeaderItem(l.get(0));
                for (SaleItem s : l){
                    adapter.addItem(s);
                    Log.d(TAG, s.getQuantity()+" : "+s.getIdProduct()+" : "+s.getUnitPrice());
                }
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Cliente ou Data inválidos.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

//    private void loadSalePerDateAndClient(long date, Client client) {
//        ArrayList<SaleItem> salesPerDateAndClient = db.listSaleAndStockPerDateAndClient(
//                date, client, sql);
//        if (salesPerDateAndClient.size() == 0) {
//            Toast.makeText(getApplicationContext(),
//                    "Não há itens para a data: " + date, Toast.LENGTH_SHORT)
//                    .show();
//        }
//        Log.d(TAG, salesPerDateAndClient.size()
//                + " resultados para o cliente:  " + client);
//        setupListViewAdapter(salesPerDateAndClient);
//    }


    private DATE_TYPE currentDateEditing;
    protected void createAndShowDatePickerDialog(DATE_TYPE type) {
        currentDateEditing = type;
        final DatePickerDialog datePickDialog = new DatePickerDialog(this,
                onSetDatePpickerListener, calendarAux.get(Calendar.YEAR),
                calendarAux.get(Calendar.MONTH), calendarAux.get(Calendar.DAY_OF_MONTH));
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

    Calendar calendarAux = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener onSetDatePpickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            calendarAux.set(Calendar.YEAR, selectedYear);
            calendarAux.set(Calendar.MONTH, selectedMonth);
            calendarAux.set(Calendar.DAY_OF_MONTH, selectedDay);

           //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);

            if(currentDateEditing.equals(DATE_TYPE.FINAL)){
                edtFinalDate.setText(sdf.format(calendarAux.getTime()));
            }else{
                edtInitialDate.setText(sdf.format(calendarAux.getTime()));
            }
        }

    };

//    private void saveSales() {
//        if (adapter != null) {
//            for (int i = 0; i < adapter.getCount(); i++) {
//                SaleItem saleItem = adapter.getItem(i);
//                saleItem.setDate(unixDate);
//                saleItem.setIdClient(currentClient.getId());
//                long id = db.insertSaleStock(saleItem, sql);
//                if (id != -1) {
//                    System.out.println("salvou com id: " + id);
//                    System.out.println("e data" + saleItem.getDate());
//                    System.out.println("e id client" + saleItem.getIdClient());
//                    adapter.getItem(i).setId(id);
//                }
//            }
//        }
//
//    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        System.out.println("Cliente autocomplete: " + s.toString());
        if (getKey(s.toString()) != -1) {
            long id = getKey(s.toString());
            currentClient = new Client();
            currentClient.setId(id);
            currentClient.setName(s.toString());
            System.out.println("Setou id " + id + "  e  name " + s);
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
        Cursor clients = db.listCursorClients();
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
