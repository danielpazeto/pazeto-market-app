package com.pazeto.ceasapazeto.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pazeto.ceasapazeto.db.UsuarioDB;
import com.pazeto.ceasapazeto.db.DB;

public class DBFacade extends SQLiteOpenHelper  {



	public DBFacade(Context context) {
		super(context,DB.DATABASE_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}


	
	public void onCreate(SQLiteDatabase db) {
		try {
			 db.execSQL(UsuarioDB.CREATE_TABLE_USUARIO);
			 db.execSQL(ClienteDB.CREATE_TABLE_CLIENTE);
			 Log.d("DBFacade", "Criou Tabelas");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void insertUsuario() 
	{
		SQLiteDatabase db = null;
		db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("nome", "Daniel PAzeto"); 
		values.put("login", "daniel");
		values.put("password", "daniel");
		db.insert(UsuarioDB.TABLE_NAME, null, values);
		db.close();
		
	}
	
	public Cursor listUsuario()
	{
		SQLiteDatabase db = null;
		db = getReadableDatabase();
		
		Cursor c = db.query(UsuarioDB.TABLE_NAME, null, null,null, null, null, null);
		
		c.moveToFirst();
		String name = c.getString(c.getColumnIndex("1"));
		String login = c.getString(c.getColumnIndex("1"));
		String password = c.getString(c.getColumnIndex("2"));
		String email = c.getString(c.getColumnIndex("3"));
		String phone = c.getString(c.getColumnIndex("4"));
		//String name = c.getString(c.getColumnIndex("5"));
		Log.e("Usuarios", " oi" + name+login+password+email+phone);
		return c;
		
		
	}
	
	public void insertClientes() 
	{
		SQLiteDatabase db;
		db = getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put("name", "Clinte1"); 
			values.put("phone1", "9685758");
			values.put("created_by", "1");
			db.insert(ClienteDB.TABLE_NAME, null, values);
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			db.close();
		}
		
		
		
		
	}
	public Cursor listCLientes()
	{
		SQLiteDatabase db = null;
		db = getReadableDatabase();
		try {
			Cursor c = db.query(ClienteDB.TABLE_NAME, new String[]{"*","1 _id"}, null,null, null, null, null);
			
			Log.d("CLientes:  ", ""+c.getCount());
			
			return c;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			db.close();
		}
		return null;
		
		
		
		
		
	}

	
	
	
	
}
