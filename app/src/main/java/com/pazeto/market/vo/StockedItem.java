package com.pazeto.market.vo;

import java.io.Serializable;

import android.database.Cursor;

/**
 * 
 * @author pazeto
 *
 */
public class StockedItem implements Serializable {
	
	public static final String TABLE_NAME = "StockedItem";
	public static final String ID = "_id";
	public static final String PRODUCT_ID = "product_id";
	public static final String CLIENT_ID = "client_id";
	public static final String QUANTITY = "quantity";
	public static final String DATE = "date";
	public static final String CREATED_DATE = "created_date";
	public static final String DESCRIPTION = "description";
	public static final String UNIT_PRICE = "unit_price";
	public static final String IS_PAID = "is_paid";
	public static final String _UPDATED = "_updated";
	public static final String _DELETED = "_deleted";
	//@formatter:off
	public static final String CREATE_TABLE_STOCKED_ITEM = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+
	    		  ID +" INTEGER PRIMARY KEY,"+
	    		  CLIENT_ID +" INTEGER not null,"+
	    		  PRODUCT_ID +" INTEGER not null,"+
	    		  QUANTITY +" INTEGER not null,"+
	    		  DATE + " INTEGER not null, "+
	    		  UNIT_PRICE + " REAL ,"+
			      IS_PAID + " INTEGER ,"+
	    		  CREATED_DATE + " INTEGER DEFAULT CURRENT_TIMESTAMP,"+
	    		  DESCRIPTION +" TEXT ,"+
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

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public StockedItem(long idProd, double quantity, long date) {
		this.setIdProduct(idProd);
		this.setQuantity(quantity);
		this.setDate(date);
	}

	public StockedItem(Cursor c) {
		long idProd = c.getInt(c.getColumnIndex(StockedItem.PRODUCT_ID));
		long idClient = c.getInt(c.getColumnIndex(StockedItem.CLIENT_ID));
		long id = c.getInt(c.getColumnIndex(StockedItem.ID));
		long dateProd = c.getLong(c.getColumnIndex(StockedItem.DATE));
		double quantity = c.getInt(c.getColumnIndex(StockedItem.QUANTITY));
		double unitPrice = c.getDouble(c
				.getColumnIndex(StockedItem.UNIT_PRICE));

		this.setId(id);
		this.setIdProduct(idProd);
		this.setQuantity(quantity);
		this.setIdClient(idClient);
		this.setUnitPrice(unitPrice);
		this.setDate(dateProd);
	}

	public StockedItem(long id, long idProd, double quantity, long idClient,
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
}