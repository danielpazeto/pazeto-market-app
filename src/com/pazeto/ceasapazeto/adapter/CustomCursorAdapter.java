package com.pazeto.ceasapazeto.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.vo.Client;
import com.pazeto.ceasapazeto.vo.Product;

public class CustomCursorAdapter extends CursorAdapter {

	final public static int PRODUCT = 1;
	final public static int CLIENT = 2;
	private int type;

	// PASSO COMO ARGUMENTO A LIST VIEW A SER USADA??
	public CustomCursorAdapter(Context context, Cursor c, int type) {
		super(context, c);
		this.type = type;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = null;
		switch (type) {
		case PRODUCT:
			retView = inflater.inflate(R.layout.product_list_item, parent,
					false);
			break;
		case CLIENT:
			retView = inflater
					.inflate(R.layout.client_list_item, parent, false);
			break;

		default:
			break;
		}
		return retView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		switch (type) {
		case PRODUCT:
			TextView tvProdName = (TextView) view.findViewById(R.id.tvProdName);
			TextView tvProdDesc = (TextView) view
					.findViewById(R.id.tvProdDescription);
			tvProdName.setText(cursor.getString(cursor
					.getColumnIndex(Product.NAME)));
			tvProdDesc.setText(cursor.getString(cursor
					.getColumnIndex(Product.DESCRIPTION)));
			break;
		case CLIENT:
			TextView tvClientName = (TextView) view
					.findViewById(R.id.tv_client_name);
			TextView tvClientCity = (TextView) view
					.findViewById(R.id.tv_client_city);
			TextView tvClientTelephone = (TextView) view
					.findViewById(R.id.tv_client_telephone);
			TextView tcClientCellphone1 = (TextView) view
					.findViewById(R.id.tv_client_cellphone1);
			tvClientName
					.setText(cursor.getString(cursor
							.getColumnIndex(Client.NAME))
							+ " "
							+ cursor.getString((cursor
									.getColumnIndex(Client.LASTNAME))));
			tvClientCity.setText(cursor.getString(cursor
					.getColumnIndex(Client.CITY)));

			tvClientTelephone.setText(cursor.getString(cursor
					.getColumnIndex(Client.TELEPHONE)));

			tcClientCellphone1.setText(cursor.getString(cursor
					.getColumnIndex(Client.PHONE1)));

			break;

		default:

		}
	}

}
