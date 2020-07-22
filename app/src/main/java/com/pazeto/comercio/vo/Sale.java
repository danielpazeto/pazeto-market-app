package com.pazeto.comercio.vo;

import android.database.Cursor;

import java.util.Date;

/**
 * @author pazeto
 */
public class Sale extends BaseSaleStocked {

    public Sale() {

    }

    public Sale(Product product, String clientId, Date date, double quantity, double unitPrice) {
        super(product, clientId, date, quantity, unitPrice);
    }

    @Override
    public BaseSaleStocked.TYPE getType() {
        return BaseSaleStocked.TYPE.SALE;
    }

}