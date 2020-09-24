package com.pazeto.comercio.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pazeto.comercio.R;
import com.pazeto.comercio.vo.BaseSaleStocked;
import com.pazeto.comercio.vo.Sale;
import com.pazeto.comercio.vo.Stock;
import com.pazeto.comercio.widgets.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pazeto on 11/11/16.
 */
public class SummaryActivity extends DefaultActivity {

    private HashMap<Long, List<BaseSaleStocked>> mData;
    public static final String EXTRA_MAP_ITEMS = "com.pazeto.market.EXTRA_MAP_ITEMS";
    long clientId;

    private TextView tvDate, tvClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            mData = (HashMap<Long, List<BaseSaleStocked>>) data.getSerializable(EXTRA_MAP_ITEMS);
//            clientId = data.getLong(Client.ID);
            extractInfoFromData(mData);
        } else {
            //TODO somenthig wrong
        }

        tvClient = (TextView) findViewById(R.id.tv_client_summary_name);
//        tvClient.setText(db.getClient(clientId).getFullname());
        tvDate = (TextView) findViewById(R.id.tv_date);

        List<Long> dates = new ArrayList<>(mData.keySet());
        Collections.sort(dates);
        Long initialDate = dates.get(0);
        Long finalDate;
//        if (dates.size() > 1) {
//            finalDate = dates.get(dates.size() - 1);
//            tvDate.setText("De " + Utils.unixtimeToDate(initialDate, DATE_FORMAT_STRING)
//                    + " à " + Utils.unixtimeToDate(finalDate, DATE_FORMAT_STRING));
//        } else {
//            tvDate.setText(Utils.unixtimeToDate(initialDate, DATE_FORMAT_STRING));
//        }

    }

    private void extractInfoFromData(HashMap<Long, List<BaseSaleStocked>> mData) {

        List<Sale> sales = new ArrayList<>();
        List<Stock> stocks = new ArrayList<>();
        long saleAmount = 0;
        long stockAmount = 0;

        for (Long date : mData.keySet()) {
            List<BaseSaleStocked> itemsToDate = mData.get(date);
            for (BaseSaleStocked item : itemsToDate) {
                if (item.getType().equals(BaseSaleStocked.TYPE.SALE)) {
                    sales.add((Sale) item);
                    saleAmount += item.getTotalPrice();
                } else if (item.getType().equals(BaseSaleStocked.TYPE.STOCK)) {
                    stocks.add((Stock) item);
                    stockAmount += item.getTotalPrice();
                }
            }
        }
        if (sales.size() > 0) {
            ((TextView) findViewById(R.id.tv_total_sale)).setText(String.valueOf(saleAmount));
            findViewById(R.id.ll_sale_summary).setVisibility(View.VISIBLE);
        }
        if (stocks.size() > 0) {
            ((TextView) findViewById(R.id.tv_total_stock)).setText(String.valueOf(stockAmount));
            findViewById(R.id.ll_stock_summary).setVisibility(View.VISIBLE);
        }


    }
}
