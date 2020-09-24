package com.pazeto.comercio.ui;

import android.os.Bundle;

import com.pazeto.comercio.R;
import com.pazeto.comercio.vo.BaseSaleStocked;
import com.pazeto.comercio.vo.Product;
import com.pazeto.comercio.vo.Stock;

public class StockListListActivity extends SaleStockBaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getNewBtnDrawable() {
        return R.mipmap.ic_new_product;
    }

    @Override
    BaseSaleStocked getNewItem() {
        return new Stock(new Product(), currentClient.getId(), currentDate, 0, 0);

    }

    @Override
    BaseSaleStocked.TYPE getItemType() {
        return BaseSaleStocked.TYPE.STOCK;
    }

    @Override
    protected int getTotalLayoutBackground() {
        return R.drawable.background_rounded_border_red;
    }

    @Override
    protected int getHeaderLayoutBackground() {
        return R.color.red_section_header;
    }

}
