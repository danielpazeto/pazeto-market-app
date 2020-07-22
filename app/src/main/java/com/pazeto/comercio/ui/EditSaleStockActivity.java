package com.pazeto.comercio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.pazeto.comercio.R;
import com.pazeto.comercio.db.FirebaseHandler;
import com.pazeto.comercio.vo.BaseSaleStocked;
import com.pazeto.comercio.vo.Product;

public class EditSaleStockActivity extends DefaultActivity {

    protected static final String TAG = EditSaleStockActivity.class.getName();
    private BaseSaleStocked currentItem;

    private TextView tvProductName, tvProductDescription, tvTotalValue;
    private EditText etUnitValue;
    private EditText etQuantity;
    private TextInputLayout inputLayoutQuantity;
    private TextInputLayout inputLayoutUnitValue;
    private Button btnSave, btnCancel;
    private FloatingActionButton btnChooseProduct;
    public static String SALE_STOCK_EXTRA = "SALE_STOCK_EXTRA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.PopupTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_salestock_activity);

        if (getIntent().getExtras() != null) {
            currentItem = (BaseSaleStocked) getIntent().getSerializableExtra(SALE_STOCK_EXTRA);
            if (currentItem.getType().equals(BaseSaleStocked.TYPE.SALE)) {
                setTitle(R.string.sale);
            } else {
                setTitle(R.string.stock);
            }
            btnChooseProduct = findViewById(R.id.btn_salestock_choose_prod);
            btnChooseProduct.setOnClickListener(btnChooseProdClickListener);

            tvProductName = findViewById(R.id.tv_sale_product);
            tvProductDescription = findViewById(R.id.tv_sale_product_description);

            inputLayoutQuantity = findViewById(R.id.input_layout_salestock_quantity);
            inputLayoutUnitValue = findViewById(R.id.input_layout_salestock_unit_value);

            etUnitValue = findViewById(R.id.sale_stock_et_unit_value);
            setUnitPriceTextListeners();
            etQuantity = findViewById(R.id.sale_stock_et_quantity);
            setQuantityTextListeners();

            tvTotalValue = findViewById(R.id.tv_sale_stock_total_value);

            btnSave = findViewById(R.id.sale_stock_btn_save);
            btnCancel = findViewById(R.id.sale_stock_btn_cancel);

            btnSave.setOnClickListener(btnSaveClickListener);
            btnCancel.setOnClickListener(btnCancelClickListener);

            updateTextViews();
        }
    }

    private void updateTextViews() {
        tvProductName.setText(currentItem.getProduct().getName());
        tvProductDescription.setText(currentItem.getProduct().getDescription());
        etUnitValue.setText(String.valueOf(currentItem.getUnitPrice()));
        etQuantity.setText(String.valueOf(currentItem.getQuantity()));
        tvTotalValue.setText(String.valueOf(currentItem.getTotalPrice()));
    }

    View.OnClickListener btnChooseProdClickListener = view -> {
        Intent intent = new Intent(EditSaleStockActivity.this, ListProductsActivity.class);
        intent.putExtra(ListProductsActivity.EXTRA_IS_TO_SELECT_PRODUCT, true);
        EditSaleStockActivity.this.startActivityForResult(intent, 0);
    };

    View.OnClickListener btnSaveClickListener = v -> {
        save(currentItem);
    };

    View.OnClickListener btnCancelClickListener = v -> {
        finish();
    };


    private void save(BaseSaleStocked saleStock) {

        Log.d(TAG, "Saving SaleStock");

        btnSave.setEnabled(false);

        CollectionReference collectionRef = new FirebaseHandler(getApplicationContext()).getSaleStocksCollectionReference();
        DocumentReference ref;
        if (saleStock.getId() != null) {
            ref = collectionRef.document(saleStock.getId());
        } else {
            ref = collectionRef.document();
            saleStock.setId(ref.getId());
        }
        ref.set(saleStock).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot added ");
                Intent intent = new Intent();
                intent.putExtra(SALE_STOCK_EXTRA, saleStock);
                setResult(RESULT_OK, intent);
                finish();
            }

        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "N�o salvou product.");
                btnSave.setEnabled(true);
            }
        });
    }


    private boolean validateName() {
        if (etUnitValue.getText().toString().trim().isEmpty()) {
            inputLayoutQuantity.setError(getString(R.string.invalid_name));
            etUnitValue.requestFocus();
            return false;
        } else {
            inputLayoutQuantity.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateDesc() {
        if (etQuantity.getText().toString().trim().isEmpty()) {
            inputLayoutUnitValue.setError(getString(R.string.invalid_name));
            etQuantity.requestFocus();
            return false;
        } else {
            inputLayoutUnitValue.setErrorEnabled(false);
            return true;
        }
    }

    private void setQuantityTextListeners() {
        etQuantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    currentItem.setQuantity(Double.parseDouble(s.toString()));
                    currentItem.setTotalPrice(currentItem.getQuantity() * currentItem.getUnitPrice());
                    etQuantity.removeTextChangedListener(this);
                    updateTextViews();
                    etQuantity.addTextChangedListener(this);
                } catch (NumberFormatException e) {
                    Log.e(TAG,
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

    private void setUnitPriceTextListeners() {
        etUnitValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    currentItem.setUnitPrice(Double.parseDouble(s.toString()));
                    currentItem.setTotalPrice(currentItem.getQuantity() * currentItem.getUnitPrice());

                    etUnitValue.removeTextChangedListener(this);
                    updateTextViews();
                    etUnitValue.addTextChangedListener(this);
                } catch (NumberFormatException e) {
                    Log.e(TAG,
                            "error reading double value: " + s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (data.hasExtra(ListProductsActivity.EXTRA_PRODUCT)) {
                Product prod = (Product) data.getSerializableExtra(ListProductsActivity.EXTRA_PRODUCT);
                currentItem.setProduct(prod);
                updateTextViews();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

