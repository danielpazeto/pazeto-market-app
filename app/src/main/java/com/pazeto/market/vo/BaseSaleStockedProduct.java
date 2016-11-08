package com.pazeto.market.vo;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * @author pazeto
 */
public abstract class BaseSaleStockedProduct extends BaseDB implements Serializable {

    public enum TYPE_PRODUCT {
        ALL, SALE, STOCKED
    }

    public static final String TABLE_NAME = "SaleStockedProduct";
    public static final String ID = "_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String CLIENT_ID = "client_id";
    public static final String QUANTITY = "quantity";
    public static final String DATE = "date";
    public static final String CREATED_DATE = "created_date";
    public static final String DESCRIPTION = "description";
    public static final String UNIT_PRICE = "unit_price";
    public static final String IS_PAID = "is_paid";
    public static final String TYPE = "type";
    public static final String _UPDATED = "_updated";
    public static final String _DELETED = "_deleted";
    //@formatter:off
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY," +
            CLIENT_ID + " INTEGER not null," +
            PRODUCT_ID + " INTEGER not null," +
            QUANTITY + " INTEGER not null," +
            DATE + " INTEGER not null, " +
            UNIT_PRICE + " REAL ," +
            IS_PAID + " INTEGER ," +
            CREATED_DATE + " INTEGER DEFAULT CURRENT_TIMESTAMP," +
            DESCRIPTION + " TEXT ," +
            TYPE + " TEXT ," +
            _UPDATED + " INTEGER," +
            _DELETED + " BOOLEAN," +
            " FOREIGN KEY(client_id) REFERENCES Client(_id)," +
            " FOREIGN KEY(product_id) REFERENCES Product(_id))";

    public static final String DROP_TABLE_STOCKED_ITEM = " DROP TABLE IF EXISTS " + TABLE_NAME;
    // @formatter:on

    private static final long serialVersionUID = -5435670920302756945L;

    private long id;
    private long idProduct = 0;
    private double quantity = 0;
    private long idClient = 0;
    private double unitPrice = 0;
    private boolean isPaid;
    private long date;
    TYPE_PRODUCT type;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public BaseSaleStockedProduct(long idProd, double quantity, long date) {
        this.setIdProduct(idProd);
        this.setQuantity(quantity);
        this.setDate(date);
    }

    public BaseSaleStockedProduct(Cursor c) {
        long idProd = c.getInt(c.getColumnIndex(BaseSaleStockedProduct.PRODUCT_ID));
        long idClient = c.getInt(c.getColumnIndex(BaseSaleStockedProduct.CLIENT_ID));
        long id = c.getInt(c.getColumnIndex(BaseSaleStockedProduct.ID));
        long dateProd = c.getLong(c.getColumnIndex(BaseSaleStockedProduct.DATE));
        double quantity = c.getInt(c.getColumnIndex(BaseSaleStockedProduct.QUANTITY));
        double unitPrice = c.getDouble(c
                .getColumnIndex(BaseSaleStockedProduct.UNIT_PRICE));

        this.setId(id);
        this.setIdProduct(idProd);
        this.setQuantity(quantity);
        this.setIdClient(idClient);
        this.setUnitPrice(unitPrice);
        this.setDate(dateProd);
        this.type = TYPE_PRODUCT.valueOf(c.getString(c
                .getColumnIndex(BaseSaleStockedProduct.TYPE)));
    }

    public BaseSaleStockedProduct(long id, long idProd, double quantity, long idClient,
                                  double unitPrice, long date) {
        this.setId(id);
        this.setIdProduct(idProd);
        this.setQuantity(quantity);
        this.setIdClient(idClient);
        this.setUnitPrice(unitPrice);
        this.setDate(date);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(long idProd) {
        this.idProduct = idProd;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public abstract BaseSaleStockedProduct.TYPE_PRODUCT getType();

    @Override
    public ContentValues getAsContentValue() {
        ContentValues values = new ContentValues();
        values.put(BaseSaleStockedProduct.CLIENT_ID, getIdClient());
        values.put(BaseSaleStockedProduct.PRODUCT_ID, getIdProduct());
        values.put(BaseSaleStockedProduct.QUANTITY, getQuantity());
        values.put(BaseSaleStockedProduct.UNIT_PRICE, getUnitPrice());
        values.put(BaseSaleStockedProduct.DATE, getDate());
        values.put(BaseSaleStockedProduct.IS_PAID, isPaid());
        values.put(BaseSaleStockedProduct.TYPE, getType().name());
        return values;
    }

    //non-saved on db
    private double totalPrice;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}