package com.pazeto.comercio.db;

public class Payment {
	  
	 /**
	  * Product 
	  * 
	  */
	     public static final String TABLE_NAME = "Product";
	     public static final String ID = "_id";
	     public static final String ID_CLIENT = "id_client";
	     public static final String VALUE = "value";
	     public static final String DATE = "date";
	     public static final String DESCRIPTION = "description";
	     public static final String _UPDATED = "_updated";
	     public static final String _DELETED = "_deleted";
	     
	     static final String CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+
	    		  ID +" INTEGER PRIMARY KEY,"+
	    		  ID_CLIENT +" INTEGER,"+
	    		  DESCRIPTION +" TEXT,"+
	    		  VALUE +" REAL,"+
	    		  DATE +" TEXT,"+
	    		  _UPDATED +" INTEGER,"+
	    		  _DELETED + " BOOLEAN)";
	     
	     public static final String DROP_TABLE_PRODUCT = " DROP TABLE IF EXISTS " + TABLE_NAME;
}
