package com.zoobie.android.myapplication.adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zoobie.android.myapplication.MainActivity;
import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.market.data.Product;
import com.zoobie.android.myapplication.market.data.Shopping;
import com.zoobie.android.myapplication.market.shops.ShopsData;
import com.zoobie.android.myapplication.storage.ProductsDB;
import com.zoobie.android.myapplication.market.shops.Market;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditNewDataListAdapter extends RecyclerView.Adapter<EditNewDataListAdapter.ViewHolder> {


    private ArrayList<Product> products;
    private Context context;
    private Boolean editable = false;
    private Market market;

    public EditNewDataListAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_edit_reciept_data, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameEditText.setText(product.getName());
        holder.priceEditText.setText(product.getPrice() + "");
        holder.amountEditText.setText(product.getAmount() + "");


        holder.nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                products.get(position).setName(editable.toString());
            }
        });
        holder.priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    try {
                        products.get(position).setPrice(Float.parseFloat(editable.toString()));
                    }finally {

                    }
                }
            }
        });
        holder.amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0)
                    products.get(position).setAmount(Float.parseFloat(editable.toString()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public EditText nameEditText, amountEditText, priceEditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameEditText = itemView.findViewById(R.id.productNameEditText);
            amountEditText = itemView.findViewById(R.id.productAmountEditText);
            priceEditText = itemView.findViewById(R.id.productPriceEditText);
        }

    }

    public void saveData(Market market) {
        this.market = market;
        showSaveDialog();
    }

    //ToDO make scanner scan shop names etc
    private void showSaveDialog() {
        Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_new_purchase);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        String time = "", name = "", description = "";
        ImageButton newDateBtn = dialog.findViewById(R.id.editDateBtn);
        ImageButton newTimeBtn = dialog.findViewById(R.id.editTimeBtn);

        TextView dateTextView = dialog.findViewById(R.id.newPurchaseDateTv);
        TextView timeTextView = dialog.findViewById(R.id.newPurchaseTimeTv);

        EditText commentEditText = dialog.findViewById(R.id.newPurchaseComment);
        EditText nameEditText = dialog.findViewById(R.id.newPurchaseShopName);
        EditText addressEditText = dialog.findViewById(R.id.newPurchaseShopAddress);

        Button saveBtn = dialog.findViewById(R.id.newPurchaseSaveBtn);
        ImageButton cancelBtn = dialog.findViewById(R.id.newPurchaseCancelBtn);

        timeTextView.setText("06:30");
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        dateTextView.setText(mDay + "." + mMonth + "." + mYear);

        commentEditText.setHint("Write a description of the purchase here...");
        nameEditText.setText(ShopsData.getShopName(market.getId()));
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        saveBtn.setOnClickListener(view -> {
            String dateString[] = dateTextView.getText().toString().split("[.]");
            String timeString[] = timeTextView.getText().toString().split("[:]");
            int hour = Integer.parseInt(timeString[0]);
            int minute = Integer.parseInt(timeString[1]);
            int year = Integer.parseInt(dateString[2]);
            int month = Integer.parseInt(dateString[1]);
            int day = Integer.parseInt(dateString[0]);
            calendar.set(year, month, day, hour, minute);
            Timestamp date = new Timestamp(calendar.getTimeInMillis());
            Shopping shopping = new Shopping(
                    0, market.getId(), date, addressEditText.getText().toString(),
                    commentEditText.getText().toString(), products);
            ProductsDB db = new ProductsDB(context);
            db.addNewShopping(shopping);
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("page", 1);
            context.startActivity(intent);
        });
        cancelBtn.setOnClickListener(view -> dialog.dismiss());
        newDateBtn.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (datePicker, year, month, day) -> {
                        String date1 = day + "." + month + "." + year;
                        dateTextView.setText(date1);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        });
        newTimeBtn.setOnClickListener(view -> {
            int mHour = calendar.get(Calendar.HOUR);
            int mMinute = calendar.get(Calendar.MINUTE);

            new TimePickerDialog(context,
                    (timePicker, hour, minute) -> {
                        String time1 = hour + ":" + minute;
                        timeTextView.setText(time1);
                    }, mHour, mMinute, true).show();
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
