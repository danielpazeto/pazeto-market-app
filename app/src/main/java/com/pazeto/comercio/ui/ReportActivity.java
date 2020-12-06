package com.pazeto.comercio.ui;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pazeto.comercio.R;
import com.pazeto.comercio.adapter.SaleStockReportAdapter;
import com.pazeto.comercio.db.FirebaseHandler;
import com.pazeto.comercio.utils.Constants;
import com.pazeto.comercio.vo.BaseSaleStocked;
import com.pazeto.comercio.vo.Client;
import com.pazeto.comercio.widgets.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ReportActivity extends DefaultActivity {
    private TextView tvInitialDate;
    private TextView tvFinalDate;
    private static final String TAG = ReportActivity.class.getName();
    private SaleStockReportAdapter adapter;
    private Client currentClient;
    private TextView tvClient;
    private Spinner reportTypeSpinner;
    ListView salesListView;

    private DATE_TYPE currentDateEditing;

    enum DATE_TYPE {
        INITIAL, FINAL
    }

    private CoordinatorLayout cdlReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_sale_view);

        tvInitialDate = findViewById(R.id.edt_initial_date);
        tvFinalDate = findViewById(R.id.edt_final_date);
        tvClient = findViewById(R.id.tvSelectClient);

        reportTypeSpinner = findViewById(R.id.tv_item_type);
        setupTypeSpinner();

        tvInitialDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createAndShowDatePickerDialog(DATE_TYPE.INITIAL);
            }

        });
        tvFinalDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createAndShowDatePickerDialog(DATE_TYPE.FINAL);
            }

        });

        salesListView = findViewById(R.id.sale_list_view);
        adapter = new SaleStockReportAdapter(this, R.layout.report_sale_list_item);
        salesListView.setEmptyView(findViewById(R.id.tv_empty));
        salesListView.setAdapter(adapter);

        cdlReport = (CoordinatorLayout) findViewById(R.id.cdl_report);

    }

    private void setupTypeSpinner() {
        String[] types = new String[]{
                getString(R.string.all), getString(R.string.sales), getString(R.string.stock)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, types);
        reportTypeSpinner.setAdapter(adapter);
    }

    private void setItemsToApater(List<BaseSaleStocked> saleStockReportList) {
        adapter.addAll(saleStockReportList);
    }


    public void onGenerateReport(View v) {
        if (isValidClient() && validateDates()) {

            BaseSaleStocked.TYPE type = BaseSaleStocked.TYPE.ALL;
            if (reportTypeSpinner.getSelectedItem().toString().equals(getString(R.string.sales))) {
                type = BaseSaleStocked.TYPE.SALE;
            } else if (reportTypeSpinner.getSelectedItem().toString().equals(getString(R.string.stock))) {
                type = BaseSaleStocked.TYPE.STOCK;
            }
            Query query = new FirebaseHandler(this).getSaleStocksCollectionReference()
                    .orderBy("date")
                    .startAt(initialDate)
                    .endAt(finalDate)
                    .whereEqualTo("idClient", currentClient.getId());

            if (type != BaseSaleStocked.TYPE.ALL){
                query = query.whereEqualTo("type", type.name());
            }

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<BaseSaleStocked> saleStockReportList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    BaseSaleStocked saleStock = document.toObject(BaseSaleStocked.class);
                                    saleStock.setId(document.getId());
                                    saleStockReportList.add(saleStock);
                                }
                                setItemsToApater(saleStockReportList);
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
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

    private Date initialDate;
    private Date finalDate;

    private boolean validateDates() {
        if (initialDate == null || finalDate == null || finalDate.before(initialDate)) {
            Toast.makeText(getApplicationContext(),
                    R.string.invalid_dates, Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    protected void createAndShowDatePickerDialog(DATE_TYPE dateType) {
        final DatePickerDialog datePickDialog = new DatePickerDialog(this,
                onSetDatePpickerListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        currentDateEditing = dateType;

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

    private DatePickerDialog.OnDateSetListener onSetDatePpickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            Calendar calendarInstance = Utils.getCalendarDate(selectedYear, selectedMonth, selectedDay);

            if (currentDateEditing.equals(DATE_TYPE.FINAL)) {
                finalDate = calendarInstance.getTime();
                if(initialDate == null || finalDate.before(initialDate)){
                    initialDate = finalDate;
                }
            } else {
                initialDate = calendarInstance.getTime();
                if(finalDate == null || initialDate.after(finalDate)){
                    finalDate = initialDate;
                }
            }

            setDateViewTexts();
        }

    };

    private void setDateViewTexts() {
        tvFinalDate.setText(Utils.formatDateToString(finalDate));
        tvInitialDate.setText(Utils.formatDateToString(initialDate));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (data.hasExtra(ListClientsActivity.IS_TO_SELECT_CLIENT)) {
                currentClient = (Client) data.getSerializableExtra(Constants.INTENT_EXTRA_CLIENT);
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
