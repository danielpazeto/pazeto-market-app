package com.pazeto.market.vo;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * 
 * @author pazeto
 *
 */
public abstract class BaseStockedProduct extends BaseDB implements Serializable {

	public enum TYPE_PRODUCT{
		SALE, STOCKED
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
	private static final String TYPE = "type";
	public static final String _UPDATED = "_updated";
	public static final String _DELETED = "_deleted";
	//@formatter:off
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+
	    		  ID +" INTEGER PRIMARY KEY,"+
	    		  CLIENT_ID +" INTEGER not null,"+
	    		  PRODUCT_ID +" INTEGER not null,"+
	    		  QUANTITY +" INTEGER not null,"+
	    		  DATE + " INTEGER not null, "+
	    		  UNIT_PRICE + " REAL ,"+
			      IS_PAID + " INTEGER ,"+
	    		  CREATED_DATE + " INTEGER DEFAULT CURRENT_TIMESTAMP,"+
	    		  DESCRIPTION +" TEXT ,"+
			      TYPE +" TEXT ,"+
	    		  _UPDATED +" INTEGER,"+
	    		  _DELETED + " BOOLEAN," +
	    		  " FOREIGN KEY(client_id) REFERENCES Client(_id),"+
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

	public BaseStockedProduct(long idProd, double quantity, long date) {
		this.setIdProduct(idProd);
		this.setQuantity(quantity);
		this.setDate(date);
	}

	public BaseStockedProduct(Cursor c) {
		long idProd = c.getInt(c.getColumnIndex(BaseStockedProduct.PRODUCT_ID));
		long idClient = c.getInt(c.getColumnIndex(BaseStockedProduct.CLIENT_ID));
		long id = c.getInt(c.getColumnIndex(BaseStockedProduct.ID));
		long dateProd = c.getLong(c.getColumnIndex(BaseStockedProduct.DATE));
		double quantity = c.getInt(c.getColumnIndex(BaseStockedProduct.QUANTITY));
		double unitPrice = c.getDouble(c
				.getColumnIndex(BaseStockedProduct.UNIT_PRICE));

		this.setId(id);
		this.setIdProduct(idProd);
		this.setQuantity(quantity);
		this.setIdClient(idClient);
		this.setUnitPrice(unitPrice);
		this.setDate(dateProd);
	}

	public BaseStockedProduct(long id, long idProd, double quantity, long idClient,
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

	public abstract BaseStockedProduct.TYPE_PRODUCT getType();

    @Override
    public ContentValues getAsContentValue() {
        ContentValues values = new ContentValues();
        values.put(BaseStockedProduct.CLIENT_ID, getIdClient());
        values.put(BaseStockedProduct.PRODUCT_ID, getIdProduct());
        values.put(BaseStockedProduct.QUANTITY, getQuantity());
        values.put(BaseStockedProduct.UNIT_PRICE, getUnitPrice());
        values.put(BaseStockedProduct.DATE, getDate());
        values.put(BaseStockedProduct.IS_PAID, isPaid());
        values.put(BaseStockedProduct.TYPE, getType().name());
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