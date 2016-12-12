package com.pazeto.market.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pazeto.market.R;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.Product;

public class CustomCursorAdapter extends CursorAdapter {

    final public static int PRODUCT = 1;
    final public static int CLIENT = 2;
    private int type;

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
        }
        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        switch (type) {
            case PRODUCT:
                Product prod = new Product(cursor);
                TextView tvProdName = (TextView) view.findViewById(R.id.tvProdName);
                TextView tvProdDesc = (TextView) view
                        .findViewById(R.id.tvProdDescription);
                tvProdName.setText(prod.getName());
                tvProdDesc.setText(prod.getDescription());
                view.setTag(prod);
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
                Client client = new Client(cursor);
                tvClientName
                        .setText(client.getName()
                                + " "
                                + client.getLastname());
                tvClientCity.setText(client.getCity());
                tvClientTelephone.setText(client.getTelephone());
                tcClientCellphone1.setText(client.getCellPhone1());
                view.setTag(client);
                break;

            default:

        }
    }

}
