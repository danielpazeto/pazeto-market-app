package com.pazeto.market.ui;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pazeto.market.R;
import com.pazeto.market.adapter.ProductStockListAdapter;
import com.pazeto.market.db.DBFacade;
import com.pazeto.market.vo.StockedItem;
import com.pazeto.market.widgets.Utils;

public class ListProductStock extends Activity {
	private TextView tvDate;
	private Button changeDate;
	static final int DATE_PICKER_ID = 451654;
	private int year;
	private int month;
	private int day;
	private String date;
	private ProductStockListAdapter adapter;
	DBFacade db;

	CalendarView calendar;
	private long unixDate;
	boolean saved = true;
	private SQLiteDatabase sql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productday_list_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		db = new DBFacade(this);
		sql = db.getWritableDatabase();
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		changeDate = (Button) findViewById(R.id.btn_change_date);
		tvDate = (TextView) findViewById(R.id.tvDate);
		if (unixDate <= 0) {
			showDialog(DATE_PICKER_ID);
		}
		changeDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// if (adapter.isFormatedEdit()) {
				showDialog(DATE_PICKER_ID);
				// }
			}

		});

		setupAddProductDayButton();
	}

	private void setupListViewAdapter(ArrayList<StockedItem> items) {
		adapter = new ProductStockListAdapter(this,
				R.layout.add_productday_list_item, items, db);
		ListView productDaysListView = (ListView) findViewById(R.id.product_day_list_view);
		productDaysListView.setEmptyView(findViewById(android.R.id.empty));
		productDaysListView.setAdapter(adapter);
		adapter.setSaveListener(saveListener);
	}

	private void setupAddProductDayButton() {
		findViewById(R.id.btn_add_product_day).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// if (adapter.isFormatedEdit()) {
						Log.d("data do add: ", "" + unixDate);
						adapter.insert(new StockedItem(0, 0, unixDate), 0);
					}
					// }
				});
	}

	private void loadProductDayPerDate(long date) {
		Cursor cursorProductsDay = db.listStockProductsInDate(date, sql);
		ArrayList<StockedItem> prodsPerDate = new ArrayList<StockedItem>();
		while (cursorProductsDay.moveToNext()) {
			prodsPerDate.add(new StockedItem(cursorProductsDay));
		}
		if (cursorProductsDay != null) {
			cursorProductsDay.close();
		}
		setupListViewAdapter(prodsPerDate);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			final DatePickerDialog datePickDialog = new DatePickerDialog(this,
					datePickerListener, year, month, day);
			datePickDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					"Cancelar", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							datePickDialog.dismiss();
							finish();
						}
					});
			return datePickDialog;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// Show selected date
			tvDate.setText(new StringBuilder().append(month + 1).append("/")
					.append(day).append("/").append(year).append(" "));

			date = new StringBuilder().append(year).append("-")
					.append(month + 1).append("-").append(day).toString();

			unixDate = Utils.componentTimeToTimestamp(year, month, day);
			loadProductDayPerDate(unixDate);
		}
	};

	private boolean saveStockProducts() {
		try {
			int savedIdsCounter = 0;
			System.out.println("ITEMS: " + adapter.getCount());
			for (int i = 0; i < adapter.getCount(); i++) {
				StockedItem prodDay = adapter.getItem(i);

				System.out.println("ID: " + prodDay.getId() + " Quant.: "
						+ prodDay.getQuantity() + " Prod id: "
						+ prodDay.getIdProduct() + " Date: "
						+ prodDay.getDate() + " e valor : "
						+ prodDay.getUnitPrice());

				long id = db.insertProductStock(prodDay, sql);
				if (id != -1 && id > 0) {
					System.out.println("salvou com novo id: " + id);
					adapter.getItem(i).setId(id);
				}
				savedIdsCounter++;
			}
			Toast.makeText(getApplicationContext(),
					"Salvou " + savedIdsCounter + " Item(s).",
					Toast.LENGTH_SHORT).show();
			return true;
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Erro ao salvar itens",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onBackPressed() {
		saveListener.save();
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_stock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_stock:
			System.out.println("AWUEHIAWHUUEHIEHUAUE");
			saveListener.save();
			return true;
		case android.R.id.home:
			saveListener.save();
			this.finish();
			return true;
		case R.id.new_client:
			startActivityForResult(new Intent(ListProductStock.this,
					ClientActivity.class), 1);
			return true;
		case R.id.new_product:
			startActivityForResult(new Intent(ListProductStock.this,
					ProductActivity.class), 1);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("Atualizando produtos e clientes");
		if (adapter != null) {
			adapter.refreshProductAndClientList();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	SaveStockListener saveListener = new SaveStockListener() {

		@Override
		public boolean save() {
			return saveStockProducts();

		}
	};

	public interface SaveStockListener {
		public boolean save();
	}
}
