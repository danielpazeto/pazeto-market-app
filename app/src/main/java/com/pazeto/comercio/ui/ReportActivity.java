package com.pazeto.comercio.ui;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pazeto.comercio.R;
import com.pazeto.comercio.adapter.SaleStockReportAdapter;
import com.pazeto.comercio.vo.BaseSaleStocked;
import com.pazeto.comercio.vo.Client;
import com.pazeto.comercio.widgets.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReportActivity extends DefaultActivity {
    private TextView edtInitialDate;
    private TextView edtFinalDate;
    private static final String TAG = ReportActivity.class.getName();
    private SaleStockReportAdapter adapter;
    HashMap<Long, String> hmClients;
    private Client currentClient;
    private TextView tvClient;
    private Spinner reportTypeSpinner;
    ListView salesListView;

    private DATE_TYPE currentDateEditing;

    enum DATE_TYPE {
        INITIAL, FINAL
    }

    private Snackbar snackbar;
    private CoordinatorLayout cdlReport;

    private static String[] TYPES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_sale_view);

        edtInitialDate = (TextView) findViewById(R.id.edt_initial_date);
        edtFinalDate = (TextView) findViewById(R.id.edt_final_date);
        tvClient = (TextView) findViewById(R.id.OutputClient);
        salesListView = (ListView) findViewById(R.id.sale_ListView);
        reportTypeSpinner = (Spinner)
                findViewById(R.id.tv_item_type);
        setupTypeSpinner();

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

        cdlReport = (CoordinatorLayout) findViewById(R.id.cdl_report);

    }

    private Snackbar getSummarySnackBar() {
        if(snackbar!=null && snackbar.isShown()){
            snackbar.dismiss();
        }
        snackbar = Snackbar
                .make(cdlReport, "Total", Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.summary, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSummaryBtnClick(view);
                    }
                });
        return snackbar;
    }

    private void setupTypeSpinner() {
        TYPES = new String[]{
                getString(R.string.all), getString(R.string.sales), getString(R.string.stock)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, TYPES);
        reportTypeSpinner.setAdapter(adapter);
    }

    private void setupListViewAdapter() {
        adapter = new SaleStockReportAdapter(this, R.layout.report_sale_list_item);
        final TextView tv = (TextView) findViewById(R.id.tv_empty);

//        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale_1dot5);
//        a.setRepeatMode(Animation.ABSOLUTE);
//        tv.setAnimation(a);
//        tv.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int i) {
//                if(View.VISIBLE==i){
//                    tv.animate();
//                }
//            }
//        });

        salesListView.setEmptyView(tv);
        salesListView.setAdapter(adapter);
    }


    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);

    public void onGenerateReport(View v) {
        if (isValidClient() && validateDates()) {

            BaseSaleStocked.TYPE type = BaseSaleStocked.TYPE.ALL;
            if (reportTypeSpinner.getSelectedItem().toString().equals(getString(R.string.sales))) {
                type = BaseSaleStocked.TYPE.SALE;
            } else if (reportTypeSpinner.getSelectedItem().toString().equals(getString(R.string.stock))) {
                type = BaseSaleStocked.TYPE.STOCK;
            }

//            HashMap<Long, List<BaseSaleStocked>> salesPerDateAndClient = db.listSaleAndStockPerDateAndClient(initialDate,
//                    finalDate, type, currentClient, sql);
//            if (!salesPerDateAndClient.isEmpty()) {
//                setupListViewAdapter();
//                adapter.addAll(salesPerDateAndClient);
//                getSummarySnackBar().setText(salesPerDateAndClient.size() + " dia(s)");
//                getSummarySnackBar().show();
//            } else {
//                getSummarySnackBar().dismiss();
//                adapter.clear();
//                salesListView.getEmptyView().animate();
//            }
        }
    }

    private boolean isValidClient() {
        if (currentClient != null) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.invalid_client, Toast.LENGTH_SHORT)
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
                        getString(R.string.invalid_date_client), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
            ReportActivity.initialDate = initialDate;
            ReportActivity.finalDate = finalDate;
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(),
                    R.string.invalid_dates, Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    protected void createAndShowDatePickerDialog(DATE_TYPE type) {
        final DatePickerDialog datePickDialog = new DatePickerDialog(this,
                onSetDatePpickerListener, calendarAux.get(Calendar.YEAR),
                calendarAux.get(Calendar.MONTH), calendarAux.get(Calendar.DAY_OF_MONTH));
        currentDateEditing = type;
        if (currentDateEditing.equals(DATE_TYPE.FINAL)) {
            datePickDialog.setTitle(R.string.choose_final_date_2dot);
        } else if (currentDateEditing.equals(DATE_TYPE.INITIAL)) {
            datePickDialog.setTitle(R.string.choose_initial_date_2dot);
        }
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
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);

            if (currentDateEditing.equals(DATE_TYPE.FINAL)) {
                edtFinalDate.setText(sdf.format(calendarAux.getTime()));
            } else {
                edtInitialDate.setText(sdf.format(calendarAux.getTime()));
                if(edtFinalDate.getText().toString().isEmpty() ){
                    edtFinalDate.setText(sdf.format(calendarAux.getTime()));
                }
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
//                Long clientId = data.getExtras().getLong(Client.ID);
//                currentClient = db.getClient(clientId);
//                tvClient.setText(currentClient.getName() + " " + currentClient.getLastname());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void selectClientClick(View v) {
        Intent intent = new Intent(this, ListClientsActivity.class);
        intent.putExtra(ListClientsActivity.IS_TO_SELECT_CLIENT, true);
        startActivityForResult(intent, 0);
    }


    public void onSummaryBtnClick(View v) {
        HashMap<Long, List<BaseSaleStocked>> itemsPerDate = adapter.getAll();
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra(SummaryActivity.EXTRA_MAP_ITEMS, itemsPerDate);
//        intent.putExtra(Client.ID, currentClient.getId());
        startActivity(intent);
    }

}
