package com.pazeto.market.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pazeto.market.R;
import com.pazeto.market.vo.BaseSaleStockedProduct;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.SaleItem;
import com.pazeto.market.vo.StockedItem;
import com.pazeto.market.widgets.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pazeto on 11/11/16.
 */
public class SummaryActivity extends DefaultActivity {

    private HashMap<Long, List<BaseSaleStockedProduct>> mData;
    public static final String EXTRA_MAP_ITEMS = "com.pazeto.market.EXTRA_MAP_ITEMS";
    long clientId;

    private TextView tvDate, tvClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            mData = (HashMap<Long, List<BaseSaleStockedProduct>>) data.getSerializable(EXTRA_MAP_ITEMS);
            clientId = data.getLong(Client.ID);
            extractInfoFromData(mData);
        } else {
            //TODO somenthig wrong
        }

        tvClient = (TextView) findViewById(R.id.tv_client_summary_name);
        tvClient.setText(db.getClient(clientId).getFullname());
        tvDate = (TextView) findViewById(R.id.tv_date);

        List<Long> dates = new ArrayList<>(mData.keySet());
        Collections.sort(dates);
        Long initialDate = dates.get(0);
        Long finalDate;
        if (dates.size() > 1) {
            finalDate = dates.get(dates.size() - 1);
            tvDate.setText("De " + Utils.unixtimeToDate(initialDate, dateFormatStr)
                    + " à " + Utils.unixtimeToDate(finalDate, dateFormatStr));
        } else {
            tvDate.setText(Utils.unixtimeToDate(initialDate, dateFormatStr));
        }

    }

    private void extractInfoFromData(HashMap<Long, List<BaseSaleStockedProduct>> mData) {

        List<SaleItem> sales = new ArrayList<>();
        List<StockedItem> stocks = new ArrayList<>();
        long saleAmount = 0;
        long stockAmount = 0;

        for (Long date : mData.keySet()) {
            List<BaseSaleStockedProduct> itemsToDate = mData.get(date);
            for (BaseSaleStockedProduct item : itemsToDate) {
                if (item.getType().equals(BaseSaleStockedProduct.TYPE_PRODUCT.SALE)) {
                    sales.add((SaleItem) item);
                    saleAmount += item.getTotalPrice();
                } else if (item.getType().equals(BaseSaleStockedProduct.TYPE_PRODUCT.STOCKED)) {
                    stocks.add((StockedItem) item);
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
