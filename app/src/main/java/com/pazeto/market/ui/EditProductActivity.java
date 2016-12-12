package com.pazeto.market.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.pazeto.market.R;
import com.pazeto.market.vo.Product;

public class EditProductActivity extends DefaultActivity {

    protected static final String TAG = "addProcudt";
    private Product currentProduct;

    private EditText etName;
    private EditText etDesc;
    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_prod);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutDesc = (TextInputLayout) findViewById(R.id.input_layout_desc);
        etName = (EditText) findViewById(R.id.cadprod_name);
        etDesc = (EditText) findViewById(R.id.cadprod_desc);

        long productId = getIntent().getLongExtra(Product.ID, -1);
        if (productId != -1) {
            currentProduct = db.getProduct(productId);
        } else {
            currentProduct = new Product();
        }
        loadProduct();

    }

    private void loadProduct() {
        etName.setText(currentProduct.getName());
        etDesc.setText(currentProduct.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_product_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                save();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean save() {
        String nameProd = etName.getText().toString();
        String descProd = etDesc.getText().toString();

        if (!validateName()) {
            return false;
        }
        if (!validateDesc()) {
            return false;
        }

        currentProduct.setName(nameProd);
        currentProduct.setDescription(descProd);

        if (db.persistProduct(currentProduct)) {
            setResult(RESULT_OK);
            Toast.makeText(getApplicationContext(),
                    R.string.save_successfully, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.product_exists, Toast.LENGTH_LONG).show();
        }
        return true;
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
