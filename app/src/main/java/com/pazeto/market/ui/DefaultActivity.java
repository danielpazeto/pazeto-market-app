package com.pazeto.market.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.pazeto.market.db.DBFacade;

public class DefaultActivity extends AppCompatActivity {

    DBFacade db;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBFacade(this);
        sql = db.getWritableDatabase();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        if (sql != null) {
            sql.close();
        }
        if (db != null) {
            db.close();
        }
        super.onDestroy();
    }

    public void onNewClientBtn(View v) {
        startActivityForResult(new Intent(DefaultActivity.this, EditClientActivity.class), 1);
    }

    public void onNewStockedProductBtn(View v) {
        startActivityForResult(new Intent(DefaultActivity.this, ListProductStockActivity.class),
                1);
    }

    public void onNewProductBtn(View v) {
        startActivityForResult(new Intent(DefaultActivity.this, EditProductActivity.class),
                1);
    }

}
