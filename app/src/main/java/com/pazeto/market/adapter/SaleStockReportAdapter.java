package com.pazeto.market.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pazeto.market.R;
import com.pazeto.market.db.DBFacade;
import com.pazeto.market.vo.BaseSaleStockedProduct;
import com.pazeto.market.widgets.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class SaleStockReportAdapter extends ArrayAdapter<BaseSaleStockedProduct> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<BaseSaleStockedProduct> mData = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<>();

    private LayoutInflater mInflater;

    public SaleStockReportAdapter(Context context, int resource) {
        super(context, resource);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hmProducts = new Utils.ProductHashMap(new DBFacade(context));
    }


    public void addItem(final BaseSaleStockedProduct sale) {
        mData.add(sale);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final BaseSaleStockedProduct itemToExtractDate) {
        mData.add(itemToExtractDate);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public BaseSaleStockedProduct getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SaleStockHolder holder = null;
        int rowType = getItemViewType(position);
        if (convertView == null) {
            holder = new SaleStockHolder();
            holder.sale = mData.get(position);
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.report_sale_list_item, null);
                    holder.position = (TextView) convertView.findViewById(R.id.pos);
                    holder.prodName = (TextView) convertView.findViewById(R.id.sale_product);
                    holder.quantity = (TextView) convertView.findViewById(R.id.sale_quantity);
                    holder.unitPrice = (TextView) convertView.findViewById(R.id.sale_price_unit);
                    holder.totalPrice = (TextView) convertView.findViewById(R.id.sale_price_total);
                    if(holder.sale.getType().equals(BaseSaleStockedProduct.TYPE_PRODUCT.SALE)){
                        convertView.setBackgroundColor(Color.GREEN);
                    }else{
                        convertView.setBackgroundColor(Color.RED);
                    }
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
                    holder.tvDateHeaderGroup = (TextView) convertView.findViewById(android.R.id.text1);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (SaleStockHolder) convertView.getTag();
        }

        setupItem(holder, rowType, position);

        return convertView;
    }

    private void setupItem(SaleStockHolder holder, int rowType, int position) {
        switch (rowType) {
            case TYPE_ITEM:
                holder.position.setText(String.valueOf(position));
                holder.prodName.setText(hmProducts.get(holder.sale.getIdProduct()));
                holder.quantity.setText(String.valueOf(holder.sale.getQuantity()));
                holder.unitPrice.setText(String.valueOf(holder.sale.getUnitPrice()));
                holder.totalPrice.setText(String.valueOf(holder.sale.getTotalPrice()));
                break;
            case TYPE_SEPARATOR:
                String dateString = new SimpleDateFormat("dd/MM/yyyy").
                        format(new Date(mData.get(position).getDate()*1000));
                holder.tvDateHeaderGroup.setText(dateString);
                break;
        }
    }

    Utils.ProductHashMap hmProducts;

    public void addAll(HashMap<Long, List<BaseSaleStockedProduct>> salesPerDateAndClient) {
        if (salesPerDateAndClient != null && !salesPerDateAndClient.isEmpty()) {
            for (Long date : salesPerDateAndClient.keySet()) {
                Log.d(SaleStockReportAdapter.class.getName(), "Date : " + date);
                List<BaseSaleStockedProduct> listPerDate = salesPerDateAndClient.get(date);
                if (listPerDate != null && !listPerDate.isEmpty()) {
                    addSectionHeaderItem(listPerDate.get(0));
                    for (BaseSaleStockedProduct item : listPerDate) {
                        addItem(item);
                        Log.d(SaleStockReportAdapter.class.getName(), item.getQuantity() + " : "
                                + item.getIdProduct() + " : " + item.getUnitPrice());
                    }
                }

            }
        }
    }

    private class SaleStockHolder {
        BaseSaleStockedProduct sale;

        TextView position;
        TextView prodName;
        TextView quantity;
        TextView unitPrice;
        TextView totalPrice;
        CheckBox isPaid;
        // ImageButton removeSaleButton;

        //when it is group header
        public TextView tvDateHeaderGroup;

    }

}