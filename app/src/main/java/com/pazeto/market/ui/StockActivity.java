package com.pazeto.market.ui;

import android.os.Bundle;

import com.pazeto.market.R;
import com.pazeto.market.vo.BaseStockedProduct;
import com.pazeto.market.vo.StockedItem;

public class StockActivity extends SaleStockBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sale_list_view);
    }

    @Override
    protected int getNewBtnDrawable() {
        return R.mipmap.ic_new_product;
    }

    @Override
    BaseStockedProduct getNewItem() {
        return new StockedItem(0, 0, unixDate);
    }

    @Override
    BaseStockedProduct.TYPE_PRODUCT getItemType() {
        return BaseStockedProduct.TYPE_PRODUCT.STOCKED;
    }

    @Override
    CharSequence getMenuTitle() {
        return getString(R.string.stock);
    }


//    private EditText edtCurrentDate;
//    private ProductStockListAdapter adapter;
//    Calendar myCalendar = Calendar.getInstance();
//    private long unixDate;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.productday_list_view);
//        edtCurrentDate = (EditText) findViewById(R.id.edt_date);
//        if (unixDate <= 0) {
//            createAndShowDatePickerDialog();
//        }
//        edtCurrentDate.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                createAndShowDatePickerDialog();
//            }
//
//        });
//    }
//
//    private void setupListViewAdapter(ArrayList<StockedItem> items) {
//        adapter = new ProductStockListAdapter(this,
//                R.layout.add_productday_list_item, items, db);
//        ListView productDaysListView = (ListView) findViewById(R.id.product_day_list_view);
//        productDaysListView.setEmptyView(findViewById(android.R.id.empty));
//        productDaysListView.setAdapter(adapter);
//        adapter.setSaveListener(saveListener);
//    }
//
//    public void onNewStockedProductBtn(View v) {
//        // if (adapter.isFormatedEdit()) {
//        Log.d("data do add: ", "" + unixDate);
//        adapter.insert(new StockedItem(0, 0, unixDate), 0);
//        // }
//    }
//
//    private void loadProductDayPerDate(long date) {
//        Cursor cursorProductsDay = db.listStockProductsInDate(date, sql);
//        ArrayList<StockedItem> prodsPerDate = new ArrayList<>();
//        while (cursorProductsDay.moveToNext()) {
//            prodsPerDate.add(new StockedItem(cursorProductsDay));
//        }
//        if (cursorProductsDay != null) {
//            cursorProductsDay.close();
//        }
//        setupListViewAdapter(prodsPerDate);
//    }
//
//    protected void createAndShowDatePickerDialog() {
//        final DatePickerDialog datePickDialog = new DatePickerDialog(this,
//                onSetDatePpickerListener, myCalendar.get(Calendar.YEAR),
//                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
//        datePickDialog.setTitle("Escolha a data:");
//        datePickDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
//                getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        datePickDialog.dismiss();
//                        finish();
//                    }
//                });
//        datePickDialog.show();
//    }
//
//    private DatePickerDialog.OnDateSetListener onSetDatePpickerListener = new DatePickerDialog.OnDateSetListener() {
//
//        // when dialog box is closed, below method will be called.
//        @Override
//        public void onDateSet(DatePicker view, int selectedYear,
//                              int selectedMonth, int selectedDay) {
//            myCalendar.set(Calendar.YEAR, selectedYear);
//            myCalendar.set(Calendar.MONTH, selectedMonth);
//            myCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
//
//            String myFormat = "dd/MM/yy"; //In which you need put here
//            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//            edtCurrentDate.setText(sdf.format(myCalendar.getTime()));
//
//            unixDate = Utils.calendarIntanceToStartSecondsDay(myCalendar);
//            loadProductDayPerDate(unixDate);
//        }
//
//    };
//
//    private boolean saveStockProducts() {
//        try {
//            int savedIdsCounter = 0;
//            System.out.println("ITEMS: " + adapter.getCount());
//            for (int i = 0; i < adapter.getCount(); i++) {
//                StockedItem prodDay = adapter.getItem(i);
//
//                System.out.println("ID: " + prodDay.getId() + " Quant.: "
//                        + prodDay.getQuantity() + " Prod id: "
//                        + prodDay.getIdProduct() + " Date: "
//                        + prodDay.getDate() + " e valor : "
//                        + prodDay.getUnitPrice());
//
//                long id = db.insertProductStock(prodDay, sql);
//                if (id != -1 && id > 0) {
//                    System.out.println("salvou com novo id: " + id);
//                    adapter.getItem(i).setId(id);
//                }
//                savedIdsCounter++;
//            }
//            Toast.makeText(getApplicationContext(),
//                    "Salvou " + savedIdsCounter + " Item(s).",
//                    Toast.LENGTH_SHORT).show();
//            return true;
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "Erro ao salvar itens",
//                    Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (saveListener.save())
//            super.onBackPressed();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                if (saveListener.save())
//                    this.finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        System.out.println("Atualizando produtos e clientes");
//        if (adapter != null) {
//            adapter.refreshProductAndClientList();
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    SaveStockListener saveListener = new SaveStockListener() {
//
//        @Override
//        public boolean save() {
//            return saveStockProducts();
//
//        }
//    };
//
//    public interface SaveStockListener {
//        boolean save();
//    }
}
