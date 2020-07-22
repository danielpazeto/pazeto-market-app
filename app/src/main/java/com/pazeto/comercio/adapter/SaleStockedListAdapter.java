package com.pazeto.comercio.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pazeto.comercio.R;
import com.pazeto.comercio.db.FirebaseHandler;
import com.pazeto.comercio.ui.DefaultActivity;
import com.pazeto.comercio.ui.EditSaleStockActivity;
import com.pazeto.comercio.vo.BaseSaleStocked;
import com.pazeto.comercio.vo.Client;
import com.pazeto.comercio.vo.Sale;
import com.pazeto.comercio.vo.Stock;
import com.pazeto.comercio.widgets.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SaleStockedListAdapter extends BaseAdapter {

    protected static final String TAG = SaleStockedListAdapter.class.getSimpleName();

    private Context context;

    List<BaseSaleStocked> items = new ArrayList<>();
    Map<Integer, ViewHolder> hmHolderPostition;

    public SaleStockedListAdapter(Context context) {
        this.context = context;
        hmHolderPostition = new HashMap<>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public BaseSaleStocked getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.add_sale_list_item, null);

            setupItemClickListener(holder, convertView);

            holder.prodName = convertView.findViewById(R.id.tv_sale_product);
            holder.prodDescription = convertView.findViewById(R.id.tv_sale_product_description);
            holder.quantity = convertView.findViewById(R.id.sale_quantity);
            holder.unitPrice = convertView.findViewById(R.id.sale_price_unit);
            holder.totalPrice = convertView.findViewById(R.id.sale_price_total);
            holder.isPaid = convertView.findViewById(R.id.sale_ck_is_paid);
            setIsPaidCheckListener(holder);

            holder.removeSaleButton = convertView.findViewById(R.id.sale_remove);
            holder.removeSaleButton.setTag(holder);
            setRemoveButtonClickListener(holder);

            hmHolderPostition.put(position, holder);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        holder.index = position;

        setupItem(holder);

        return convertView;
    }

    private void setRemoveButtonClickListener(ViewHolder holder) {
        holder.removeSaleButton.setOnClickListener(this::remove);
    }

    public void updateItem(BaseSaleStocked saleStock) {
        ListIterator<BaseSaleStocked> itr = items.listIterator();
        boolean exists = false;
        while(itr.hasNext()) {
            BaseSaleStocked saleStockInList =itr.next();
            if(saleStockInList.getId().equals(saleStock.getId())) {
                itr.set(saleStock);
                exists = true;
                break;
            }
        }

        if (!exists) {
            items.add(saleStock);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {

        int index;
        TextView prodName;
        TextView prodDescription;
        TextView quantity;
        TextView unitPrice;
        TextView totalPrice;
        CheckBox isPaid;
        ImageButton removeSaleButton;
    }

    private void setupItem(ViewHolder holder) {

        BaseSaleStocked item = items.get(holder.index);

        holder.prodName.setText(item.getProduct().getName());
        holder.prodDescription.setText(item.getProduct().getDescription());
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.unitPrice.setText(String.valueOf(item.getUnitPrice()));
        holder.totalPrice.setText(String.valueOf(item.getTotalPrice()));
        holder.isPaid.setChecked(item.isPaid());
    }

    public void reload(Client currentClient, Date currentDate, BaseSaleStocked.TYPE type) {

        new FirebaseHandler(context).getSaleStocksCollectionReference()
                .whereEqualTo("date", Utils.ISO_8601_DATE_FORMAT.format(currentDate))
                .whereEqualTo("idClient", currentClient.getId())
                .whereEqualTo("type", type)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<BaseSaleStocked> saleStockList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                BaseSaleStocked saleStock;
                                if (type==BaseSaleStocked.TYPE.SALE) {
                                    saleStock = document.toObject(Sale.class);
                                } else {
                                    saleStock = document.toObject(Stock.class);
                                }

                                saleStock.setId(document.getId());
                                saleStockList.add(saleStock);
                            }

                            if (saleStockList.size() == 0) {
                                SimpleDateFormat sdf = new SimpleDateFormat(DefaultActivity.DATE_FORMAT_STRING, Locale.getDefault());

                                String saleStock = (type == BaseSaleStocked.TYPE.SALE) ? "vendas": "compras";
                                Toast.makeText(context,
                                        "Não há "+ saleStock +" para a data: " + sdf.format(currentDate), Toast.LENGTH_SHORT)
                                        .show();
                            }

                            setupListViewAdapter(saleStockList);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void remove(View view) {

        ViewHolder itemToRemove = (ViewHolder) view.getTag();
        BaseSaleStocked saleToRemove = items.get(itemToRemove.index);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(
                "Deseja remover: " + saleToRemove.getQuantity() + " x " + saleToRemove.getProduct().getFullname())
                .setPositiveButton(context.getResources().getText(R.string.remove),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeItem(itemToRemove.index);
                            }
                        })
                .setNegativeButton(context.getResources().getText(R.string.cancel), null);
        builder.create().show();

    }

    private void removeItem(int index) {

        new FirebaseHandler(context).getSaleStocksCollectionReference().document(getItem(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        items.remove(index);
                        notifyDataSetChanged();
                    }
                });

    }

    public void add(BaseSaleStocked newItem) {
        new FirebaseHandler(context).getSaleStocksCollectionReference()
                .add(newItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                        items.add(getCount(), newItem);

                        newItem.setId(documentReference.getId());

                        SaleStockedListAdapter.this.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding document", e);
                    }
                });
    }

    private void setupItemClickListener(final ViewHolder holder, View convertView) {

        LinearLayout productLayout = convertView.findViewById(R.id.sale_stock_item);
        productLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditSaleStockActivity.class);
            intent.putExtra(EditSaleStockActivity.SALE_STOCK_EXTRA, getItem(holder.index));
            ((Activity) context).startActivityForResult(intent, 0);
        });
    }



    private void setIsPaidCheckListener(final ViewHolder holder) {
        holder.isPaid.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                try {
                    BaseSaleStocked sale = getItem(holder.index);
                    sale.setPaid(isChecked);
                } catch (Exception e) {
                    Log.e(TAG, "CHeckBox is Paid?: " + isChecked
                            + " Erro: " + e);
                }
            }
        });
    }



    private void setupListViewAdapter(List<BaseSaleStocked> newItems) {
        this.items.clear();
        if(newItems != null) {
            this.items.addAll(newItems);
        }
        this.notifyDataSetChanged();
    }

}
