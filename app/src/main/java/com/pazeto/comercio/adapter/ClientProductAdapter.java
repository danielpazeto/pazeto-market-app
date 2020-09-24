package com.pazeto.comercio.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pazeto.comercio.R;
import com.pazeto.comercio.vo.Client;
import com.pazeto.comercio.vo.Product;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientProductAdapter extends RecyclerView.Adapter<ClientProductAdapter.BaseViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter, FastScrollRecyclerView.MeasurableAdapter {

    final public static int PRODUCT = 1;
    final public static int CLIENT = 2;
    private int type;
    private List items = new ArrayList();
    private static RecyclerViewClickListener itemListener;

    @NonNull
    @Override
    public String getSectionName(int position) {

        if(this.type == CLIENT){
            return ((Client) getItem(position)).getName().toUpperCase().substring(0, 1);
        } else {
            return ((Product) getItem(position)).getName().toUpperCase().substring(0, 1);

        }
    }

    @Override
    public int getViewTypeHeight(RecyclerView recyclerView, @Nullable RecyclerView.ViewHolder viewHolder, int viewType) {
        return recyclerView.getResources().getDimensionPixelSize(R.dimen.client_list_item_height);

    }


    public interface RecyclerViewClickListener {
        public void recyclerViewListClicked(View v, int position);
    }

    public ClientProductAdapter(int type, RecyclerViewClickListener itemListener) {
        this.type = type;
        ClientProductAdapter.itemListener = itemListener;
    }

    static abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View view;
        public BaseViewHolder(View v) {
            super(v);
            this.view = v;
            this.view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getAdapterPosition());
        }
    }

    public static class ClientViewHolder extends BaseViewHolder {
        TextView tvClientName, tvClientCity, tvClientTelephone, tcClientCellphone1;
        public ClientViewHolder(View view) {
            super(view);
            this.tvClientName = view.findViewById(R.id.tv_client_name);
            this.tvClientCity = view.findViewById(R.id.tv_client_city);
            this.tvClientTelephone = view.findViewById(R.id.tv_client_telephone);
            this.tcClientCellphone1 = view.findViewById(R.id.tv_client_cellphone1);
        }
    }

    public static class ProductViewHolder extends BaseViewHolder {
        TextView tvProdName, tvProdDesc;
        public ProductViewHolder(View view) {
            super(view);
            this.tvProdName = view.findViewById(R.id.tvProdName);
            this.tvProdDesc = view.findViewById(R.id.tvProdDescription);
        }
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public ClientProductAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BaseViewHolder viewHolder = null;
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (type) {
            case PRODUCT:
                view = inflater.inflate(R.layout.product_list_item, parent, false);
                viewHolder = new ProductViewHolder(view);
                break;
            case CLIENT:
                view = inflater.inflate(R.layout.client_list_item, parent, false);
                viewHolder = new ClientViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        switch (type) {
            case PRODUCT:
                Product prod = (Product) getItem(position);

                ProductViewHolder productViewHolder = (ProductViewHolder) holder;
                productViewHolder.tvProdName.setText(prod.getName());
                productViewHolder.tvProdDesc.setText(prod.getDescription());
                break;
            case CLIENT:
                Client client = (Client) getItem(position);

                ClientViewHolder clientViewHolder = (ClientViewHolder) holder;
                clientViewHolder.tvClientName.setText(client.getName() + " " + client.getLastname());
                clientViewHolder.tvClientCity.setText(client.getCity());
                clientViewHolder.tvClientTelephone.setText(client.getTelephone());
                clientViewHolder.tcClientCellphone1.setText(client.getCellPhone());
                break;

            default:

        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List items) {

        if(this.type == CLIENT){
            Collections.sort(items, new Client.ClientNameComparator());
        } else {
            Collections.sort(items, new Product.ProductNameComparator());
        }

        this.items = items;
        this.notifyDataSetChanged();
    }
}
