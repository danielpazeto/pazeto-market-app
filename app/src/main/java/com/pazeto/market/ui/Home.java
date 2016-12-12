package com.pazeto.market.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.pazeto.market.R;
import com.pazeto.market.db.DBFacade;
import com.pazeto.market.vo.BaseSaleStockedProduct;
import com.pazeto.market.vo.Client;
import com.pazeto.market.vo.Product;

import java.util.Random;

public class Home extends DefaultActivity {

    LinearLayout btCadProd, btClients;
    LinearLayout btListStock, btSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        btCadProd = (LinearLayout) findViewById(R.id.ll_cad_prod);
        btSales = (LinearLayout) findViewById(R.id.ll_btSell);
        btClients = (LinearLayout) findViewById(R.id.ll_btn_client);
        btListStock = (LinearLayout) findViewById(R.id.ll_bt_add_prod_stock);
        btCadProd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, ListProductsActivity.class);
                startActivity(i);
            }
        });

        btSales.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, SaleActivity.class);
                startActivity(i);
            }
        });
        btListStock.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, StockActivity.class);
                startActivity(i);

            }
        });
        btClients.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, ListClientsActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public void openReportActivity(View v) {
        Intent i = new Intent(Home.this, ReportActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_fake_data:
                this.insertFakeData();
                return true;
            case R.id.drop_table:
                this.dropTables();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void dropTables() {
        try {
            sql.execSQL(Client.DROP_TABLE_CLIENT);
            sql.execSQL(Product.DROP_TABLE_PRODUCT);
            sql.execSQL(BaseSaleStockedProduct.DROP_TABLE_STOCKED_ITEM);
            Log.d("DBFacade", "Dropou Tabelas");

            db = new DBFacade(getApplicationContext());
            Log.d("DBFacade", "Dropou Tabelas");
        } catch (Exception e) {
            Log.e("db", "Failure on create tables " + e);
        }

    }


    public void insertFakeData() {
        String[] prodNames = {"Chuchu", "Tomate", "Abacate", "Manga", "Batata"};
        String[] prodDesc = {"A", "AA", "AAA", "Boa", "Média"};

        for (String name : prodNames) {
            String desc = prodDesc[new Random().nextInt(4)];
            Product pro = new Product();
            pro.setName(name);
            pro.setDescription(desc);
            db.persistProduct(pro);
        }

        String[] clientNames = {"João", "Marcos", "Pedro", "Carlos", "Maria"};
        String[] clientLastName = {"Da Silva", "Pereira", "Aparecido", "Vargas", "Souza"};

        for (String name : clientNames) {
            String desc = clientLastName[new Random().nextInt(4)];
            Client pro = new Client();
            pro.setName(name);
            pro.setLastname(desc);
            db.persistClient(pro);
        }


    }

}
