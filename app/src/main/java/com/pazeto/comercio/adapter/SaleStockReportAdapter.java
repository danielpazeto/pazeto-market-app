package com.pazeto.comercio.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pazeto.comercio.R;
import com.pazeto.comercio.vo.BaseSaleStocked;
import com.pazeto.comercio.widgets.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class SaleStockReportAdapter extends ArrayAdapter<BaseSaleStocked> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<BaseSaleStocked> mData = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<>();

    private LayoutInflater mInflater;

    public SaleStockReportAdapter(Context context, int resource) {
        super(context, resource);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void addItem(final BaseSaleStocked sale) {
        mData.add(sale);
    }

    @Override
    public void clear() {
        mData.clear();
        sectionHeader.clear();
        super.clear();
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final BaseSaleStocked itemToExtractDate) {
        mData.add(itemToExtractDate);
        sectionHeader.add(mData.size() - 1);
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
    public BaseSaleStocked getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SaleStockHolder holder;
        int rowType = getItemViewType(position);
        if (convertView == null) {
            holder = new SaleStockHolder();
            holder.sale = mData.get(position);
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.report_sale_list_item, null);
                    convertView.findViewById(R.id.sale_remove).setVisibility(View.GONE);
                    holder.productName = convertView.findViewById(R.id.tv_sale_product);
                    holder.productDescription = convertView.findViewById(R.id.tv_sale_product_description);
                    holder.quantity = convertView.findViewById(R.id.sale_quantity);
                    holder.unitPrice = convertView.findViewById(R.id.sale_price_unit);
                    holder.totalPrice = convertView.findViewById(R.id.sale_price_total);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.date_section_header_item, null);
                    holder.tvDateHeaderGroup = (TextView) convertView.findViewById(R.id.tv_date_section);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (SaleStockHolder) convertView.getTag();
        }

        setupItem(holder, rowType, position, convertView);

        return convertView;
    }

    private void setupItem(SaleStockHolder holder, int rowType, int position, View convertView) {
        switch (rowType) {
            case TYPE_ITEM:
                holder.productName.setText(holder.sale.getProduct().getName());
                holder.productDescription.setText(holder.sale.getProduct().getDescription());
                holder.quantity.setText(String.valueOf(holder.sale.getQuantity()));
                holder.unitPrice.setText(String.valueOf(holder.sale.getUnitPrice()));
                holder.totalPrice.setText(String.valueOf(holder.sale.getTotalPrice()));
                if (holder.sale.getType().equals(BaseSaleStocked.TYPE.SALE)) {
                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.green_section_header));
                } else if (holder.sale.getType().equals(BaseSaleStocked.TYPE.STOCK)) {
                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.red_section_header));
                }
                break;
            case TYPE_SEPARATOR:
                holder.tvDateHeaderGroup.setText(Utils.formatDateToString(mData.get(position).getDate()));
                break;
        }
    }

    public void addAll(List<BaseSaleStocked> salesPerDateAndClient) {
        clear();
        Date auxDate = null;
        if (salesPerDateAndClient != null && !salesPerDateAndClient.isEmpty()) {

            for (BaseSaleStocked saleStockItem : salesPerDateAndClient) {

                if(auxDate == null || saleStockItem.getDate().compareTo(auxDate) != 0){
                    auxDate = saleStockItem.getDate();
                    addSectionHeaderItem(saleStockItem);
                }
                addItem(saleStockItem);
            }
        }
        notifyDataSetChanged();
    }

    private class SaleStockHolder {
        BaseSaleStocked sale;

        TextView productName;
        TextView productDescription;
        TextView quantity;
        TextView unitPrice;
        TextView totalPrice;
        CheckBox isPaid;

        //when it is group header
        public TextView tvDateHeaderGroup;

    }

}