package com.zoobie.android.myapplication.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.activity.MainActivity;
import com.zoobie.android.myapplication.market.data.Product;
import com.zoobie.android.myapplication.market.data.Receipt;
import com.zoobie.android.myapplication.market.shops.Market;
import com.zoobie.android.myapplication.market.shops.ShopsData;
import com.zoobie.android.myapplication.storage.ProductsDB;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DialogHandler {

    private Context context;

    public DialogHandler(Context context) {
        this.context = context;
    }



    public Dialog showEditProductDialog(Product product){
        Dialog editProductDataDialog = new Dialog(context);


        editProductDataDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editProductDataDialog.setCancelable(true);
        editProductDataDialog.setContentView(R.layout.edit_product_data_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(editProductDataDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView dialogTitle = editProductDataDialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.dialog_title_product_data);

        ImageButton cancelBtn = editProductDataDialog.findViewById(R.id.dialogCancelBtn);
        Button saveBtn = editProductDataDialog.findViewById(R.id.dialogSaveBtn);


        EditText productNameEditText = editProductDataDialog.findViewById(R.id.productNameEditText);
        productNameEditText.setText(product.getName());

        EditText priceEditText = editProductDataDialog.findViewById(R.id.priceEditText);
        String price = Float.toString(product.getPrice());
        priceEditText.setText(price);

        EditText amountEditText = editProductDataDialog.findViewById(R.id.amountEditText);
        String amount = Float.toString(product.getAmount());
        amountEditText.setText(amount);


        cancelBtn.setOnClickListener(v -> editProductDataDialog.dismiss());

        saveBtn.setOnClickListener(v -> {
            product.setAmount(Float.parseFloat(amountEditText.getText().toString()));
            product.setName(productNameEditText.getText().toString());
            product.setPrice(Float.parseFloat(priceEditText.getText().toString()));
            editProductDataDialog.dismiss();
        });


        editProductDataDialog.show();
        editProductDataDialog.getWindow().setAttributes(lp);
        return editProductDataDialog;
    }

//    public Receipt showSaveReceiptDialog(Market market,ArrayList<Product> products){
//        return showSaveOrUpdateDialog(market,true,null,products);
//    }

//    public Receipt showUpdateReceiptDialog(Market market,Receipt receipt,ArrayList<Product> products){
//        return showSaveOrUpdateDialog(market,false,receipt,products);
//    }

    public Dialog showSaveOrUpdateDialog(Market market, boolean isNewReceipt, Receipt receipt, ArrayList<Product> products) {
        Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_new_purchase);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        String title = "New Receipt";
        String storeName = ShopsData.getShopName(market.getId());
        String address = "";
        String description = "";
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR);
        int mMinute = calendar.get(Calendar.MINUTE);

        if(!isNewReceipt){
            title = "Updating Receipt";
            address = market.getAddress();
            description = receipt.getDescription();

            calendar.setTimeInMillis(receipt.getDate().getTime());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR);
            mMinute = calendar.get(Calendar.MINUTE);
        }

        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(title);


        ImageButton newDateBtn = dialog.findViewById(R.id.editDateBtn);
        ImageButton newTimeBtn = dialog.findViewById(R.id.editTimeBtn);

        TextView dateTextView = dialog.findViewById(R.id.newPurchaseDateTv);
        TextView timeTextView = dialog.findViewById(R.id.newPurchaseTimeTv);

        EditText commentEditText = dialog.findViewById(R.id.newPurchaseComment);
        EditText nameEditText = dialog.findViewById(R.id.newPurchaseShopName);
        EditText addressEditText = dialog.findViewById(R.id.newPurchaseShopAddress);
        commentEditText.setText(description);
        addressEditText.setText(address);
        Button saveBtn = dialog.findViewById(R.id.dialogSaveBtn);
        ImageButton cancelBtn = dialog.findViewById(R.id.dialogCancelBtn);

        timeTextView.setText(mHour + ":" + mDay);

        dateTextView.setText(mDay + "-" + mMonth + "-" + mYear);

        if(!isNewReceipt){
            nameEditText.setEnabled(false);
            addressEditText.setEnabled(false);
        }
        commentEditText.setHint("Write a description of the purchase here...");
        nameEditText.setText(ShopsData.getShopName(market.getId()));

        saveBtn.setOnClickListener(view -> {
            String dateString[] = dateTextView.getText().toString().split("[-]");
            String timeString[] = timeTextView.getText().toString().split("[:]");
            int hour = Integer.parseInt(timeString[0]);
            int minute = Integer.parseInt(timeString[1]);
            int year = Integer.parseInt(dateString[2]);
            int month = Integer.parseInt(dateString[1]);
            int day = Integer.parseInt(dateString[0]);
            calendar.set(year, month, day, hour, minute);
            Timestamp date = new Timestamp(calendar.getTimeInMillis());
            System.out.println(market.getId() + " hererererere");
            Receipt newReceipt = new Receipt(
                    0, market.getId(), date, addressEditText.getText().toString(),
                    commentEditText.getText().toString(), products);
            System.out.println(products.toString());
            ProductsDB db = new ProductsDB(context);
            if(isNewReceipt) {
                db.addNewShopping(newReceipt);
            }else{
                newReceipt.setRowid(receipt.getRowid());

//                db.addNewShopping(receipt);
                db.updateReceiptData(newReceipt);
            }
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("page", 1);
            context.startActivity(intent);
        });
        cancelBtn.setOnClickListener(view -> dialog.dismiss());
        newDateBtn.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (datePicker, year, month, day) -> {
                        String date1 = day + "-" + month + "-" + year;
                        dateTextView.setText(date1);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        });
        newTimeBtn.setOnClickListener(view -> {
            int Hour = calendar.get(Calendar.HOUR);
            int Minute = calendar.get(Calendar.MINUTE);

            new TimePickerDialog(context,
                    (timePicker, hour, minute) -> {
                        String time1 = hour + ":" + minute;
                        timeTextView.setText(time1);
                    }, Hour, Minute, true).show();
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }
}
