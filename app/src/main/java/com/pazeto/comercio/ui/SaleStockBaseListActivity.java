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
import com.pazeto.comercio.vo.BaseSaleStocked;
import com.pazeto.comercio.vo.Client;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public abstract class SaleStockBaseListActivity extends DefaultActivity {
    private static final String TAG = SaleStockBaseListActivity.class.getName();

    private TextView tvClient, tvCurrentDate;
    SaleStockedListAdapter adapter;
    ListView salesStockedProdListView;

    Client currentClient;
    Date currentDate;

    abstract BaseSaleStocked.TYPE getItemType();
    abstract CharSequence getMenuTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_stocked_prod_list_view);

        tvCurrentDate = findViewById(R.id.tv_date);
        setCurrenDate(Calendar.getInstance());

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

        adapter = new SaleStockedListAdapter(this);
        salesStockedProdListView = findViewById(R.id.sale_ListView);
        salesStockedProdListView.setAdapter(adapter);
        salesStockedProdListView.setEmptyView(findViewById(R.id.tv_empty));

        FloatingActionButton btnAddNew = findViewById(R.id.btn_add_new);
        btnAddNew.setImageResource(getNewBtnDrawable());
    }

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
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_client), Toast.LENGTH_SHORT).show();
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
            Calendar myCalendar = Calendar.getInstance();
            myCalendar.set(Calendar.YEAR, selectedYear);
            myCalendar.set(Calendar.MONTH, selectedMonth);
            myCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
            myCalendar.set(Calendar.HOUR_OF_DAY, 0);
            myCalendar.set(Calendar.MINUTE, 0);
            myCalendar.set(Calendar.SECOND, 0);

            setCurrenDate(myCalendar);

            if (isConfigured()) {
                reloadAllItems();
            }
        }

    };

    private void setCurrenDate(Calendar currentDate) {
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);

        this.currentDate = currentDate.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault());
        tvCurrentDate.setText(sdf.format(this.currentDate));
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
                Client client = (Client) data.getSerializableExtra(EditClientActivity.ID_EXTRA);
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

}
