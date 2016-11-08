package com.pazeto.market.vo;

import android.database.Cursor;

/**
 * @author pazeto
 */
public class SaleItem extends BaseSaleStockedProduct {

    public SaleItem(long idProd, double quantity, long date) {
        super(idProd, quantity, date);
    }

    public SaleItem(Cursor c) {
        super(c);
    }

    public SaleItem(long id, long idProd, double quantity, long idClient, double unitPrice, long date) {
        super(id, idProd, quantity, idClient, unitPrice, date);
    }

    @Override
    public TYPE_PRODUCT getType() {
        return TYPE_PRODUCT.SALE;
    }

}