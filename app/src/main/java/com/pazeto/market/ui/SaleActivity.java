package com.pazeto.market.ui;

import android.os.Bundle;

import com.pazeto.market.R;
import com.pazeto.market.vo.BaseSaleStockedProduct;
import com.pazeto.market.vo.SaleItem;

public class SaleActivity extends SaleStockBaseActivity {
    private static final String TAG = SaleActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getNewBtnDrawable() {
        return R.mipmap.ic_new_sale;
    }

    @Override
    BaseSaleStockedProduct getNewItem() {
        return new SaleItem(0, 0, unixDate);
    }

    @Override
    BaseSaleStockedProduct.TYPE_PRODUCT getItemType() {
        return BaseSaleStockedProduct.TYPE_PRODUCT.SALE;
    }

    @Override
    CharSequence getMenuTitle() {
        return getString(R.string.sale);
    }

}
