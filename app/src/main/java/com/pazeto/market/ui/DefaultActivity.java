package com.pazeto.market.ui;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.pazeto.market.db.DBFacade;

public class DefaultActivity extends Activity {

	DBFacade db;
	SQLiteDatabase sql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		db = new DBFacade(this);
		sql = db.getWritableDatabase();

		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
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

}
