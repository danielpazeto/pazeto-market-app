package com.pazeto.market.adapter;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pazeto.market.R;
import com.pazeto.market.db.DBFacade;
import com.pazeto.market.ui.ListProductStockActivity.SaveStockListener;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.Product;
import com.pazeto.market.vo.StockedItem;

public class ProductStockListAdapter extends ArrayAdapter<StockedItem> {

	protected static final String LOG_TAG = ProductStockListAdapter.class
			.getSimpleName();

	private List<StockedItem> items;
	private int layoutResourceId;
	private Context context;
	DBFacade db;
	String[] myListProducts;
	HashMap<Long, String> hmProducts;
	Cursor clients;
	String[] myListClients;
	HashMap<Long, String> hmClients;
	boolean formatedEdit = true;

	private SaveStockListener listenerSave;

	// public boolean isFormatedEdit() {
	// if (!formatedEdit) {
	// Toast.makeText(context, "Peencha o campo Produto corretamente.",
	// Toast.LENGTH_SHORT).show();
	// }
	// return formatedEdit;
	// }

	public ProductStockListAdapter(Context context, int layoutResourceId,
			List<StockedItem> items, DBFacade db) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
		this.db = db;
		// carrega lista de produtos
		refreshProductAndClientList();
	}

	/**
	 * Atualiza a lista de produtos que sera mostrada no autocomplete
	 */
	public void refreshProductAndClientList() {
		Cursor cursorProducts = db.listProducts();
		hmProducts = new HashMap<>();
		int productItemIdex = 0;
		myListProducts = new String[cursorProducts.getCount()];
		while (cursorProducts.moveToNext()) {
			long idProd = cursorProducts.getInt(cursorProducts
					.getColumnIndex(Product.ID));
			StringBuilder name = new StringBuilder(
					cursorProducts.getString(cursorProducts
							.getColumnIndex(Product.NAME))).append(" ").append(
					cursorProducts.getString(cursorProducts
							.getColumnIndex(Product.DESCRIPTION)));
			myListProducts[productItemIdex] = name.toString();
			hmProducts.put(idProd, name.toString());
			productItemIdex++;
		}
		// setup clients
		clients = db.listClients();
		hmClients = new HashMap<>();
		int clientItemIndex = 0;
		myListClients = new String[clients.getCount()];
		while (clients.moveToNext()) {
			long idClient = clients.getInt(clients.getColumnIndex(Client.ID));
			StringBuilder name = new StringBuilder(clients.getString(clients
					.getColumnIndex(Client.NAME))).append(" ").append(
					clients.getString(clients.getColumnIndex(Client.LASTNAME)));
			myListClients[clientItemIndex] = name.toString();
			hmClients.put(idClient, name.toString());
			clientItemIndex++;
		}
		if (mAdapterClient != null) {
			mAdapterClient.notifyDataSetChanged();
		}
		if (mAdapterProd != null) {
			mAdapterProd.notifyDataSetChanged();
		}
		if (cursorProducts != null) {
			cursorProducts.close();
		}
		this.notifyDataSetChanged();
	}

	ArrayAdapter<String> mAdapterClient;
	ArrayAdapter<String> mAdapterProd;

	private OnClickListener listenerRemoveProductStock = new OnClickListener() {

		@Override
		public void onClick(View v) {
			StockedItem itemToRemove = (StockedItem) v.getTag();
			ProductStockListAdapter.this.remove(itemToRemove);
		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StockProductHolder holder = null;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new StockProductHolder();
		holder.stockProduct = items.get(position);
		holder.btnRemoveStockProduct = (ImageButton) row
				.findViewById(R.id.ProductDay_remove);
		holder.btnRemoveStockProduct
				.setOnClickListener(listenerRemoveProductStock);
		holder.btnRemoveStockProduct.setTag(holder.stockProduct);

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

		holder.edtStockProdName = autoCompProduct;
		holder.edtClientName = autoCompClient;
		holder.tvQuantity = (TextView) row
				.findViewById(R.id.ProductDay_quantity);
		holder.tvUnitPrice = (TextView) row
				.findViewById(R.id.ProductDay_unit_price);
		setListenerOnFields(holder);

		row.setTag(holder);
		setupItem(holder);
		return row;
	}

	private void setupItem(StockProductHolder holder) {
		String productName = hmProducts.get(holder.stockProduct.getIdProduct());
		String clientName = hmClients.get(holder.stockProduct.getIdClient());
		holder.edtStockProdName.setText(productName);
		holder.edtClientName.setText(clientName);
		holder.tvQuantity.setText(String.valueOf(holder.stockProduct
				.getQuantity()));
		System.out.println("VALOR " + holder.stockProduct.getUnitPrice());
		holder.tvUnitPrice.setText(String.valueOf(holder.stockProduct
				.getUnitPrice()));
	}

	public static class StockProductHolder {
		StockedItem stockProduct;
		AutoCompleteTextView edtStockProdName;
		AutoCompleteTextView edtClientName;
		TextView tvQuantity;
		TextView tvUnitPrice;
		ImageButton btnRemoveStockProduct;
	}

	private void setListenerOnFields(final StockProductHolder holder) {
		holder.edtStockProdName.addTextChangedListener(new TextWatcher() {

			// @Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Pega o id apartir do nome
				if (getKey(hmProducts, s.toString()) != -1) {
					long id = getKey(hmProducts, s.toString());
					holder.stockProduct.setIdProduct(id);
					System.out.println("Setou id " + id + "  e  name " + s);
					formatedEdit = true;
					// listenerSave.save();
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
		holder.edtClientName.addTextChangedListener(new TextWatcher() {

			// @Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Pega o id apartir do nome
				if (getKey(hmClients, s.toString()) != -1) {
					long id = getKey(hmClients, s.toString());
					holder.stockProduct.setIdClient(id);
					System.out.println("Setou id " + id + "  e  name " + s);
					formatedEdit = true;
					// listenerSave.save();
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
		holder.tvQuantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					holder.stockProduct.setQuantity(Double.parseDouble(s
							.toString()));
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

		holder.tvUnitPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					holder.stockProduct.setUnitPrice(Double.parseDouble(s
							.toString()));
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
	public void remove(StockedItem object) {
		db.removeProductDay(object);
		super.remove(object);
	}

	public long getKey(HashMap<Long, String> hm, String name) {
		for (long key : hm.keySet()) {
			if (hm.get(key).equals(name))
				return key;
		}
		return -1;
	}

	public void setSaveListener(SaveStockListener saveListener) {
		listenerSave = saveListener;
	}

}
