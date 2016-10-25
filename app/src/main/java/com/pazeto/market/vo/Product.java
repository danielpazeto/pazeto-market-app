package com.pazeto.market.vo;

import android.database.Cursor;

public class Product {

	/**
	 * Product
	 * 
	 */
	public static final String TABLE_NAME = "Product";
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String _UPDATED = "_updated";
	public static final String _DELETED = "_deleted";
	// @formatter:off
	public static final String CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " ("
			+ ID + " INTEGER PRIMARY KEY,"
			+ NAME + " text not null,"
			+ DESCRIPTION + " text not null,"
			+ _UPDATED + " integer,"
			+ _DELETED + " integer,"
			+ " UNIQUE("+NAME+","+DESCRIPTION+"))";

	public static final String DROP_TABLE_PRODUCT = " DROP TABLE IF EXISTS "
			+ TABLE_NAME;
	// @formatter:on
	
	Long id;
	String name;
	String description;

	public Product(Cursor c) {
		setId(c.getLong(c.getColumnIndex(Product.ID)));
		setName(c.getString(c.getColumnIndex(Product.NAME)));
		setDescription(c.getString(c.getColumnIndex(Product.DESCRIPTION)));
	}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
