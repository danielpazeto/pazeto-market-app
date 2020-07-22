package com.pazeto.comercio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pazeto.comercio.R;
import com.pazeto.comercio.adapter.ClientProductAdapter;
import com.pazeto.comercio.db.FirebaseHandler;
import com.pazeto.comercio.vo.Product;

import java.util.ArrayList;
import java.util.List;

public class ListProductsActivity extends DefaultActivity implements ClientProductAdapter.RecyclerViewClickListener {

    protected static final String TAG = ListProductsActivity.class.getName();
    public static String EXTRA_IS_TO_SELECT_PRODUCT = "IS_TO_SELECT_PRODUCT";
    public static String EXTRA_PRODUCT = "IS_TO_SELECT_PRODUCT";

    RecyclerView productRecyclerView;
    ClientProductAdapter customAdapter;
    boolean isToSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        listProducts();

        if (getIntent().getExtras() != null) {
            isToSelect = getIntent().getBooleanExtra(EXTRA_IS_TO_SELECT_PRODUCT, false);
            if (isToSelect) {
                setTheme(R.style.PopupTheme);
                setTitle(R.string.choose_product);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        productRecyclerView = findViewById(R.id.products_recycler_view);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdapter = new ClientProductAdapter(ClientProductAdapter.PRODUCT, ListProductsActivity.this);
        productRecyclerView.setAdapter(customAdapter);

    }

    public void listProducts() {
        new FirebaseHandler(getApplicationContext()).getProductsCollectionReference().get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Product> products = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());

                            Product product = document.toObject(Product.class);
                            product.setId(document.getId());
                            products.add(product);
                        }
                        customAdapter.setItems(products);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            listProducts();
        }
        Log.d(TAG, requestCode + " / " + resultCode + "  / " + data);
    }

    public void addNewProduct(View v) {
        Intent iProd = new Intent(ListProductsActivity.this, EditProductActivity.class);
        iProd.putExtra(ListProductsActivity.EXTRA_PRODUCT, new Product());
        startActivityForResult(iProd, 1);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Product prod = (Product) customAdapter.getItem(position);
        if (isToSelect) {
            Intent intent = new Intent();
            intent.putExtra(ListProductsActivity.EXTRA_PRODUCT, prod);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Intent iProd = new Intent(ListProductsActivity.this, EditProductActivity.class);
            iProd.putExtra(ListProductsActivity.EXTRA_PRODUCT, prod);
            startActivityForResult(iProd, 1);
        }
    }
}
