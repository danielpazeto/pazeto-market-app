package com.pazeto.ceasapazeto.adapter;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.db.DBFacade;
import com.pazeto.ceasapazeto.vo.Product;
import com.pazeto.ceasapazeto.vo.StockedItem;
import com.pazeto.ceasapazeto.vo.SaleItem;

public class SaleListAdapter extends ArrayAdapter<SaleItem> implements
		TextWatcher {

	protected static final String LOG_TAG = SaleListAdapter.class
			.getSimpleName();

	private List<SaleItem> items;
	private int layoutResourceId;
	private Context context;
	DBFacade db;
	int j;
	Cursor prodyctsAvailables;
	String[] myListProductsDay;
	HashMap<Long, String> hmProductDay;
	boolean formatedEdit = true;

	private SQLiteDatabase sql;

	public boolean isFormatedEdit() {
		return formatedEdit;
	}

	public SaleListAdapter(Context context, int layoutResourceId,
			List<SaleItem> items, long date) {

		super(context, layoutResourceId, items);

		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
		db = new DBFacade(getContext());
		sql = db.getWritableDatabase();
		// Produtos disponiveis, ou seja, produtos com numero maior que zero no
		// estoque
		prodyctsAvailables = db.listProductsDayAvailables(sql);
		hmProductDay = new HashMap<Long, String>();
		j = 0;
		myListProductsDay = new String[prodyctsAvailables.getCount()];
		while (prodyctsAvailables.moveToNext()) {
			long idProd = prodyctsAvailables.getInt(prodyctsAvailables
					.getColumnIndex(StockedItem.PRODUCT_ID));
			Product prod = db.getProduct(idProd);
			StringBuilder name = new StringBuilder(prod.getName() + " "
					+ prod.getDescription());

			myListProductsDay[j] = name.toString();
			hmProductDay.put(idProd, name.toString());
			j++;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		SaleHolder holder = new SaleHolder();
		holder.sale = items.get(position);
		holder.removeSaleButton = (ImageButton) row
				.findViewById(R.id.sale_remove);
		holder.removeSaleButton.setTag(holder.sale);
		// TODO FAZENDO AUTOCOMPLETE TEXT VIEW

		ArrayAdapter<String> prodAdapter = new ArrayAdapter<String>(
				this.context, android.R.layout.simple_dropdown_item_1line,
				myListProductsDay);

		AutoCompleteTextView autoCompProduct = (AutoCompleteTextView) row
				.findViewById(R.id.sale_product);
		autoCompProduct.addTextChangedListener(this);
		autoCompProduct.setThreshold(0);
		autoCompProduct.setAdapter(prodAdapter);

		holder.prodName = (AutoCompleteTextView) row
				.findViewById(R.id.sale_product);
		setNameTextChangeListener(holder);
		TextView pos = (TextView) row.findViewById(R.id.pos);
		pos.setText(String.valueOf(position));
		
		holder.quantity = (EditText) row.findViewById(R.id.sale_quantity);
		setQuantityTextListeners(holder);
		holder.unitPrice = (EditText) row.findViewById(R.id.sale_price_unit);
		setUnitPriceTextListeners(holder);
		holder.totalPrice = (TextView) row.findViewById(R.id.sale_price_total);
		holder.isPaid = (CheckBox) row.findViewById(R.id.sale_ck_is_paid);
		setIsPaidCheckListener(holder);

		row.setTag(holder);
		setupItem(holder);
		return row;
	}

	private void setupItem(SaleHolder holder) {

		String prodName = hmProductDay.get(holder.sale.getIdProduct());
		holder.prodName.setText(prodName);
		holder.quantity.setText(String.valueOf(holder.sale.getQuantity()));
		holder.unitPrice.setText(String.valueOf(holder.sale.getUnitPrice()));
		// holder.totalPrice.setText((int)
		// (holder.sale.getUnit_price()*holder.sale.getQuantity()));
		holder.isPaid.setChecked(holder.sale.isPaid());
	}

	private class SaleHolder {
		TextView position;
		SaleItem sale;
		AutoCompleteTextView prodName;
		EditText quantity;
		EditText unitPrice;
		TextView totalPrice;
		CheckBox isPaid;
		ImageButton removeSaleButton;
	}

	private void setNameTextChangeListener(final SaleHolder holder) {
		holder.prodName.addTextChangedListener(new TextWatcher() {

			// @Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Pega o id apartir do nome
				if (getKey(s.toString()) != -1) {
					long id = getKey(s.toString());
					holder.sale.setIdProduct(id);
					formatedEdit = true;
				} else {
					formatedEdit = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

		});
	}

	private void setQuantityTextListeners(final SaleHolder holder) {
		holder.quantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					holder.sale.setQuantity(Double.parseDouble(s.toString()));
					if (holder.sale.getUnitPrice() != 0) {
						holder.sale.setTotalPrice(holder.sale.getQuantity()
								* holder.sale.getUnitPrice());
						holder.totalPrice.setText(""
								+ holder.sale.getTotalPrice());
					}
				} catch (NumberFormatException e) {
					Log.e(LOG_TAG,
							"error reading double value: " + s.toString());
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

		});
	}

	private void setUnitPriceTextListeners(final SaleHolder holder) {
		holder.unitPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					holder.sale.setUnitPrice(Double.parseDouble(s.toString()));
					if (holder.sale.getQuantity() != 0) {
						holder.sale.setTotalPrice(holder.sale.getQuantity()
								* holder.sale.getUnitPrice());
						holder.totalPrice.setText(""
								+ holder.sale.getTotalPrice());
					}
				} catch (NumberFormatException e) {
					Log.e(LOG_TAG,
							"error reading double value: " + s.toString());
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

		});
	}

	private void setIsPaidCheckListener(final SaleHolder holder) {
		holder.isPaid.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				try {
					holder.sale.setPaid(isChecked);
				} catch (Exception e) {
					Log.e(LOG_TAG, "CHeckBox is Paid?: " + isChecked
							+ " Erro: " + e);
				}
			}
		});

		holder.isPaid.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

		});
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	public long getKey(String name) {

		for (long key : hmProductDay.keySet()) {
			if (hmProductDay.get(key).equals(name))
				return key;
		}
		return -1;
	}

	public String getName(long id) {
		return hmProductDay.get(id);
	}

	@Override
	public void remove(SaleItem saleToRemove) {
		if (isFormatedEdit()) {
			db.removeSale(saleToRemove, sql);
			super.remove(saleToRemove);
		}
	}
}
