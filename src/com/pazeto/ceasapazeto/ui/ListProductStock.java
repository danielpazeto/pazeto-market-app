package com.pazeto.ceasapazeto.ui;

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

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.adapter.ProductStockListAdapter;
import com.pazeto.ceasapazeto.db.DBFacade;
import com.pazeto.ceasapazeto.vo.ProductStock;
import com.pazeto.ceasapazeto.widgets.Utils;

public class ListProductStock extends Activity {
	private TextView Output;
	private Button changeDate;
	static final int DATE_PICKER_ID = 1111;
	private int year;
	private int month;
	private int day;
	private String date;
	private ProductStockListAdapter adapter;
	DBFacade db;
	Cursor cursorProductsDay;
	CalendarView calendar;
	private long unixDate;
	boolean saved = true;
	private SQLiteDatabase sql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productday_list_view);
		db = new DBFacade(this);
		sql = db.getWritableDatabase();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		changeDate = (Button) findViewById(R.id.changeDate);
		Output = (TextView) findViewById(R.id.Output);
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

	/**
	 * Remove o produto do esqtoque
	 * 
	 * @param v
	 */
	public void removeProductDayOnClickHandler(View v) {
		ProductStock itemToRemove = (ProductStock) v.getTag();
		adapter.remove(itemToRemove);

	}

	private void setupListViewAdapter(ArrayList<ProductStock> items) {

		adapter = new ProductStockListAdapter(this,
				R.layout.add_productday_list_item, items);
		ListView productDaysListView = (ListView) findViewById(R.id.ProductDay_ListView);
		productDaysListView.setAdapter(adapter);
		adapter.setSaveListener(saveListener);
	}

	private void setupAddProductDayButton() {
		findViewById(R.id.AddProductDay).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// if (adapter.isFormatedEdit()) {
						Log.d("data do add: ", "" + unixDate);
						adapter.insert(new ProductStock(0, 0, unixDate), 0);
					}
					// }
				});
	}

	private void loadProductDayPerDate(long date) {
		cursorProductsDay = db.listProductsDayPerDate(date, sql);
		ArrayList<ProductStock> prodsPerDate = new ArrayList<ProductStock>();
		while (cursorProductsDay.moveToNext()) {
			long idProd = cursorProductsDay.getInt(cursorProductsDay
					.getColumnIndex(ProductStock.PRODUCT_ID));
			long idClient = cursorProductsDay.getInt(cursorProductsDay
					.getColumnIndex(ProductStock.CLIENT_ID));
			long id = cursorProductsDay.getInt(cursorProductsDay
					.getColumnIndex(ProductStock.ID));
			long dateProd = cursorProductsDay.getLong(cursorProductsDay
					.getColumnIndex(ProductStock.DATE));
			double quantity = cursorProductsDay.getInt(cursorProductsDay
					.getColumnIndex(ProductStock.QUANTITY));
			double unitPrice = cursorProductsDay.getInt(cursorProductsDay
					.getColumnIndex(ProductStock.UNIT_PRICE));
			ProductStock prodDay = new ProductStock(id, idProd, quantity,
					idClient, unitPrice, dateProd);
			prodsPerDate.add(prodDay);
		}
		setupListViewAdapter(prodsPerDate);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:

			// open datepicker dialog.
			// set date picker for current date
			// add pickerListener listner to date picker
			final DatePickerDialog datePickDialog = new DatePickerDialog(this,
					pickerListener, year, month, day);
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

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// Show selected date
			Output.setText(new StringBuilder().append(month + 1).append("/")
					.append(day).append("/").append(year).append(" "));

			date = new StringBuilder().append(year).append("-")
					.append(month + 1).append("-").append(day).toString();

			unixDate = Utils.componentTimeToTimestamp(year, month, day);
			loadProductDayPerDate(unixDate);
		}
	};

	private boolean saveStockProducts() {
		try {
			int savedIds = 0;
			System.out.println("ITEMS: " + adapter.getCount());
			for (int i = 0; i < adapter.getCount(); i++) {
				ProductStock prodDay = adapter.getItem(i);

				System.out.println("ID: " + prodDay.getId() + " Quant.: "
						+ prodDay.getQuantity() + " Prod id: "
						+ prodDay.getIdProduct() + " Date: "
						+ prodDay.getDate());

				long id;
				id = db.insertProductStock(prodDay, sql);
				if (id != -1) {
					System.out.println("salvou com id: " + id);
					adapter.getItem(i).setId(id);
					savedIds++;
				}
			}
			Toast.makeText(getApplicationContext(),
					"Salvou " + savedIds + " Item(s).", Toast.LENGTH_SHORT)
					.show();
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
		case android.R.id.home:
			this.finish();
			return true;

		case R.id.new_client:
			startActivityForResult(new Intent(ListProductStock.this,
					AddClient.class), 1);
			return true;

		case R.id.new_product:
			startActivityForResult(new Intent(ListProductStock.this,
					AddProduct.class), 1);
			return true;
		case R.id.save:
			saveListener.save();
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

	saveStock saveListener = new saveStock() {

		@Override
		public boolean save() {
			return saveStockProducts();

		}
	};

	public interface saveStock {
		public boolean save();
	}
}
