package com.pazeto.market.ui;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pazeto.market.R;
import com.pazeto.market.adapter.SaleStockReportAdapter;
import com.pazeto.market.vo.BaseSaleStockedProduct;
import com.pazeto.market.vo.Client;
import com.pazeto.market.widgets.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReportActivity extends DefaultActivity {
    private EditText edtInitialDate;
    private EditText edtFinalDate;
    private static final String TAG = ReportActivity.class.getName();
    private SaleStockReportAdapter adapter;
    HashMap<Long, String> hmClients;
    private Client currentClient;
    private TextView tvClient;
    private Spinner reportTypeSpinner;

    enum DATE_TYPE {
        INITIAL, FINAL
    }

    private static String[] TYPES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_sale_view);

        edtInitialDate = (EditText) findViewById(R.id.edt_initial_date);
        edtFinalDate = (EditText) findViewById(R.id.edt_final_date);
        tvClient = (TextView) findViewById(R.id.OutputClient);
        reportTypeSpinner = (Spinner)
                findViewById(R.id.tv_item_type);
        setupAutoCompleteType();

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

    private void setupAutoCompleteType() {
        TYPES = new String[]{
                getString(R.string.all), getString(R.string.sales), getString(R.string.stock)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, TYPES);
        reportTypeSpinner.setAdapter(adapter);
    }

    private void setupListViewAdapter() {
        adapter = new SaleStockReportAdapter(this, R.layout.report_sale_list_item);
        ListView salesListView = (ListView) findViewById(R.id.sale_ListView);
        salesListView.setAdapter(adapter);
    }


    SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);

    public void onGenerateReport(View v) {
        if (validateDates() && isValidClient()) {

            BaseSaleStockedProduct.TYPE_PRODUCT type = BaseSaleStockedProduct.TYPE_PRODUCT.ALL;
            if (reportTypeSpinner.getSelectedItem().toString().equals(getString(R.string.sales))) {
                type = BaseSaleStockedProduct.TYPE_PRODUCT.SALE;
            } else if (reportTypeSpinner.getSelectedItem().toString().equals(getString(R.string.sales))) {
                type = BaseSaleStockedProduct.TYPE_PRODUCT.STOCKED;
            }

            HashMap<Long, List<BaseSaleStockedProduct>> salesPerDateAndClient = db.listSaleAndStockPerDateAndClient(initialDate,
                    finalDate, type, currentClient, sql);
            adapter.clear();
            if(!salesPerDateAndClient.isEmpty()){
                adapter.addAll(salesPerDateAndClient);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private boolean isValidClient() {
        if( currentClient != null){
            return true;
        }else{
            Toast.makeText(getApplicationContext(),
                    "Cliente inválido", Toast.LENGTH_SHORT)
                    .show();
        }
        return false;
    }

    private static long initialDate;
    private static long finalDate;

    private boolean validateDates() {
        Calendar calendarFrom = Calendar.getInstance();
        Calendar calendarUntil = Calendar.getInstance();
        try {
            calendarFrom.setTime(dateFormat.parse(edtInitialDate.getText().toString()));
            calendarUntil.setTime(dateFormat.parse(edtFinalDate.getText().toString()));
            long initialDate = Utils.calendarIntanceToStartSecondsDay(calendarFrom);
            long finalDate = Utils.calendarIntanceToStartSecondsDay(calendarUntil);
            if (initialDate <= 0 && finalDate <= 0 && finalDate < initialDate) {
                Toast.makeText(getApplicationContext(),
                        "Datas inválidas.", Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
            ReportActivity.initialDate = initialDate;
            ReportActivity.finalDate = finalDate;
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(),
                    "Datas inválidas.", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

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

            if (currentDateEditing.equals(DATE_TYPE.FINAL)) {
                edtFinalDate.setText(sdf.format(calendarAux.getTime()));
            } else {
                edtInitialDate.setText(sdf.format(calendarAux.getTime()));
            }
        }

    };

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
        if (data != null) {
            if (data.hasExtra(ListClientsActivity.IS_TO_SELECT_CLIENT)) {
                Long clientId = data.getExtras().getLong(Client.ID);
                currentClient = db.getClient(clientId);
                tvClient.setText(currentClient.getName() + " " + currentClient.getLastname());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void selectClientClick(View v) {
        Intent intent = new Intent(this, ListClientsActivity.class);
        intent.putExtra(ListClientsActivity.IS_TO_SELECT_CLIENT, true);
        startActivityForResult(intent, 0);
    }
}
