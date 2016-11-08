package com.pazeto.market.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.pazeto.market.R;
import com.pazeto.market.adapter.SaleStockedListAdapter;
import com.pazeto.market.vo.BaseSaleStockedProduct;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.pazeto.market.widgets.Utils.calendarIntanceToStartSecondsDay;

public abstract class SaleStockBaseActivity extends DefaultActivity {
    private Button btSave;
    private TextView tvClient,edtCurrentDate;
    private static final String TAG = SaleStockBaseActivity.class.getName();
    SaleStockedListAdapter adapter;

    Client currentClient;
    long unixDate;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_stocked_prod_list_view);

        btSave = (Button) findViewById(R.id.btSave);
        edtCurrentDate = (TextView) findViewById(R.id.tv_date);
        tvClient = (TextView) findViewById(R.id.OutputClient);
        tvClient.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaleStockBaseActivity.this, ListClientsActivity.class);
                intent.putExtra(ListClientsActivity.IS_TO_SELECT_CLIENT, true);
                startActivityForResult(intent, 0);
            }
        });

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

        adapter = new SaleStockedListAdapter(this, new ArrayList<BaseSaleStockedProduct>());
        salesStockedProdListView = (ListView) findViewById(R.id.sale_ListView);
        salesStockedProdListView.setAdapter(adapter);
        salesStockedProdListView.setEmptyView(findViewById(R.id.tv_empty));

        FloatingActionButton btnAddNew = (FloatingActionButton) findViewById(R.id.btn_add_new);
        btnAddNew.setImageResource(getNewBtnDrawable());
    }

    /**
     * Method to return the int resource for the new btn image
     * @return
     */
    protected abstract int getNewBtnDrawable();

    /**
     * Remove a venda
     *
     * @param v
     */
    public void removeSaleOnClickHandler(View v) {
        final BaseSaleStockedProduct itemToRemove = (BaseSaleStockedProduct) v.getTag();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Deseja remover o item nº " + v.getId() + ":\n" + itemToRemove.getQuantity() + " - "
                        + adapter.getProductFullName(itemToRemove.getIdProduct()) + " - "
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

    ListView salesStockedProdListView;

    abstract BaseSaleStockedProduct getNewItem();

    public void onAddNewBtnClick(View v) {
        if (isConfigured()) {
            adapter.insert(getNewItem(), adapter.getCount());
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_date_client)
                    , Toast.LENGTH_SHORT)
                    .show();
        }
    }

    abstract BaseSaleStockedProduct.TYPE_PRODUCT getItemType();

    void loadItemsPerDateAndClient(long date, Client client) {
        HashMap<Long, List<BaseSaleStockedProduct>> itemPerDateAndClient = db.listSaleAndStockPerDateAndClient(
                date, client, getItemType(), sql);
        if (itemPerDateAndClient.size() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Não há itens para a data: " + date, Toast.LENGTH_SHORT)
                    .show();
        }
        Log.d(TAG, itemPerDateAndClient.size()
                + " resultados para o cliente:  " + client);
        setupListViewAdapter(itemPerDateAndClient.get(date));
    }

    private void setupListViewAdapter(List<BaseSaleStockedProduct> items) {
        adapter.clear();
        if(items !=null) {
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        }
    }

    protected void createAndShowDatePickerDialog() {
        final DatePickerDialog datePickDialog = new DatePickerDialog(this,
                onSetDatePpickerListener, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickDialog.setTitle(R.string.choose_date);
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
                BaseSaleStockedProduct itemToSave = adapter.getItem(i);
                itemToSave.setDate(unixDate);
                itemToSave.setIdClient(currentClient.getId());
                long id = db.insertSaleStock(itemToSave, sql);
                if (id != -1) {
                    System.out.println("salvou com id: " + id);
                    System.out.println("e data" + itemToSave.getDate());
                    System.out.println("e id client" + itemToSave.getIdClient());
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

    /**
     * Inicializa o autocomplete do cliente
     */
    void setClient(long clientId) {
        currentClient = db.getClient(clientId);
        tvClient.setText(currentClient.getName() + " " + currentClient.getLastname());
        if (isConfigured()) {
            loadItemsPerDateAndClient(unixDate, currentClient);
        }
    }

    /**
     * Verify if client and date are selected
     *
     * @return true if configured client and date
     */
    public boolean isConfigured() {
        return (currentClient != null && unixDate > 0);
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
        if (data != null) {
            if (data.hasExtra(ListProductsActivity.IS_TO_SELECT_PRODUCT)) {
                int listPosition = data.getIntExtra(ListProductsActivity.IS_TO_SELECT_PRODUCT, -1);
                Long prodId = data.getExtras().getLong(Product.ID);
                adapter.insertProductOnPosition(listPosition, prodId);
            } else if (data.hasExtra(ListClientsActivity.IS_TO_SELECT_CLIENT)) {
                Long clientId = data.getExtras().getLong(Client.ID);
                setClient(clientId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    abstract CharSequence getMenuTitle();
}
