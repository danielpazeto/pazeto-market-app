package com.pazeto.market.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.github.clans.fab.FloatingActionButton;
import com.pazeto.market.R;
import com.pazeto.market.adapter.SaleStockedListAdapter;
import com.pazeto.market.vo.BaseStockedProduct;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.SaleItem;
import com.pazeto.market.widgets.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.pazeto.market.widgets.Utils.ClientHashMap;
import static com.pazeto.market.widgets.Utils.calendarIntanceToStartSecondsDay;

public abstract class SaleStockBaseActivity extends DefaultActivity implements TextWatcher {
    EditText edtCurrentDate;
    private Button btSave;
    private static final String TAG = SaleStockBaseActivity.class.getName();
    SaleStockedListAdapter adapter;

    Client currentClient;
    long unixDate;
    Calendar myCalendar = Calendar.getInstance();
    Utils.ClientHashMap hmClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_list_view);

        hmClients = new Utils.ClientHashMap(db);

        btSave = (Button) findViewById(R.id.btSave);
        edtCurrentDate = (EditText) findViewById(R.id.tv_date);

        setupAutoCompleteClient();

        edtCurrentDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                save();
                createAndShowDatePickerDialog();
            }

        });
        btSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                save();

            }
        });

        FloatingActionButton btnAddNew = (FloatingActionButton) findViewById(R.id.btn_add_new);
        btnAddNew.setImageResource(getNewBtnDrawable());
    }

    protected abstract int getNewBtnDrawable();

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

    private void setupListViewAdapter(ArrayList<BaseStockedProduct> items) {
        adapter = new SaleStockedListAdapter(this, items,
                unixDate);
        ListView salesListView = (ListView) findViewById(R.id.sale_ListView);
        salesListView.setAdapter(adapter);
    }


    abstract BaseStockedProduct getNewItem();


    public void onAddNewBtnClick(View v) {
        if (isConfigured()) {
            adapter.insert(getNewItem(), 0);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Cliente ou Data inválidos.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    abstract BaseStockedProduct.TYPE_PRODUCT getItemType();

    void loadItemsPerDateAndClient(long date, Client client) {
        ArrayList<BaseStockedProduct> itemPerDateAndClient = db.listSaleAndStockPerDateAndClient(
                date, client, getItemType(), sql);
        if (itemPerDateAndClient.size() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Não há itens para a data: " + date, Toast.LENGTH_SHORT)
                    .show();
        }
        Log.d(TAG, itemPerDateAndClient.size()
                + " resultados para o cliente:  " + client);
        setupListViewAdapter(itemPerDateAndClient);
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

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr, Locale.getDefault());

            edtCurrentDate.setText(sdf.format(myCalendar.getTime()));

            unixDate = calendarIntanceToStartSecondsDay(myCalendar);
            if (isConfigured()) {
                loadItemsPerDateAndClient(unixDate, currentClient);
            }
        }

    };

    void save() {
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                BaseStockedProduct saleItem = adapter.getItem(i);
                saleItem.setDate(unixDate);
                saleItem.setIdClient(currentClient.getId());
                long id = db.insertSaleStock(saleItem, sql);
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
        save();
        super.onBackPressed();
    }

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
        long id = hmClients.getIdByName(s.toString());
        if (id != -1) {
            currentClient = db.getClient(id);
            System.out.println("Setou id " + id + "  e  name " + s);
            if (isConfigured()) {
                loadItemsPerDateAndClient(unixDate, currentClient);
            }
        } else {
            currentClient = null;
        }
    }

    /**
     * Inicializa o autocomplete do cliente
     */
    public void setupAutoCompleteClient() {
        hmClients = new ClientHashMap(db);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.dropdown_list_custom,
                hmClients.getClientNames());
        AutoCompleteTextView autoCompProduct = (AutoCompleteTextView) findViewById(R.id.OutputClient);
        autoCompProduct.addTextChangedListener(this);
        autoCompProduct.setThreshold(0);
        autoCompProduct.setAdapter(mAdapter);
    }

    /**
     * Verify if client and date are selected
     *
     * @return true if configured client and date
     */
    public boolean isConfigured() {
        return (currentClient != null && unixDate > 0 );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                save();
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

    abstract CharSequence getMenuTitle();
}
