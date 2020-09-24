package com.pazeto.comercio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.pazeto.comercio.widgets.NumberTextWatcher;
import com.pazeto.comercio.widgets.Utils;

import java.text.DecimalFormat;

import static com.pazeto.comercio.widgets.Utils.setNumberValueView;

public class EditSaleStockActivity extends DefaultActivity {

    protected static final String TAG = EditSaleStockActivity.class.getName();
    private BaseSaleStocked currentItem;

    private TextView tvProductName, tvProductDescription, tvTotalValue;
    private EditText etUnitValue;
    private EditText etQuantity;
    private TextInputLayout inputLayoutQuantity;
    private TextInputLayout inputLayoutUnitValue;
    private FloatingActionButton btnChooseProduct, btnSave, btnCancel;;
    public static String SALE_STOCK_EXTRA = "SALE_STOCK_EXTRA";
    private Button btnPlusQuantity, btnMinusQuantity, btnMinusUnitPrice, btnPlusUnitPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFinishOnTouchOutside(false);
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

            tvTotalValue = findViewById(R.id.tv_sale_stock_total_value);

            etUnitValue = findViewById(R.id.sale_stock_et_unit_value);
            etUnitValue.addTextChangedListener(new UnitPriceTextWatcher(etUnitValue));

            etQuantity = findViewById(R.id.sale_stock_et_quantity);
            etQuantity.addTextChangedListener(new QuantityTextWatcher(etQuantity));

            btnSave = findViewById(R.id.sale_stock_btn_save);
            btnCancel = findViewById(R.id.sale_stock_btn_cancel);

            btnSave.setOnClickListener(btnSaveClickListener);
            btnCancel.setOnClickListener(btnCancelClickListener);

            btnMinusQuantity = findViewById(R.id.fltbtn_minus_quantity);
            btnMinusQuantity.setOnClickListener(minusQuantityClickListener);
            btnPlusQuantity = findViewById(R.id.fltbtn_plus_quantity);
            btnPlusQuantity.setOnClickListener(plusQuantityClickListener);
            btnMinusUnitPrice = findViewById(R.id.fltbtn_minus_unit_price);
            btnMinusUnitPrice.setOnClickListener(minusUnitPriceClickListener);
            btnPlusUnitPrice = findViewById(R.id.fltbtn_plus_unit_price);
            btnPlusUnitPrice.setOnClickListener(plusUnitPriceClickListener);

            updateTextViews();
        }
    }

    private void updateTextViews() {
        tvProductName.setText(currentItem.getProduct().getName());
        tvProductDescription.setText(currentItem.getProduct().getDescription());

        setNumberValueView(etQuantity, currentItem.getQuantity());
        setNumberValueView(etUnitValue, currentItem.getUnitPrice());
        setNumberValueView(tvTotalValue, currentItem.getTotalPrice());
    }

    View.OnClickListener btnChooseProdClickListener = view -> {
        Intent intent = new Intent(EditSaleStockActivity.this, ListProductsActivity.class);
        intent.putExtra(ListProductsActivity.EXTRA_IS_TO_SELECT_PRODUCT, true);
        EditSaleStockActivity.this.startActivityForResult(intent, 0);
    };

    View.OnClickListener btnSaveClickListener = v -> {
        if (currentItem.getProduct().getName() != null) {
            save(currentItem);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.choose_product), Toast.LENGTH_LONG).show();
        }
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

    class QuantityTextWatcher extends NumberTextWatcher{

        public QuantityTextWatcher(EditText et) {
            super(et);
        }

        @Override
        protected void textChangedCallback(double number) {
            currentItem.setQuantity(number);
            currentItem.setTotalPrice(currentItem.getQuantity() * currentItem.getUnitPrice());
            setNumberValueView(tvTotalValue, currentItem.getTotalPrice());
        }
    }

    class UnitPriceTextWatcher extends NumberTextWatcher{

        public UnitPriceTextWatcher(EditText et) {
            super(et);
        }

        @Override
        protected void textChangedCallback(double number) {
            currentItem.setUnitPrice(number);
            currentItem.setTotalPrice(currentItem.getQuantity() * currentItem.getUnitPrice());
            setNumberValueView(tvTotalValue, currentItem.getTotalPrice());
        }
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

    View.OnClickListener minusQuantityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (currentItem.getQuantity() <= 1.0) {
                currentItem.setQuantity(0);
                setNumberValueView(etQuantity,currentItem.getQuantity());
            } else {
                setNumberValueView(etQuantity,currentItem.getQuantity() - 1.0);
            }
        }
    };

    View.OnClickListener plusQuantityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setNumberValueView(etQuantity,currentItem.getQuantity() + 1.0);
        }
    };

    View.OnClickListener minusUnitPriceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (currentItem.getUnitPrice() <= 1.0) {
                currentItem.setUnitPrice(0);
                setNumberValueView(etUnitValue,currentItem.getUnitPrice());
            } else {
                setNumberValueView(etUnitValue, currentItem.getUnitPrice() - 1.0);
            }
        }
    };

    View.OnClickListener plusUnitPriceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setNumberValueView(etUnitValue,currentItem.getUnitPrice() + 1.0);
        }
    };
}

