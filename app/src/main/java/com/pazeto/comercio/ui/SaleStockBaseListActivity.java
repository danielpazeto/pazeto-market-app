package com.pazeto.comercio.ui;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pazeto.comercio.R;
import com.pazeto.comercio.adapter.SaleStockedListAdapter;
import com.pazeto.comercio.utils.Constants;
import com.pazeto.comercio.vo.BaseSaleStocked;
import com.pazeto.comercio.vo.Client;
import com.pazeto.comercio.widgets.Utils;

import java.util.Calendar;
import java.util.Date;


public abstract class SaleStockBaseListActivity extends DefaultActivity implements SaleStockedListAdapter.AdapterUpdateNotify {
    private static final String TAG = SaleStockBaseListActivity.class.getName();

    private TextView tvClient, tvCurrentDate, tvTotalAmount;
    SaleStockedListAdapter adapter;
    ListView salesStockedProdListView;

    Client currentClient;
    Date currentDate;

    abstract BaseSaleStocked.TYPE getItemType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_stocked_prod_list_view);

        setLayoutColors();

        tvCurrentDate = findViewById(R.id.tv_date);
        setCurrenDate(Utils.getCalendarDate(Calendar.getInstance()));

        tvClient = findViewById(R.id.OutputClient);
        tvClient.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaleStockBaseListActivity.this, ListClientsActivity.class);
                intent.putExtra(ListClientsActivity.IS_TO_SELECT_CLIENT, true);
                startActivityForResult(intent, 0);
            }
        });

        tvCurrentDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createAndShowDatePickerDialog();
            }

        });

        tvTotalAmount = findViewById(R.id.sale_stock_total_amount_tv);
        Utils.setCurrencyValueView(tvTotalAmount, 0);


        adapter = new SaleStockedListAdapter(this);
        salesStockedProdListView = findViewById(R.id.sale_list_view);
        salesStockedProdListView.setAdapter(adapter);
        salesStockedProdListView.setEmptyView(findViewById(R.id.tv_empty));

        FloatingActionButton btnAddNew = findViewById(R.id.btn_add_new);
        btnAddNew.setImageResource(getNewBtnDrawable());
    }

    private void setLayoutColors() {
        findViewById(R.id.header).setBackground(getDrawable(getHeaderLayoutBackground()));
        findViewById(R.id.sale_stock_total_layout).setBackground(getDrawable(getTotalLayoutBackground()));
    }

    protected abstract int getTotalLayoutBackground();

    protected abstract int getHeaderLayoutBackground();

    /**
     * Method to return the int resource for the new btn image
     * @return
     */
    protected abstract int getNewBtnDrawable();

    abstract BaseSaleStocked getNewItem();

    public void addNewSaleStock(View v) {
        if (isConfigured()) {
            Intent intent = new Intent(SaleStockBaseListActivity.this, EditSaleStockActivity.class);
            intent.putExtra(EditSaleStockActivity.SALE_STOCK_EXTRA, getNewItem());
            startActivityForResult(intent, 0);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.choose_client), Toast.LENGTH_SHORT).show();
        }
    }

    protected void createAndShowDatePickerDialog() {
        Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog datePickDialog = new DatePickerDialog(this,
                onSetDatePickerListener, myCalendar.get(Calendar.YEAR),
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

    private DatePickerDialog.OnDateSetListener onSetDatePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            Calendar myCalendar = Utils.getCalendarDate(selectedYear, selectedMonth, selectedDay);

            setCurrenDate(myCalendar);

            if (isConfigured()) {
                reloadAllItems();
            }
        }

    };

    private void setCurrenDate(Calendar currentDate) {
        this.currentDate = currentDate.getTime();
        tvCurrentDate.setText(Utils.formatDateToString(this.currentDate));
    }

    private void reloadAllItems() {
        adapter.reload(currentClient, currentDate, getItemType());
    }

    /**
     * Verify if client and date are selected
     *
     * @return true if configured client and date
     */
    public boolean isConfigured() {
        return (currentClient != null && currentDate != null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (data.hasExtra(EditSaleStockActivity.SALE_STOCK_EXTRA)) {
                BaseSaleStocked saleStock = (BaseSaleStocked) data.getSerializableExtra(EditSaleStockActivity.SALE_STOCK_EXTRA);
                adapter.updateItem(saleStock);
            } else if (data.hasExtra(ListClientsActivity.IS_TO_SELECT_CLIENT)) {
                Client client = (Client) data.getSerializableExtra(Constants.INTENT_EXTRA_CLIENT);
                setClient(client);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void setClient(Client client) {
        currentClient = client;
        tvClient.setText(currentClient.getName() + " " + currentClient.getLastname());
        if (isConfigured()) {
            reloadAllItems();
        }
    }

    @Override
    public void adapterNotifyChange() {
        double total = adapter.getTotalAmout();
        Utils.setCurrencyValueView(tvTotalAmount, total);
    }
}
