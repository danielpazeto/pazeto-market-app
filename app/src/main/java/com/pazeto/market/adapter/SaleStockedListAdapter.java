package com.pazeto.market.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pazeto.market.R;
import com.pazeto.market.db.DBFacade;
import com.pazeto.market.ui.ListProductsActivity;
import com.pazeto.market.vo.BaseStockedProduct;
import com.pazeto.market.widgets.Utils;

import java.util.List;

public class SaleStockedListAdapter extends ArrayAdapter<BaseStockedProduct> {

    protected static final String LOG_TAG = SaleStockedListAdapter.class
            .getSimpleName();

    //    private List<BaseStockedProduct> items;
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
//        this.items = items;
        db = new DBFacade(getContext());
        sql = db.getWritableDatabase();
        // Produtos disponiveis, ou seja, produtos com numero maior que zero no
        // estoque
        hmProducts = new Utils.ProductHashMap(db);

    }

    @Override
    public BaseStockedProduct getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SaleStockHolder holder;
        if (convertView == null) {
            Log.d(LOG_TAG, "Setando novo");
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.add_sale_list_item, null);
            holder = new SaleStockHolder();
            holder.sale = getItem(position);

            setupProductNameText(holder, convertView, position);

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
            Log.d(LOG_TAG, "Ja existe");
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

    public void insertProductOnPosition(int listPosition, Long prodId) {
        getItem(listPosition).setIdProduct(prodId);
        notifyDataSetInvalidated();
    }

    private class SaleStockHolder {
        TextView position;
        BaseStockedProduct sale;
        TextView prodName;
        EditText quantity;
        EditText unitPrice;
        TextView totalPrice;
        CheckBox isPaid;
        ImageButton removeSaleButton;
    }


    private void setupProductNameText(final SaleStockHolder holder, View convertView, final int pos) {
        holder.prodName = (TextView) convertView
                .findViewById(R.id.sale_product);
        holder.prodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListProductsActivity.class);
                intent.putExtra(ListProductsActivity.IS_TO_SELECT_PRODUCT, pos);
                ((Activity)context).startActivityForResult(intent, 0);
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
