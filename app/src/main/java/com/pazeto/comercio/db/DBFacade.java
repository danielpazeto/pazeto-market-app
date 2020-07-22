//package com.pazeto.comercio.db;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import com.pazeto.comercio.vo.BaseSaleStocked;
//import com.pazeto.comercio.vo.Client;
//import com.pazeto.comercio.vo.Product;
//import com.pazeto.comercio.vo.Stock;
//
//public class DBFacade extends SQLiteOpenHelper {
//
//    static final String DATABASE_NAME = "market.db";
//    private static final String TAG = "DBFACADE";
//
//    public DBFacade(Context context) {
//        super(context, DATABASE_NAME, null, 1);
//    }
//
//    public void onCreate(SQLiteDatabase db) {
//        Log.d(TAG, "Tentando criar tabelas...");
//        try {
////            db.execSQL(User.CREATE_TABLE_USUARIO);
////            db.execSQL(Client.CREATE_TABLE_CLIENT);
////            db.execSQL(Product.CREATE_TABLE_PRODUCT);
////            db.execSQL(BaseSaleStockedProduct.CREATE_TABLE);
//            Log.d("DBFacade", "Criou Tabelas");
//        } catch (Exception e) {
//            Log.e("db", "Failure on create tables " + e);
//        }
//
//    }
//
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onCreate(db);
//    }
//
//    public void insertUsuario() {
//        SQLiteDatabase db = null;
//        db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("nome", "Daniel PAzeto");
//        values.put("login", "daniel");
//        values.put("password", "daniel");
//        db.insert(User.TABLE_NAME, null, values);
//        db.close();
//
//    }
//
//    public Cursor listUsuario() {
//        SQLiteDatabase db = null;
//        db = getReadableDatabase();
//        Cursor c = db
//                .query(User.TABLE_NAME, null, null, null, null, null, null);
//
//        c.moveToFirst();
//        String name = c.getString(c.getColumnIndex("1"));
//        String login = c.getString(c.getColumnIndex("1"));
//        String password = c.getString(c.getColumnIndex("2"));
//        String email = c.getString(c.getColumnIndex("3"));
//        String phone = c.getString(c.getColumnIndex("4"));
//        // String name = c.getString(c.getColumnIndex("5"));
//        Log.e("Usuarios", " oi" + name + login + password + email + phone);
//        return c;
//
//    }
//
//    public Cursor listCursorClients() {
//        SQLiteDatabase db = getReadableDatabase();
////        try {
////            return db.query(Client.TABLE_NAME, null, null, null, null,
////                    null, null);
////        } catch (Exception e) {
////            e.printStackTrace();
////        } finally {
////            // db.close();
////        }
//        return null;
//    }
//
//    public List<Client> listClients() {
////        List<Client> list = new ArrayList<>();
////        Cursor c = listCursorClients();
////        while (c.moveToNext()) {
////            list.add(new Client(c));
////        }
////        return list;
//        return null;
//    }
//
//
//
//    /**
//     * List by date and client, if date is null, get all date, if client is null
//     * get all client sales
//     *
//     * @param date
//     * @param client
//     * @param sql
//     * @return
//     */
//    public HashMap<Long, List<BaseSaleStocked>> listSaleAndStockPerDateAndClient(long date, Client client,
//                                                                                 BaseSaleStocked.TYPE type,
//                                                                                 SQLiteDatabase sql) {
//
//        return this.listSaleAndStockPerDateAndClient(date, date, type, client, sql);
////
////
////        ArrayList<BaseSaleStockedProduct> itemsPerDateAndClient = new ArrayList<>();
////        Cursor c = null;
////        try {
////
////            StringBuilder query = new StringBuilder("select * from "
////                    + BaseSaleStockedProduct.TABLE_NAME + " where type='" + type.name() + "' AND ");
////
////            if (date > 0) {
////                query.append(" date = " + date + " AND ");
////            } else {
////                query.append(" date > 0 AND ");
////            }
////
////            query.append(BaseSaleStockedProduct.CLIENT_ID + " = " + client.getId());
////
//////			query.replace(query.length() - 4, query.length(), "");
////            Log.d(TAG, "QUERYYY: " + query.toString());
////            c = sql.rawQuery(query.toString(), null);
////
////            while (c.moveToNext()) {
////                BaseSaleStockedProduct item = null;
////                if (type.equals(BaseSaleStockedProduct.TYPE_PRODUCT.SALE)) {
////                    item = new SaleItem(c);
////                } else if (type.equals(BaseSaleStockedProduct.TYPE_PRODUCT.STOCKED)) {
////                    item = new StockedItem(c);
////                }
////                itemsPerDateAndClient.add(item);
////            }
////
////            //DEBUG
////            Iterator it = itemsPerDateAndClient.iterator();
////            while (it.hasNext()) {
////                Map.Entry pair = (Map.Entry) it.next();
////                Log.d(TAG, "***** Date = " + pair.getKey());
////                it.remove(); // avoids a ConcurrentModificationException
////                List<SaleItem> l = (List) pair.getValue();
////                for (SaleItem s : l) {
////                    Log.d(TAG, s.getQuantity() + " : " + s.getIdProduct() + " : " + s.getUnitPrice());
////                }
////            }
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        } finally {
////            if (c != null) {
////                c.close();
////            }
////        }
////        return itemsPerDateAndClient;
//    }
//
//    /**
//     * List by date and client, if date is null, get all date, if client is null
//     * get all client sales
//     *
//     * @param initialDate
//     * @param finalDate
//     * @param client
//     * @param sql
//     * @return
//     */
//    public HashMap<Long, List<BaseSaleStocked>> listSaleAndStockPerDateAndClient(
//            long initialDate, long finalDate, BaseSaleStocked.TYPE type,
//            Client client, SQLiteDatabase sql) {
////        HashMap<Long, List<BaseSaleStocked>> salesPerDateAndClient = new HashMap<>();
////        try {
////
////            StringBuilder query = new StringBuilder("SELECT * FROM "
////                    + SaleItem.TABLE_NAME + " WHERE ");
////
////            query.append(SaleItem.CLIENT_ID + " = " + client.getId() + " AND ");
////            query.append("date >= " + initialDate + " AND date <=" + finalDate);
////
////            if (type.equals(BaseSaleStocked.TYPE.SALE) ||
////                    type.equals(BaseSaleStocked.TYPE.STOCKED)) {
////                query.append(" AND type LIKE '" + type.name() + "'");
////            }
////
////
////            Log.d(TAG, "QUERYYY: " + query.toString());
////            Cursor c = sql.rawQuery(query.toString(), null);
////
////            while (c.moveToNext()) {
////                BaseSaleStocked.TYPE saleStockStype = BaseSaleStocked.TYPE.valueOf(c.getString(c
////                        .getColumnIndex(BaseSaleStocked.TYPE)));
////                BaseSaleStocked item = null;
////                if (saleStockStype.equals(BaseSaleStocked.TYPE.SALE)) {
////                    item = new SaleItem(c);
////                } else if (saleStockStype.equals(BaseSaleStocked.TYPE.STOCKED)) {
////                    item = new StockedItem(c);
////                }
////                //separate per date
////                List<BaseSaleStocked> list = salesPerDateAndClient.get(item.getDate());
////                if (list == null) {
////                    list = new ArrayList();
////                    salesPerDateAndClient.put(item.getDate(), list);
////                }
////                list.add(item);
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return salesPerDateAndClient;
//    return null;
//    }
//
//    public Product getProduct(long id) {
//
//        SQLiteDatabase db = getReadableDatabase();
//        try {
//            Cursor c = db.query(Product.TABLE_NAME, null, "_id = " + id, null, null, null, null);
//            if (c.getCount() > 0 && c.moveToFirst()) {
//                return new Product(c);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // db.close();
//        }
//        return null;
//    }
//
//    public Client getClient(long id) {
//
////        SQLiteDatabase db = getReadableDatabase();
////        try {
////            Cursor c = db.query(Client.TABLE_NAME, null,
////                    Client.ID + " = " + id, null, null, null, null);
////            if (c.getCount() > 0 && c.moveToFirst()) {
////                return new Client(c);
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        } finally {
////            // db.close();
////        }
//        return null;
//    }
//}
