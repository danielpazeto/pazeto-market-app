package com.pazeto.ceasapazeto.db;

import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ClienteDB {
	  
	 /**
	  * Cliente 
	  * 
	  */
	     public static final String TABLE_NAME = "Cliente";
	     public static final String ID = "id";
	     public static final String NAME = "name";
	     public static final String PHONE1 = "phone1";
	     public static final String PHONE2 = "phone";
	     public static final String HANGAR = "hangar";
	     public static final String POSITION = "position";
	     public static final String CITY = "city";
	     public static final String ADDRESS = "address";
	     public static final String CPF_CNPJ = "cpf_cnpj";
	     public static final String CREATED_BY = "created_by";
	     public static final String OBSERVACAO = "observacao";
	     public static final String _UPDATED = "_updated";
	     public static final String _DELETED = "_deleted";
	     
	     static final String CREATE_TABLE_CLIENTE = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + "( "+
	    		  ID +" INTEGER PRIMARY KEY,"+
	    		  NAME +" VARCHAR(100) ,"+
	    		  PHONE1+" VARCHAR(100) ,"+
	    		  PHONE2+" VARCHAR(100) ," +
	    		  HANGAR +" VARCHAR(100) ,"+
	    		  POSITION +" VARCHAR(12),"+
	    		  CITY +" VARCHAR(50),"+
	    		  ADDRESS +" VARCHAR(255),"+
	    		  CPF_CNPJ +" VARCHAR(20),"+
	    		  OBSERVACAO +" VARCHAR(255),"+
	    		  CREATED_BY +" INTEGER,"+
	    		  _UPDATED +" VARCHAR(12),"+
	    		  _DELETED + "BOOLEAN,"+
	    		  "FOREIGN KEY(CREATED_BY) REFERENCES Usuario(id))";
	     
	     public final String DROP_TABLE_CLIENTE = "DROP TABLE IF EXISTS"+TABLE_NAME;
}
