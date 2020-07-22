package com.pazeto.comercio.vo;

import java.util.Date;

/**
 * @author pazeto
 */
public class Stock extends BaseSaleStocked {

    public Stock() {

    }

    public Stock(Product product, String clientId, Date date, double quantity, double unitPrice) {
        super(product, clientId, date, quantity, unitPrice);
    }

    @Override
    public BaseSaleStocked.TYPE getType() {
        return BaseSaleStocked.TYPE.STOCK;
    }
}