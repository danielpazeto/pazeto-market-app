package com.pazeto.market.vo;

import android.database.Cursor;

/**
 * @author pazeto
 */
public class SaleItem extends BaseStockedProduct {

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

    //	public static final String TABLE_NAME = "Venda";
//	public static final String ID = "_id";
//	public static final String ID_CLIENT = "id_client";
//	public static final String ID_PRODUCT = "id_product";
//	public static final String QUANTITY = "quantity";
//	public static final String DATE = "date";
//	public static final String CREATED_DATE = "created_date";
//	public static final String UNIT_PRICE = "unit_price";
//	public static final String IS_PAID = "is_paid";
//	public static final String OBSERVATION = "observation";
//	public static final String _UPDATED = "_updated";
//	public static final String _DELETED = "_deleted";
//	// @formatter:off
//	public static final String CREATE_TABLE_SALE = "CREATE TABLE IF NOT EXISTS "
//			+ TABLE_NAME+ "( "
//			+ ID+ " INTEGER PRIMARY KEY,"
//			+ ID_CLIENT	+ " INTEGER not null ,"
//			+ ID_PRODUCT+ " INTEGER not null,"
//			+ QUANTITY	+ " integer ,"
//			+ DATE	+ " integer not null ,"
//			+ CREATED_DATE + " integer DEFAULT CURRENT_TIMESTAMP ,"
//			+ UNIT_PRICE+ " integer ,"
//			+ IS_PAID+ " integer ,"
//			+ OBSERVATION+ " TEXT ,"
//			+ _UPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP,"
//			+ _DELETED	+ " BOOLEAN )";
//
//	public static final String DROP_TABLE_SALE = "DROP TABLE IF EXISTS"
//			+ TABLE_NAME;
//	// @formatter:on
//	private long id;
//	private long idProduct = 0;
//	private long idClient = 0;
//	private double quantity = 0;
//	private long date;
//	private String created_date;
//	private double unitPrice;


    //	private boolean isPaid;
//	private String observation;
//
//	public SaleItem(long idProd, double quantity, long date) {
//		this.setIdProduct(idProd);
//		this.setQuantity(quantity);
//		this.setDate(date);
//	}
//
//	public SaleItem(long id, long idProd, double quantity, long date,
//			double priceUnit, long idClient, boolean isPaid) {
//		this.setId(id);
//		this.setIdClient(idClient);
//		this.setIdProduct(idProd);
//		this.setQuantity(quantity);
//		this.setUnitPrice(priceUnit);
//		this.setPaid(isPaid);
//		this.setDate(date);
//	}
//
//	public SaleItem(Cursor c) {
//		this.setId(c.getInt(c.getColumnIndex(SaleItem.ID)));
//		this.setIdClient(c.getInt(c.getColumnIndex(SaleItem.ID_CLIENT)));
//		this.setIdProduct(c.getInt(c.getColumnIndex(SaleItem.ID_PRODUCT)));
//		this.setQuantity(c.getInt(c.getColumnIndex(SaleItem.QUANTITY)));
//		this.setUnitPrice(c.getInt(c.getColumnIndex(SaleItem.UNIT_PRICE)));
//		this.setPaid(c.getInt(c.getColumnIndex(SaleItem.IS_PAID)) > 0);
//		this.setDate(c.getLong(c.getColumnIndex(SaleItem.DATE)));
//	}
//
//	public long getIdProduct() {
//		return idProduct;
//	}
//
//	public void setIdProduct(long idProduct) {
//		this.idProduct = idProduct;
//	}
//
//	public long getIdClient() {
//		return idClient;
//	}
//
//	public void setIdClient(long idClient) {
//		this.idClient = idClient;
//	}
//
//	public double getQuantity() {
//		return quantity;
//	}
//
//	public void setQuantity(double quantity) {
//		this.quantity = quantity;
//	}
//
//	public long getDate() {
//		return date;
//	}
//
//	public void setDate(long date) {
//		this.date = date;
//	}
//
//	public double getUnitPrice() {
//		return unitPrice;
//	}
//
//	public void setUnitPrice(double unit_price) {
//		this.unitPrice = unit_price;
//	}
//
//	public boolean isPaid() {
//		return isPaid;
//	}
//
//	public void setPaid(boolean isPaid) {
//		this.isPaid = isPaid;
//	}
//
//	public String getObservation() {
//		return observation;
//	}
//
//	public void setObservation(String observation) {
//		this.observation = observation;
//	}
//
//	public long getId() {
//		return id;
//	}
//
//	public void setId(long id) {
//		this.id = id;
//	}
//
//	public String getCreated_date() {
//		return created_date;
//	}
//

//
//	@Override
//	public ContentValues getAsContentValue() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}