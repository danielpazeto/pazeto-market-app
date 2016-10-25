package com.pazeto.market.adapter;

import android.app.Activity;
import android.content.Context;
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

import com.pazeto.market.R;
import com.pazeto.market.db.DBFacade;
import com.pazeto.market.vo.BaseStockedProduct;
import com.pazeto.market.widgets.Utils;

import java.util.List;

public class SaleStockedListAdapter extends ArrayAdapter<BaseStockedProduct> {

    protected static final String LOG_TAG = SaleStockedListAdapter.class
            .getSimpleName();

    private List<BaseStockedProduct> items;
    private Context context;
    DBFacade db;
    Utils.ProductHashMap hmProducts;
    boolean formatedEdit = true;

    private SQLiteDatabase sql;

    public boolean isFormatedEdit() {
        return formatedEdit;
    }

    public SaleStockedListAdapter(Context context,
                                  List<BaseStockedProduct> items, long date) {

        super(context, R.layout.add_sale_list_item, items);

        this.context = context;
        this.items = items;
        db = new DBFacade(getContext());
        sql = db.getWritableDatabase();
        // Produtos disponiveis, ou seja, produtos com numero maior que zero no
        // estoque
        hmProducts = new Utils.ProductHashMap(db);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SaleStockHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.add_sale_list_item, parent, false);
            holder = new SaleStockHolder();
            holder.sale = items.get(position);

            setupProductNameText(holder, convertView);

            TextView pos = (TextView) convertView.findViewById(R.id.pos);
            pos.setText(String.valueOf(position));

            holder.quantity = (EditText) convertView.findViewById(R.id.sale_quantity);
            setQuantityTextListeners(holder);
            holder.unitPrice = (EditText) convertView.findViewById(R.id.sale_price_unit);
            setUnitPriceTextListeners(holder);
            holder.totalPrice = (TextView) convertView.findViewById(R.id.sale_price_total);
            holder.isPaid = (CheckBox) convertView.findViewById(R.id.sale_ck_is_paid);
            setIsPaidCheckListener(holder);

            holder.removeSaleButton = (ImageButton) convertView
                    .findViewById(R.id.sale_remove);
            holder.removeSaleButton.setTag(holder.sale);

            convertView.setTag(holder);
        } else {
            holder = (SaleStockHolder) convertView.getTag();
        }
        setupItem(holder);
        return convertView;
    }

    private void setupItem(SaleStockHolder holder) {

        String prodName = hmProducts.get(holder.sale.getIdProduct());
        holder.prodName.setText(prodName);
        holder.quantity.setText(String.valueOf(holder.sale.getQuantity()));
        holder.unitPrice.setText(String.valueOf(holder.sale.getUnitPrice()));
        // holder.totalPrice.setText((int)
        // (holder.sale.getUnit_price()*holder.sale.getQuantity()));
        holder.isPaid.setChecked(holder.sale.isPaid());
    }

    private class SaleStockHolder {
        TextView position;
        BaseStockedProduct sale;
        AutoCompleteTextView prodName;
        EditText quantity;
        EditText unitPrice;
        TextView totalPrice;
        CheckBox isPaid;
        ImageButton removeSaleButton;
    }

    private void setupProductNameText(final SaleStockHolder holder, View convertView) {
        ArrayAdapter<String> prodAdapter = new ArrayAdapter<>(
                this.context, android.R.layout.simple_dropdown_item_1line,
                hmProducts.getProductNames());

        AutoCompleteTextView autoCompProduct = (AutoCompleteTextView) convertView
                .findViewById(R.id.sale_product);
        autoCompProduct.setThreshold(0);
        autoCompProduct.setAdapter(prodAdapter);
        holder.prodName = autoCompProduct;
        holder.prodName.addTextChangedListener(new TextWatcher() {

            // @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // Pega o id apartir do nome
                long id = hmProducts.getIdByName(s.toString());
                if (id != -1) {
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

    private void setQuantityTextListeners(final SaleStockHolder holder) {
        holder.quantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    holder.sale.setQuantity(Double.parseDouble(s.toString()));
                    if (holder.sale.getUnitPrice() != 0) {
                        holder.sale.setTotalPrice(holder.sale.getQuantity()
                                * holder.sale.getUnitPrice());
                        holder.totalPrice.setText(String.valueOf(holder.sale.getTotalPrice()));
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

    private void setUnitPriceTextListeners(final SaleStockHolder holder) {
        holder.unitPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    holder.sale.setUnitPrice(Double.parseDouble(s.toString()));
                    if (holder.sale.getQuantity() != 0) {
                        holder.sale.setTotalPrice(holder.sale.getQuantity()
                                * holder.sale.getUnitPrice());
                        holder.totalPrice.setText(String.valueOf(holder.sale.getTotalPrice()));
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

    private void setIsPaidCheckListener(final SaleStockHolder holder) {
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

//	public long getKey(String name) {
//
//		for (long key : hmProducts.keySet()) {
//			if (hmProducts.get(key).equals(name))
//				return key;
//		}
//		return -1;
//	}

    public String getName(long id) {
        return hmProducts.get(id);
    }

    @Override
    public void remove(BaseStockedProduct saleToRemove) {
        if (isFormatedEdit()) {
            db.removeSaleStock(saleToRemove, sql);
            super.remove(saleToRemove);
        }
    }
}
