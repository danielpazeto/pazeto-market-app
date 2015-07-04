package com.pazeto.ceasapazeto.db;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pazeto.ceasapazeto.vo.Client;
import com.pazeto.ceasapazeto.vo.Product;
import com.pazeto.ceasapazeto.vo.ProductStock;
import com.pazeto.ceasapazeto.vo.Sale;

public class DBFacade extends SQLiteOpenHelper {

	static final String DATABASE_NAME = "CEASAPAZETO.db";
	private static final String TAG = "DBFACADE";

	public DBFacade(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Tentando criar tabelas...");
		try {
			db.execSQL(User.CREATE_TABLE_USUARIO);
			db.execSQL(Client.CREATE_TABLE_CLIENTE);
			db.execSQL(Product.CREATE_TABLE_PRODUCT);
			db.execSQL(ProductStock.CREATE_TABLE_STOCK_PRODUCT);
			db.execSQL(Sale.CREATE_TABLE_SALE);
			Log.d("DBFacade", "Criou Tabelas");
		} catch (Exception e) {
			Log.e("db", "Failure on create tables " + e);
		}

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Product.CREATE_TABLE_PRODUCT);
		onCreate(db);
	}

	public void insertUsuario() {
		SQLiteDatabase db = null;
		db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("nome", "Daniel PAzeto");
		values.put("login", "daniel");
		values.put("password", "daniel");
		db.insert(User.TABLE_NAME, null, values);
		db.close();

	}

	public Cursor listUsuario() {
		SQLiteDatabase db = null;
		db = getReadableDatabase();

		Cursor c = db
				.query(User.TABLE_NAME, null, null, null, null, null, null);

		c.moveToFirst();
		String name = c.getString(c.getColumnIndex("1"));
		String login = c.getString(c.getColumnIndex("1"));
		String password = c.getString(c.getColumnIndex("2"));
		String email = c.getString(c.getColumnIndex("3"));
		String phone = c.getString(c.getColumnIndex("4"));
		// String name = c.getString(c.getColumnIndex("5"));
		Log.e("Usuarios", " oi" + name + login + password + email + phone);
		return c;

	}

	// public void insertClientes() {
	// SQLiteDatabase db;
	// db = this.getWritableDatabase();
	// try {
	// ContentValues values = new ContentValues();
	// values.put("name", "Clinte1");
	// values.put("phone1", "9685758");
	// values.put("created_by", "1");
	// db.insert(Client.TABLE_NAME, null, values);
	// } catch (Exception e) {
	// // TODO: handle exception
	// } finally {
	// db.close();
	// }
	//
	// }

	public Cursor listClients() {
		SQLiteDatabase db = null;
		db = getReadableDatabase();
		try {
			Cursor c = db.query(Client.TABLE_NAME, null, null, null, null,
					null, null);
			Log.d("Clientes:  ", "" + c.getCount());
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}

	public boolean insertProduct(Product prod) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put("name", prod.getName());
			values.put("description", prod.getDescription());
			db.insert(Product.TABLE_NAME, null, values);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
	}

	public Cursor listProducts() {

		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			Cursor c = db.query(Product.TABLE_NAME, null, null, null, null,
					null, null);
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean insertClient(Client client, SQLiteDatabase sql) {
		try {
			ContentValues values = client.getAsContentValue();
			sql.insert(Client.TABLE_NAME, null, values);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
		}

	}

	public long insertProductStock(ProductStock prod, SQLiteDatabase sql)
			throws Exception {
		ContentValues values = new ContentValues();
		values.put(ProductStock.PRODUCT_ID, prod.getIdProduct());
		values.put(ProductStock.QUANTITY, prod.getQuantity());
		values.put(ProductStock.CLIENT_ID, prod.getIdClient());
		values.put(ProductStock.UNIT_PRICE, prod.getUnitPrice());
		values.put(ProductStock.IS_PAID, prod.isPaid());
		values.put(ProductStock.DATE, prod.getDate());
		if (prod.getId() != 0) {
			values.put(ProductStock.ID, prod.getId());
			return sql.update(ProductStock.TABLE_NAME, values, ProductStock.ID
					+ " = " + prod.getId(), null);
		} else {
			return sql.insert(ProductStock.TABLE_NAME, null, values);
		}

	}

	public int removeProductDay(ProductStock prod) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			if (prod.getId() != 0) {
				return db.delete(ProductStock.TABLE_NAME,
						"_id=" + prod.getId(), null);
			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	public long insertSale(Sale saleItem, SQLiteDatabase sql) {
		try {
			ContentValues values = new ContentValues();

			values.put(Sale.ID_CLIENT, saleItem.getIdClient());
			values.put(Sale.ID_PRODUCT, saleItem.getIdProduct());
			values.put(Sale.QUANTITY, saleItem.getQuantity());
			values.put(Sale.UNIT_PRICE, saleItem.getUnitPrice());
			values.put(Sale.DATE, saleItem.getDate());
			values.put(Sale.IS_PAID, saleItem.isPaid());
			if (saleItem.getId() != 0) {
				values.put(ProductStock.ID, saleItem.getId());
				return sql.update(Sale.TABLE_NAME, values, Sale.ID + " = "
						+ saleItem.getId(), null);
			} else {
				return sql.insert(Sale.TABLE_NAME, null, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int removeSale(Sale sale, SQLiteDatabase sql) {
		try {
			if (sale.getId() != 0) {
				return sql.delete(Sale.TABLE_NAME, "_id=" + sale.getId(), null);
			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public Cursor listStockProductsInDate(long unixDate, SQLiteDatabase sql) {
		Cursor c = null;
		try {
			// System.out.println("DATA PARA FILTRO: " + unixDate);
			// c = sql.rawQuery("SELECT * FROM " + ProductStock.TABLE_NAME
			// + " WHERE date = " + unixDate, null);
			c = sql.query(ProductStock.TABLE_NAME, null, "date=?",
					new String[] { unixDate + "" }, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public Cursor listProductsDayAvailables(SQLiteDatabase sql) {
		Cursor c = null;
		try {
			c = sql.rawQuery("SELECT * FROM " + ProductStock.TABLE_NAME
					+ " WHERE quantity > 0 ", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public Cursor listSalePerDate(String date) {
		SQLiteDatabase db = null;
		db = getReadableDatabase();
		try {
			System.out.println("DATA PARA FILTRO: " + date);
			Cursor c = db.rawQuery("select * from " + Sale.TABLE_NAME
					+ " where strftime('%s',date) = strftime('%s','" + date
					+ "')", null);
			// select * from ProductDay where strftime('%s',date) <
			// strftime('%s','2013-12-27');
			return c;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}

	/**
	 * List by date and client, if date is null, get all date, if client is null
	 * get all client sales
	 * 
	 * @param date
	 * @param client
	 * @param sql
	 * @return
	 */
	public ArrayList<Sale> listSalePerDateAndClient(long date, Client client,
			SQLiteDatabase sql) {
		ArrayList<Sale> salesPerDateAndClient = new ArrayList<Sale>();
		try {

			StringBuilder query = new StringBuilder("select * from "
					+ Sale.TABLE_NAME + " where");

			if (date > 0) {
				query.append(" date = " + date + " AND ");
			} else {
				query.append(" date > 0 AND ");
			}

			query.replace(query.length() - 4, query.length(), "");
			Log.d(TAG, "QUERYYY: " + query.toString());
			Cursor c = sql.rawQuery(query.toString(), null);

			while (c.moveToNext()) {
				Sale sale = new Sale(c);
				salesPerDateAndClient.add(sale);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesPerDateAndClient;
	}

	public boolean outProductDay(String date, int idProductDay,
			float outQuantity) {
		SQLiteDatabase db = null;
		db = getWritableDatabase();
		try {
			Cursor c = db.rawQuery("SELECT " + ProductStock.QUANTITY + " FROM "
					+ ProductStock.TABLE_NAME
					+ " WHERE strftime('%s',date) = strftime('%s','" + date
					+ "') AND " + ProductStock.ID + " = " + idProductDay, null);
			float currentQuantity = c.getFloat((c
					.getColumnIndex(ProductStock.QUANTITY)));

			float newQuantity = currentQuantity - outQuantity;

			ContentValues values = new ContentValues();
			values.put(ProductStock.QUANTITY, newQuantity);
			db.update(ProductStock.TABLE_NAME, values, "_id=" + idProductDay,
					null);
			// select * from ProductDay where strftime('%s',date) <
			// strftime('%s','2013-12-27');
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			// db.close();
		}

	}

	public Cursor getProductNameById(long id) {

		SQLiteDatabase db = null;
		db = getReadableDatabase();
		try {
			Cursor c = db.query(Product.TABLE_NAME, new String[] { "name",
					"description" }, "_id = " + id, null, null, null, null);
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}

}
