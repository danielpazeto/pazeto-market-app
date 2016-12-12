package com.pazeto.market.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pazeto.market.vo.BaseSaleStockedProduct;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.Product;
import com.pazeto.market.vo.StockedItem;
import com.pazeto.market.vo.SaleItem;

public class DBFacade extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "market.db";
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
            db.execSQL(BaseSaleStockedProduct.CREATE_TABLE);
            Log.d("DBFacade", "Criou Tabelas");
        } catch (Exception e) {
            Log.e("db", "Failure on create tables " + e);
        }

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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

    public Cursor listCursorClients() {
        SQLiteDatabase db = getReadableDatabase();
        try {
            return db.query(Client.TABLE_NAME, null, null, null, null,
                    null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // db.close();
        }
        return null;
    }

    public List<Client> listClients() {
        List<Client> list = new ArrayList<>();
        Cursor c = listCursorClients();
        while (c.moveToNext()) {
            list.add(new Client(c));
        }
        return list;
    }

    public boolean persistProduct(Product prod) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = prod.getAsContentValue();
            if (prod.getId() > 0) {
                db.update(Product.TABLE_NAME, values, Product.ID+" = ?",
                        new String[]{prod.getId() + ""});
            } else {
                db.insert(Product.TABLE_NAME, null, values);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public Cursor listCursorAllProducts() {
        SQLiteDatabase db;
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

    public List<Product> listProducts() {
        try {
            Cursor c = listCursorAllProducts();
            List<Product> list = new ArrayList<>();
            while (c.moveToNext()) {
                list.add(new Product(c));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean persistClient(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = client.getAsContentValue();
            if (client.getId() > 0) {
                db.update(Client.TABLE_NAME, values, Client.ID+" = ?",
                        new String[]{client.getId() + ""});
            } else {
                db.insert(Client.TABLE_NAME, null, values);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }

    }

//    public long insertProductStock(StockedItem prod, SQLiteDatabase sql)
//            throws Exception {
//        ContentValues values = new ContentValues();
//        values.put(StockedItem.PRODUCT_ID, prod.getIdProduct());
//        values.put(StockedItem.QUANTITY, prod.getQuantity());
//        values.put(StockedItem.CLIENT_ID, prod.getIdClient());
//        values.put(StockedItem.UNIT_PRICE, prod.getUnitPrice());
//        values.put(StockedItem.IS_PAID, prod.isPaid());
//        values.put(StockedItem.DATE, prod.getDate());
//        if (prod.getId() != 0) {
//            values.put(StockedItem.ID, prod.getId());
//            return sql.update(StockedItem.TABLE_NAME, values, StockedItem.ID
//                    + " = " + prod.getId(), null);
//        } else {
//            return sql.insert(StockedItem.TABLE_NAME, null, values);
//        }
//
//    }

//    public int removeProductDay(StockedItem prod) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        try {
//            if (prod.getId() != 0) {
//                return db.delete(StockedItem.TABLE_NAME,
//                        "_id=" + prod.getId(), null);
//            }
//            return -1;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -1;
//        }
//
//    }

    public long insertSaleStock(BaseSaleStockedProduct item, SQLiteDatabase sql) {
        try {
            ContentValues values = item.getAsContentValue();
            if (item.getId() != 0) {
                values.put(StockedItem.ID, item.getId());
                sql.update(BaseSaleStockedProduct.TABLE_NAME, values, BaseSaleStockedProduct.ID + " = "
                        + item.getId(), null);
                return item.getId();
            } else {
                return sql.insert(BaseSaleStockedProduct.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int removeSaleStock(BaseSaleStockedProduct sale, SQLiteDatabase sql) {
        try {
            if (sale.getId() != 0) {
                return sql.delete(BaseSaleStockedProduct.TABLE_NAME, "_id=" + sale.getId(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

//    public Cursor listStockProductsInDate(long unixDate, SQLiteDatabase sql) {
//        Cursor c = null;
//        try {
//            // System.out.println("DATA PARA FILTRO: " + unixDate);
//            // c = sql.rawQuery("SELECT * FROM " + ProductStock.TABLE_NAME
//            // + " WHERE date = " + unixDate, null);
//            c = sql.query(StockedItem.TABLE_NAME, null, "type=? and date=?",
//                    new String[]{SaleItem.TYPE_PRODUCT.SALE.name(), unixDate + ""}, null, null, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return c;
//    }

//    public Cursor listProductsDayAvailables(SQLiteDatabase sql) {
//        Cursor c = null;
//        try {
//            c = sql.rawQuery("SELECT * FROM " + StockedItem.TABLE_NAME
//                    + " WHERE type=" + SaleItem.TYPE_PRODUCT.SALE.name() + " ", null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return c;
//    }

//    public Cursor listSalePerDate(String date) {
//        SQLiteDatabase db = null;
//        db = getReadableDatabase();
//        try {
//            System.out.println("DATA PARA FILTRO: " + date);
//            Cursor c = db.rawQuery("select * from " + SaleItem.TABLE_NAME
//                    + " where type=" + SaleItem.TYPE_PRODUCT.SALE.name() + " strftime('%s',date) = strftime('%s','" + date
//                    + "')", null);
//            // select * from ProductDay where strftime('%s',date) <
//            // strftime('%s','2013-12-27');
//            return c;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // db.close();
//        }
//        return null;
//    }

    /**
     * List by date and client, if date is null, get all date, if client is null
     * get all client sales
     *
     * @param date
     * @param client
     * @param sql
     * @return
     */
    public HashMap<Long, List<BaseSaleStockedProduct>> listSaleAndStockPerDateAndClient(long date, Client client,
                                                                                        BaseSaleStockedProduct.TYPE_PRODUCT type,
                                                                                        SQLiteDatabase sql) {

        return this.listSaleAndStockPerDateAndClient(date, date, type, client, sql);
//
//
//        ArrayList<BaseSaleStockedProduct> itemsPerDateAndClient = new ArrayList<>();
//        Cursor c = null;
//        try {
//
//            StringBuilder query = new StringBuilder("select * from "
//                    + BaseSaleStockedProduct.TABLE_NAME + " where type='" + type.name() + "' AND ");
//
//            if (date > 0) {
//                query.append(" date = " + date + " AND ");
//            } else {
//                query.append(" date > 0 AND ");
//            }
//
//            query.append(BaseSaleStockedProduct.CLIENT_ID + " = " + client.getId());
//
////			query.replace(query.length() - 4, query.length(), "");
//            Log.d(TAG, "QUERYYY: " + query.toString());
//            c = sql.rawQuery(query.toString(), null);
//
//            while (c.moveToNext()) {
//                BaseSaleStockedProduct item = null;
//                if (type.equals(BaseSaleStockedProduct.TYPE_PRODUCT.SALE)) {
//                    item = new SaleItem(c);
//                } else if (type.equals(BaseSaleStockedProduct.TYPE_PRODUCT.STOCKED)) {
//                    item = new StockedItem(c);
//                }
//                itemsPerDateAndClient.add(item);
//            }
//
//            //DEBUG
//            Iterator it = itemsPerDateAndClient.iterator();
//            while (it.hasNext()) {
//                Map.Entry pair = (Map.Entry) it.next();
//                Log.d(TAG, "***** Date = " + pair.getKey());
//                it.remove(); // avoids a ConcurrentModificationException
//                List<SaleItem> l = (List) pair.getValue();
//                for (SaleItem s : l) {
//                    Log.d(TAG, s.getQuantity() + " : " + s.getIdProduct() + " : " + s.getUnitPrice());
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return itemsPerDateAndClient;
    }

    /**
     * List by date and client, if date is null, get all date, if client is null
     * get all client sales
     *
     * @param initialDate
     * @param finalDate
     * @param client
     * @param sql
     * @return
     */
    public HashMap<Long, List<BaseSaleStockedProduct>> listSaleAndStockPerDateAndClient(
            long initialDate, long finalDate, BaseSaleStockedProduct.TYPE_PRODUCT type,
            Client client, SQLiteDatabase sql) {
        HashMap<Long, List<BaseSaleStockedProduct>> salesPerDateAndClient = new HashMap<>();
        try {

            StringBuilder query = new StringBuilder("SELECT * FROM "
                    + SaleItem.TABLE_NAME + " WHERE ");

            query.append(SaleItem.CLIENT_ID + " = " + client.getId() + " AND ");
            query.append("date >= " + initialDate + " AND date <=" + finalDate);

            if (type.equals(BaseSaleStockedProduct.TYPE_PRODUCT.SALE) ||
                    type.equals(BaseSaleStockedProduct.TYPE_PRODUCT.STOCKED)) {
                query.append(" AND type LIKE '" + type.name() + "'");
            }


            Log.d(TAG, "QUERYYY: " + query.toString());
            Cursor c = sql.rawQuery(query.toString(), null);

            while (c.moveToNext()) {
                BaseSaleStockedProduct.TYPE_PRODUCT saleStockStype = BaseSaleStockedProduct.TYPE_PRODUCT.valueOf(c.getString(c
                        .getColumnIndex(BaseSaleStockedProduct.TYPE)));
                BaseSaleStockedProduct item = null;
                if (saleStockStype.equals(BaseSaleStockedProduct.TYPE_PRODUCT.SALE)) {
                    item = new SaleItem(c);
                } else if (saleStockStype.equals(BaseSaleStockedProduct.TYPE_PRODUCT.STOCKED)) {
                    item = new StockedItem(c);
                }
                //separate per date
                List<BaseSaleStockedProduct> list = salesPerDateAndClient.get(item.getDate());
                if (list == null) {
                    list = new ArrayList();
                    salesPerDateAndClient.put(item.getDate(), list);
                }
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salesPerDateAndClient;
    }

    public Product getProduct(long id) {

        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor c = db.query(Product.TABLE_NAME, null, "_id = " + id, null, null, null, null);
            if (c.getCount() > 0 && c.moveToFirst()) {
                return new Product(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // db.close();
        }
        return null;
    }

    public Client getClient(long id) {

        SQLiteDatabase db = getReadableDatabase();
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
