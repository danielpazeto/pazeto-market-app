package com.pazeto.market.vo;

import android.database.Cursor;

/**
 * @author pazeto
 */
public class StockedItem extends BaseSaleStockedProduct {


    public StockedItem(long idProd, double quantity, long date) {
        super(idProd, quantity, date);
        this.type= TYPE_PRODUCT.STOCKED;
    }

    public StockedItem(Cursor c) {
        super(c);
        this.type= TYPE_PRODUCT.STOCKED;
    }

    public StockedItem(long id, long idProd, double quantity, long idClient, double unitPrice, long date) {
        super(id, idProd, quantity, idClient, unitPrice, date);
        this.type= TYPE_PRODUCT.STOCKED;
    }

    @Override
    public TYPE_PRODUCT getType() {
        return TYPE_PRODUCT.STOCKED;
    }

}