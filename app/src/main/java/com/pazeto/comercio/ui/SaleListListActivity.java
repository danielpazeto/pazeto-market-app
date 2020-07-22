package com.pazeto.comercio.ui;

import android.os.Bundle;

import com.pazeto.comercio.R;
import com.pazeto.comercio.vo.BaseSaleStocked;
import com.pazeto.comercio.vo.Product;
import com.pazeto.comercio.vo.Sale;

public class SaleListListActivity extends SaleStockBaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getNewBtnDrawable() {
        return R.mipmap.ic_new_sale;
    }

    @Override
    BaseSaleStocked getNewItem() {
        return new Sale(new Product(), currentClient.getId(), currentDate, 0, 0);
    }

    @Override
    BaseSaleStocked.TYPE getItemType() {
        return BaseSaleStocked.TYPE.SALE;
    }

    @Override
    CharSequence getMenuTitle() {
        return getString(R.string.sale);
    }

}
