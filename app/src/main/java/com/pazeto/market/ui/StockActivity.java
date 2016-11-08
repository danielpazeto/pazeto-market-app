package com.pazeto.market.ui;

import android.os.Bundle;

import com.pazeto.market.R;
import com.pazeto.market.vo.BaseSaleStockedProduct;
import com.pazeto.market.vo.SaleStockedItem;

public class StockActivity extends SaleStockBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getNewBtnDrawable() {
        return R.mipmap.ic_new_product;
    }

    @Override
    BaseSaleStockedProduct getNewItem() {
        return new SaleStockedItem(0, 0, unixDate);
    }

    @Override
    BaseSaleStockedProduct.TYPE_PRODUCT getItemType() {
        return BaseSaleStockedProduct.TYPE_PRODUCT.STOCKED;
    }

    @Override
    CharSequence getMenuTitle() {
        return getString(R.string.stock);
    }

}
