package com.pazeto.market.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pazeto.market.R;
import com.pazeto.market.adapter.SaleListAdapter;
import com.pazeto.market.db.DBFacade;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.SaleItem;
import com.pazeto.market.widgets.Utils;

public class SaleActivity extends Activity implements TextWatcher {
	private TextView tvCurrentDate;
	private Button changeDate, btSave;
	static final int DATE_PICKER_ID = 1111;
	private static final String TAG = SaleActivity.class.getName();
	private int year;
	private int month;
	private int day;
	private String date;
	private SaleListAdapter adapter;
	DBFacade db;
	SQLiteDatabase sql;
	Cursor cursorSale;
	CalendarView calendar;
	HashMap<Long, String> hmClients;
	Client currentClient;
	private long unixDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sale_list_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		db = new DBFacade(this);
		sql = db.getWritableDatabase();
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		changeDate = (Button) findViewById(R.id.changeDate);
		btSave = (Button) findViewById(R.id.btSave);
		tvCurrentDate = (TextView) findViewById(R.id.OutputDate);
		
		
		setupAutoCompleteClient();
		// showDialog(DATE_PICKER_ID);

		changeDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// On button click show datepicker dialog
				showDialog(DATE_PICKER_ID);
			}

		});
		btSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveSales();

			}
		});

		setupAddSaleButton();

		// loadSalePerDateAndClient(0, null);
	}

	/**
	 * Remove a venda
	 * 
	 * @param v
	 */
	public void removeSaleOnClickHandler(View v) {
		final SaleItem itemToRemove = (SaleItem) v.getTag();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Deseja remover o item nº "+ v.getId()+ ":\n" + itemToRemove.getQuantity() + " - "
						+ adapter.getName(itemToRemove.getIdProduct()) + " - "
						+ itemToRemove.getUnitPrice())
				.setPositiveButton("Remover",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								adapter.remove(itemToRemove);
							}
						})
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
							}
						});
		builder.create().show();

	}

	private void setupListViewAdapter(ArrayList<SaleItem> items) {
		adapter = new SaleListAdapter(this, R.layout.add_sale_list_item, items,
				unixDate);
		ListView salesListView = (ListView) findViewById(R.id.sale_ListView);
		salesListView.setAdapter(adapter);
	}

	private void setupAddSaleButton() {
		findViewById(R.id.addSale).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isConfigured() && unixDate > 0) {
					adapter.insert(new SaleItem(0, 0, unixDate), 0);
				} else {
					Toast.makeText(getApplicationContext(),
							"Cliente ou Data inválidos.", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	private void loadSalePerDateAndClient(long date, Client client) {
		ArrayList<SaleItem> salesPerDateAndClient = db.listSalePerDateAndClient(
				date, client, sql);
		if (salesPerDateAndClient.size() == 0) {
			Toast.makeText(getApplicationContext(),
					"Não há itens para a data: " + date, Toast.LENGTH_SHORT)
					.show();
		}
		Log.d(TAG, salesPerDateAndClient.size()
				+ " resultados para o cliente:  " + client);
		setupListViewAdapter(salesPerDateAndClient);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			saveSales();
			final DatePickerDialog datePickDialog = new DatePickerDialog(this,
					pickerListener, year, month, day);
			datePickDialog.setTitle("Escolha a data:");
			datePickDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					"Cancelar", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							datePickDialog.dismiss();
							// finish();
						}
					});
			return datePickDialog;
		}
		return null;
	}

	// private OnDateSetListener pickerListener = new OnDateSetListener() {
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// Show selected date
			tvCurrentDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

			date = new StringBuilder().append(year).append("-")
					.append(month + 1).append("-").append(day).toString();
			unixDate = Utils.componentTimeToTimestamp(year, month, day);
			if (isConfigured()) {
				loadSalePerDateAndClient(unixDate, currentClient);
			}
		}

	};

	private void saveSales() {
		if (adapter != null) {
			for (int i = 0; i < adapter.getCount(); i++) {
				SaleItem saleItem = adapter.getItem(i);
				saleItem.setDate(unixDate);
				saleItem.setIdClient(currentClient.getId());
				long id = db.insertSale(saleItem, sql);
				if (id != -1) {
					System.out.println("salvou com id: " + id);
					System.out.println("e data" + saleItem.getDate());
					System.out.println("e id client" + saleItem.getIdClient());
					adapter.getItem(i).setId(id);
				}
			}
		}

	}

	@Override
	public void onBackPressed() {
		saveSales();
		super.onBackPressed();
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		System.out.println("Cliente autocomplete: " + s.toString());
		if (getKey(s.toString()) != -1) {
			long id = getKey(s.toString());
			currentClient = new Client();
			currentClient.setId(id);
			currentClient.setName(s.toString());
			// holder.productDay.setIdProduct(id);
			System.out.println("Setou id " + id + "  e  name " + s);
			if (isConfigured()) {
				loadSalePerDateAndClient(unixDate, currentClient);
			}
		} else {
			currentClient = null;
		}
	}

	/**
	 * Inicializa o autocomplete do cliente
	 */
	public void setupAutoCompleteClient() {
		int j = 0;
		hmClients = new HashMap<Long, String>();
		Cursor clients = db.listClients();
		String[] MyListClients = new String[clients.getCount()];
		while (clients.moveToNext()) {
			long idclient = clients.getInt(clients.getColumnIndex(Client.ID));
			String name = clients
					.getString(clients.getColumnIndex(Client.NAME));
			MyListClients[j] = name;
			hmClients.put(idclient, name);
			j++;
		}

		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.dropdown_list_custom,
				MyListClients);

		AutoCompleteTextView autoCompProduct = (AutoCompleteTextView) findViewById(R.id.OutputClient);

		autoCompProduct.addTextChangedListener(this);
		autoCompProduct.setThreshold(0);
		autoCompProduct.setAdapter(mAdapter);

	}

	public long getKey(String name) {

		if (hmClients != null) {
			for (long key : hmClients.keySet()) {
				if (hmClients.get(key).equals(name))
					return key;
			}
		}
		return -1;
	}

	/**
	 * Verify if client and date are selected
	 * 
	 * @return true if configured client and date
	 */
	public boolean isConfigured() {
		if (currentClient == null) {
			return false;
			// } else if (date == null) {
			// return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_sale, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			saveSales();
			this.finish();
			return true;
		case R.id.new_client:
			startActivityForResult(new Intent(SaleActivity.this, ClientActivity.class), 1);
			return true;

		case R.id.new_product_day:
			startActivityForResult(new Intent(SaleActivity.this, ListProductStock.class),
					1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		saveSales();
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
		setupAutoCompleteClient();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
