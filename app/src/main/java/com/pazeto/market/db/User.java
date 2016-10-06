package com.pazeto.market.db;


public class User {
	  
	 /**
	  * Usuário
	  * 
	  */
	     public static final String TABLE_NAME = "Usuario";
	     public static final String ID = "_id";
	     public static final String NOME = "nome";
	     public static final String LOGIN = "login";
	     public static final String PASSWORD = "password";
	     public static final String EMAIL = "email";
	     public static final String PHONE = "phone";
	     
	     static final String CREATE_TABLE_USUARIO = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + "( "+
	    		  ID +" INTEGER PRIMARY KEY,"+
	    		  NOME +" VARCHAR(100),"+
	    		  LOGIN+" VARCHAR(100),"+
	    		  PASSWORD+" VARCHAR(100)," +
	    		  EMAIL +" VARCHAR(100),"+
	    		  PHONE +" VARCHAR(12))";
	     
	     public static final String DROP_TABLE_USUARIO = "DROP TABLE IF EXISTS"+TABLE_NAME;

}
