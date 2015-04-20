package com.pazeto.ceasapazeto.adapter;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.db.DBFacade;
import com.pazeto.ceasapazeto.ui.ListProductStock.saveStock;
import com.pazeto.ceasapazeto.vo.Client;
import com.pazeto.ceasapazeto.vo.Product;
import com.pazeto.ceasapazeto.vo.ProductStock;

public class ProductStockListAdapter extends ArrayAdapter<ProductStock> {

	protected static final String LOG_TAG = ProductStockListAdapter.class
			.getSimpleName();

	private List<ProductStock> items;
	private int layoutResourceId;
	private Context context;
	DBFacade db;
	int j, k;
	Cursor products;
	String[] myListProducts;
	HashMap<Long, String> hmProducts;
	Cursor clients;
	String[] myListClients;
	HashMap<Long, String> hmClients;
	boolean formatedEdit = true;

	private saveStock listenerSave;

	// public boolean isFormatedEdit() {
	// if (!formatedEdit) {
	// Toast.makeText(context, "Peencha o campo Produto corretamente.",
	// Toast.LENGTH_SHORT).show();
	// }
	// return formatedEdit;
	// }

	public ProductStockListAdapter(Context context, int layoutResourceId,
			List<ProductStock> items) {

		super(context, layoutResourceId, items);

		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
		db = new DBFacade(getContext());
		// carrega lista de produtos
		refreshProductAndClientList();
	}

	/**
	 * Atualiza a lista de produtos que sera mostrada no autocomplete
	 */
	public void refreshProductAndClientList() {
		products = db.listProducts();
		hmProducts = new HashMap<Long, String>();
		j = 0;
		myListProducts = new String[products.getCount()];
		while (products.moveToNext()) {
			long idProd = products.getInt(products.getColumnIndex(Product.ID));
			StringBuilder name = new StringBuilder(products.getString(products
					.getColumnIndex(Product.NAME))).append(" ").append(
					products.getString(products
							.getColumnIndex(Product.DESCRIPTION)));
			myListProducts[j] = name.toString();
			hmProducts.put(idProd, name.toString());
			j++;
		}
		// setup clients
		clients = db.listClients();
		hmClients = new HashMap<Long, String>();
		k = 0;
		myListClients = new String[clients.getCount()];
		while (clients.moveToNext()) {
			long idClient = clients.getInt(clients.getColumnIndex(Client.ID));
			StringBuilder name = new StringBuilder(clients.getString(clients
					.getColumnIndex(Client.NAME))).append(" ").append(
					clients.getString(clients.getColumnIndex(Client.LASTNAME)));
			myListClients[k] = name.toString();
			hmClients.put(idClient, name.toString());
			k++;
		}
		if (mAdapterClient != null) {
			mAdapterClient.notifyDataSetChanged();
		}
		if (mAdapterProd != null) {
			mAdapterProd.notifyDataSetChanged();
		}
		this.notifyDataSetChanged();
	}

	ArrayAdapter<String> mAdapterClient;
	ArrayAdapter<String> mAdapterProd;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ProductDayHolder holder = null;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new ProductDayHolder();
		holder.productDay = items.get(position);
		holder.removeProductDayButton = (ImageButton) row
				.findViewById(R.id.ProductDay_remove);
		holder.removeProductDayButton.setTag(holder.productDay);

		// AUTO COMPLETE PRODUCT
		mAdapterProd = new ArrayAdapter<String>(this.context,
				android.R.layout.simple_dropdown_item_1line, myListProducts);
		AutoCompleteTextView autoCompProduct = (AutoCompleteTextView) row
				.findViewById(R.id.ProductDay_name);
		autoCompProduct.setThreshold(0);
		autoCompProduct.setAdapter(mAdapterProd);
		// AUTO COMPLETE Client
		mAdapterClient = new ArrayAdapter<String>(this.context,
				android.R.layout.simple_dropdown_item_1line, myListClients);
		AutoCompleteTextView autoCompClient = (AutoCompleteTextView) row
				.findViewById(R.id.ProductDay_client);
		autoCompClient.setThreshold(0);
		autoCompClient.setAdapter(mAdapterClient);

		holder.prod_name = autoCompProduct;
		holder.client_name = autoCompClient;
		holder.quantity = (TextView) row.findViewById(R.id.ProductDay_quantity);
		holder.unit_price = (TextView) row
				.findViewById(R.id.ProductDay_unit_price);
		setListnerOnFields(holder);

		row.setTag(holder);
		setupItem(holder);
		return row;
	}

	private void setupItem(ProductDayHolder holder) {
		String name = hmProducts.get(holder.productDay.getIdProduct());
		String client_name = hmClients.get(holder.productDay.getIdClient());
		holder.prod_name.setText(name);
		holder.client_name.setText(client_name);
		holder.quantity
				.setText(String.valueOf(holder.productDay.getQuantity()));
		holder.unit_price.setText(String.valueOf(holder.productDay
				.getUnitPrice()));
	}

	public static class ProductDayHolder {
		ProductStock productDay;
		AutoCompleteTextView prod_name;
		AutoCompleteTextView client_name;
		TextView quantity;
		TextView unit_price;
		ImageButton removeProductDayButton;
	}

	private void setListnerOnFields(final ProductDayHolder holder) {
		holder.prod_name.addTextChangedListener(new TextWatcher() {

			// @Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Pega o id apartir do nome
				if (getKey(hmProducts, s.toString()) != -1) {
					long id = getKey(hmProducts, s.toString());
					holder.productDay.setIdProduct(id);
					System.out.println("Setou id " + id + "  e  name " + s);
					formatedEdit = true;
//					listenerSave.save();
				} else if (hmProducts.size() > 0) {
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
		holder.client_name.addTextChangedListener(new TextWatcher() {

			// @Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Pega o id apartir do nome
				if (getKey(hmClients, s.toString()) != -1) {
					long id = getKey(hmClients, s.toString());
					holder.productDay.setIdClient(id);
					System.out.println("Setou id " + id + "  e  name " + s);
					formatedEdit = true;
//					listenerSave.save();
				} else if (hmProducts.size() > 0) {
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
		holder.quantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					holder.productDay.setQuantity(Double.parseDouble(s
							.toString()));
//					listenerSave.save();
				} catch (NumberFormatException e) {
					Log.e(LOG_TAG,
							"error reading double value: " + s.toString());
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

		holder.unit_price.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					holder.productDay.setUnitPrice(Double.parseDouble(s
							.toString()));
//					listenerSave.save();
				} catch (NumberFormatException e) {
					Log.e(LOG_TAG,
							"error reading double value: " + s.toString());
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

	@Override
	public void remove(ProductStock object) {
		// if (isFormatedEdit()) {
		db.removeProductDay(object);
		super.remove(object);

		// }

	}

	public long getKey(HashMap<Long, String> hm, String name) {
//		System.out.println(name);
		for (long key : hm.keySet()) {
//			System.out.println(hm.get(key));
			if (hm.get(key).equals(name))
				return key;
		}
		return -1;
	}

	public void setSaveListener(saveStock saveListener) {
		listenerSave = saveListener;
	}

}
