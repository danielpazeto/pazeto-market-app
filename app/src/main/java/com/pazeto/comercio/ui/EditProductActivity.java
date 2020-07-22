package com.pazeto.comercio.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.pazeto.comercio.R;
import com.pazeto.comercio.db.FirebaseHandler;
import com.pazeto.comercio.vo.Product;

public class EditProductActivity extends DefaultActivity {

    protected static final String TAG = EditProductActivity.class.getName();
    private Product currentProduct;

    private EditText etName;
    private EditText etDesc;
    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutDesc;
    FloatingActionButton fltBtnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_prod);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutDesc = (TextInputLayout) findViewById(R.id.input_layout_desc);
        etName = (EditText) findViewById(R.id.cadprod_name);
        etDesc = (EditText) findViewById(R.id.cadprod_desc);

        fltBtnSave = findViewById(R.id.saveButton);
        fltBtnSave.setOnClickListener(btnSaveClickListener);

        currentProduct = (Product) getIntent().getSerializableExtra(ListProductsActivity.EXTRA_PRODUCT);
        updateViews();
    }

    View.OnClickListener btnSaveClickListener = v -> {

        String nameProd = etName.getText().toString();
        String descProd = etDesc.getText().toString();
        currentProduct.setName(nameProd);
        currentProduct.setDescription(descProd);

        if (validateName() || validateDesc()) {
            save(currentProduct);
        }
    };


    private void updateViews() {
        etName.setText(currentProduct.getName());
        etDesc.setText(currentProduct.getDescription());
    }


    private void save(Product product) {

        Log.d(TAG, "Saving product");

        fltBtnSave.setEnabled(false);

        CollectionReference collectionRef = new FirebaseHandler(getApplicationContext()).getProductsCollectionReference();
        Task<Void> ref;
        if (product.getId() != null){
            ref = collectionRef.document(product.getId()).set(product);
        } else {
            ref = collectionRef.document().set(product);
        }
        ref.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot added ");
                setResult(RESULT_OK);
                Toast.makeText(getApplicationContext(),
                        R.string.save_successfully, Toast.LENGTH_LONG).show();
                finish();
            }

        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "N�o salvou product.");
                fltBtnSave.setEnabled(true);
            }
        });
    }


    private boolean validateName() {
        if (etName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.invalid_name));
            etName.requestFocus();
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateDesc() {
        if (etDesc.getText().toString().trim().isEmpty()) {
            inputLayoutDesc.setError(getString(R.string.invalid_name));
            etDesc.requestFocus();
            return false;
        } else {
            inputLayoutDesc.setErrorEnabled(false);
            return true;
        }
    }


}
