package com.pazeto.market.ui;

import android.os.Bundle;

import com.pazeto.market.R;
import com.pazeto.market.vo.BaseStockedProduct;
import com.pazeto.market.vo.SaleItem;

public class SaleActivity extends SaleStockBaseActivity {
    private static final String TAG = SaleActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sale_list_view);
    }

    @Override
    protected int getNewBtnDrawable() {
        return R.mipmap.ic_new_sale;
    }

    @Override
    BaseStockedProduct getNewItem() {
        return new SaleItem(0, 0, unixDate);
    }

//    /**
//     * Remove a venda
//     *
//     * @param v
//     */
//    public void removeSaleOnClickHandler(View v) {
//        final SaleItem itemToRemove = (SaleItem) v.getTag();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(
//                "Deseja remover o item nº " + v.getId() + ":\n" + itemToRemove.getQuantity() + " - "
//                        + adapter.getName(itemToRemove.getIdProduct()) + " - "
//                        + itemToRemove.getUnitPrice())
//                .setPositiveButton(getResources().getText(R.string.remove),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                adapter.remove(itemToRemove);
//                            }
//                        })
//                .setNegativeButton(getResources().getText(R.string.cancel), null);
//        builder.create().show();
//    }

//    private void setupListViewAdapter(ArrayList<SaleItem> items) {
//        adapter = new SaleStockedListAdapter(this, R.layout.add_sale_list_item, items,
//                unixDate);
//        ListView salesListView = (ListView) findViewById(R.id.sale_ListView);
//        salesListView.setAdapter(adapter);
//    }
//
//    public void onAddSaleBtnClick(View v) {
//        if (isConfigured() && unixDate > 0) {
//            adapter.insert(new SaleItem(0, 0, unixDate), 0);
//        } else {
//            Toast.makeText(getApplicationContext(),
//                    "Cliente ou Data inválidos.", Toast.LENGTH_SHORT)
//                    .show();
//        }
//    }

    @Override
    BaseStockedProduct.TYPE_PRODUCT getItemType() {
        return BaseStockedProduct.TYPE_PRODUCT.SALE;
    }

    @Override
    CharSequence getMenuTitle() {
        return getString(R.string.sale);
    }


//    private void loadSalePerDateAndClient(long date, Client client) {
//        ArrayList<SaleItem> salesPerDateAndClient = db.listSaleAndStockPerDateAndClient(
//                date, client, sql);
//        if (salesPerDateAndClient.size() == 0) {
//            Toast.makeText(getApplicationContext(),
//                    "Não há itens para a data: " + date, Toast.LENGTH_SHORT)
//                    .show();
//        }
//        Log.d(TAG, salesPerDateAndClient.size()
//                + " resultados para o cliente:  " + client);
//        setupListViewAdapter(salesPerDateAndClient);
//    }

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
//                    }
//                });
//        datePickDialog.show();
//    }

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
//            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr, Locale.getDefault());
//
//            edtCurrentDate.setText(sdf.format(myCalendar.getTime()));
//
//            unixDate = calendarIntanceToStartSecondsDay(myCalendar);
//            if (isConfigured()) {
//                loadItemsPerDateAndClient(unixDate, currentClient);
//            }
//        }
//
//    };

//    @Override
//    public void onBackPressed() {
//        saveSales();
//        super.onBackPressed();
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count,
//                                  int after) {
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        System.out.println("Cliente autocomplete: " + s.toString());
//        long id = hmClients.getIdByName(s.toString());
//        if (id != -1) {
//            currentClient = new Client();
//            currentClient.setId(id);
//            currentClient.setName(s.toString());
//            // holder.productDay.setIdProduct(id);
//            System.out.println("Setou id " + id + "  e  name " + s);
//            if (isConfigured()) {
//                loadItemsPerDateAndClient(unixDate, currentClient);
//            }
//        } else {
//            currentClient = null;
//        }
//    }
//
//    /**
//     * Inicializa o autocomplete do cliente
//     */
//    public void setupAutoCompleteClient() {
//        hmClients = new Utils.ClientHashMap(db);
//
//        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
//                getApplicationContext(), R.layout.dropdown_list_custom,
//                hmClients.getClientNames());
//        AutoCompleteTextView autoCompProduct = (AutoCompleteTextView) findViewById(R.id.OutputClient);
//        autoCompProduct.addTextChangedListener(this);
//        autoCompProduct.setThreshold(0);
//        autoCompProduct.setAdapter(mAdapter);
//    }

//    /**
//     * Verify if client and date are selected
//     *
//     * @return true if configured client and date
//     */
//    public boolean isConfigured() {
//        if (currentClient == null) {
//            return false;
//            // } else if (date == null) {
//            // return false;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                saveSales();
//                this.finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        setupAutoCompleteClient();
//        super.onActivityResult(requestCode, resultCode, data);
//    }


}
