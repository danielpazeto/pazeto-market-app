package com.pazeto.ceasapazeto.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pazeto.ceasapazeto.vo.Client;
import com.pazeto.ceasapazeto.vo.Product;
import com.pazeto.ceasapazeto.vo.StockedItem;
import com.pazeto.ceasapazeto.vo.SaleItem;

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
			db.execSQL(Client.CREATE_TABLE_CLIENT);
			db.execSQL(Product.CREATE_TABLE_PRODUCT);
			db.execSQL(StockedItem.CREATE_TABLE_STOCKED_ITEM);
			db.execSQL(SaleItem.CREATE_TABLE_SALE);
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

	public boolean persistClient(Client client, SQLiteDatabase sql) {
		try {
			ContentValues values = client.getAsContentValue();
			if (client.getId() > 0) {
				sql.update(Client.TABLE_NAME, values, Client.ID,
						new String[] { client.getId() + "" });
			} else {
				sql.insert(Client.TABLE_NAME, null, values);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public long insertProductStock(StockedItem prod, SQLiteDatabase sql)
			throws Exception {
		ContentValues values = new ContentValues();
		values.put(StockedItem.PRODUCT_ID, prod.getIdProduct());
		values.put(StockedItem.QUANTITY, prod.getQuantity());
		values.put(StockedItem.CLIENT_ID, prod.getIdClient());
		values.put(StockedItem.UNIT_PRICE, prod.getUnitPrice());
		values.put(StockedItem.IS_PAID, prod.isPaid());
		values.put(StockedItem.DATE, prod.getDate());
		if (prod.getId() != 0) {
			values.put(StockedItem.ID, prod.getId());
			return sql.update(StockedItem.TABLE_NAME, values, StockedItem.ID
					+ " = " + prod.getId(), null);
		} else {
			return sql.insert(StockedItem.TABLE_NAME, null, values);
		}

	}

	public int removeProductDay(StockedItem prod) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			if (prod.getId() != 0) {
				return db.delete(StockedItem.TABLE_NAME,
						"_id=" + prod.getId(), null);
			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	public long insertSale(SaleItem saleItem, SQLiteDatabase sql) {
		try {
			ContentValues values = new ContentValues();
			values.put(SaleItem.ID_CLIENT, saleItem.getIdClient());
			values.put(SaleItem.ID_PRODUCT, saleItem.getIdProduct());
			values.put(SaleItem.QUANTITY, saleItem.getQuantity());
			values.put(SaleItem.UNIT_PRICE, saleItem.getUnitPrice());
			values.put(SaleItem.DATE, saleItem.getDate());
			values.put(SaleItem.IS_PAID, saleItem.isPaid());
			if (saleItem.getId() != 0) {
				values.put(StockedItem.ID, saleItem.getId());
				return sql.update(SaleItem.TABLE_NAME, values, SaleItem.ID + " = "
						+ saleItem.getId(), null);
			} else {
				return sql.insert(SaleItem.TABLE_NAME, null, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int removeSale(SaleItem sale, SQLiteDatabase sql) {
		try {
			if (sale.getId() != 0) {
				return sql.delete(SaleItem.TABLE_NAME, "_id=" + sale.getId(), null);
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
			c = sql.query(StockedItem.TABLE_NAME, null, "date=?",
					new String[] { unixDate + "" }, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public Cursor listProductsDayAvailables(SQLiteDatabase sql) {
		Cursor c = null;
		try {
			c = sql.rawQuery("SELECT * FROM " + StockedItem.TABLE_NAME
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
			Cursor c = db.rawQuery("select * from " + SaleItem.TABLE_NAME
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
	public ArrayList<SaleItem> listSalePerDateAndClient(long date, Client client,
			SQLiteDatabase sql) {
		ArrayList<SaleItem> salesPerDateAndClient = new ArrayList<SaleItem>();
		try {

			StringBuilder query = new StringBuilder("select * from "
					+ SaleItem.TABLE_NAME + " where");

			if (date > 0) {
				query.append(" date = " + date + " AND ");
			} else {
				query.append(" date > 0 AND ");
			}

			query.replace(query.length() - 4, query.length(), "");
			Log.d(TAG, "QUERYYY: " + query.toString());
			Cursor c = sql.rawQuery(query.toString(), null);

			while (c.moveToNext()) {
				SaleItem sale = new SaleItem(c);
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
			Cursor c = db.rawQuery("SELECT " + StockedItem.QUANTITY + " FROM "
					+ StockedItem.TABLE_NAME
					+ " WHERE strftime('%s',date) = strftime('%s','" + date
					+ "') AND " + StockedItem.ID + " = " + idProductDay, null);
			float currentQuantity = c.getFloat((c
					.getColumnIndex(StockedItem.QUANTITY)));

			float newQuantity = currentQuantity - outQuantity;

			ContentValues values = new ContentValues();
			values.put(StockedItem.QUANTITY, newQuantity);
			db.update(StockedItem.TABLE_NAME, values, "_id=" + idProductDay,
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

	public Product getProduct(long id) {

		SQLiteDatabase db = null;
		db = getReadableDatabase();
		try {
			Cursor c = db.query(Product.TABLE_NAME, new String[] { "name",
					"description" }, "_id = " + id, null, null, null, null);
			Product prod = new Product(c);
			return prod;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}

	public Client getClient(long id) {

		SQLiteDatabase db = null;
		db = getReadableDatabase();
		try {
			Cursor c = db.query(Client.TABLE_NAME, null,
					Client.ID + " = " + id, null, null, null, null);
			if (c.getCount() > 0 && c.moveToFirst()) {
				return new Client(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}
}
