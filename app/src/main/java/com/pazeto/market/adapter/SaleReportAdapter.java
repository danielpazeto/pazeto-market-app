package com.pazeto.market.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pazeto.market.R;
import com.pazeto.market.db.DBFacade;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.Product;
import com.pazeto.market.vo.SaleItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class SaleReportAdapter extends ArrayAdapter<SaleItem> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<SaleItem> mData = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<>();

    private LayoutInflater mInflater;

    public SaleReportAdapter(Context context, int resource) {
        super(context, resource);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void addItem(final SaleItem sale) {
        mData.add(sale);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final SaleItem saleToExtractDate) {
        mData.add(saleToExtractDate);
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
    public SaleItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SaleHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            switch (rowType) {
                case TYPE_ITEM:
                    holder = new SaleHolder();
                    convertView = mInflater.inflate(R.layout.report_sale_list_item, null);
                    // holder.tvDateHeaderGroup = (TextView) convertView.findViewById(R.id.text);

                    holder.position = (TextView) convertView.findViewById(R.id.pos);
                    holder.position.setText(String.valueOf(position));

                    holder.prodName = (TextView) convertView.findViewById(R.id.sale_product);
                    holder.prodName.setText(hmProducts.get(holder.sale.getIdProduct()));

                    holder.quantity = (TextView) convertView.findViewById(R.id.sale_quantity);
                    holder.quantity.setText(holder.sale.getQuantity() + "");

                    holder.unitPrice = (TextView) convertView.findViewById(R.id.sale_price_unit);
                    holder.unitPrice.setText(holder.sale.getUnitPrice() + "");


                    holder.totalPrice = (TextView) convertView.findViewById(R.id.sale_price_total);
                    holder.totalPrice.setText(holder.sale.getTotalPrice() + "");


//                    holder.totalPrice = (CheckBox) convertView.findViewById(R.id.sale_ck_is_paid);
//                    setIsPaidCheckListener(holder);
                    break;
                case TYPE_SEPARATOR:
                    holder = new SaleHolder();
                    convertView = mInflater.inflate(R.layout.product_list_item, null);
                    holder.tvDateHeaderGroup = (TextView) convertView.findViewById(R.id.tvProdName);
                    holder.tvDateHeaderGroup.setText(mData.get(position).getDate() + "");
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (SaleHolder) convertView.getTag();
        }


        return convertView;
    }

    DBFacade db;
    String[] myListProducts;
    HashMap<Long, String> hmProducts;
    List<Client> clients;
    String[] myListClients;
    HashMap<Long, String> hmClients;

    public void refreshProductAndClientList() {
//        List cursorProducts = db.listProducts();
//        hmProducts = new HashMap<>();
//        int productItemIdex = 0;
//        myListProducts = new String[cursorProducts.getCount()];
//        while (cursorProducts.moveToNext()) {
//            long idProd = cursorProducts.getInt(cursorProducts
//                    .getColumnIndex(Product.ID));
//            StringBuilder name = new StringBuilder(
//                    cursorProducts.getString(cursorProducts
//                            .getColumnIndex(Product.NAME))).append(" ").append(
//                    cursorProducts.getString(cursorProducts
//                            .getColumnIndex(Product.DESCRIPTION)));
//            myListProducts[productItemIdex] = name.toString();
//            hmProducts.put(idProd, name.toString());
//            productItemIdex++;
//        }
//        // setup clients
//        clients = db.listClients();
//        hmClients = new HashMap<>();
//        int j = 0;
//        myListClients = new String[clients.size()];
//        for (Client client : clients) {
//            long idClient = client.getId();
//            String name = new StringBuilder(client.getName()).append(" ").append(
//                    client.getLastname()).toString();
//            myListClients[j] = name;
//            hmClients.put(idClient, name);
//            j++;
//        }
//
//        this.notifyDataSetChanged();
    }

    private static class SaleHolder {
        SaleItem sale;

        TextView position;
        public TextView prodName;
        public TextView quantity;
        public TextView unitPrice;
        TextView totalPrice;
        CheckBox isPaid;
        // ImageButton removeSaleButton;

        //when it is group header
        public TextView tvDateHeaderGroup;

    }

}