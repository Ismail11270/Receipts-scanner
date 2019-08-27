package com.zoobie.android.myapplication.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.market.data.Receipt;
import com.zoobie.android.myapplication.market.shops.Currency;
import com.zoobie.android.myapplication.market.shops.ShopsData;

import java.util.ArrayList;

public class ReceiptsListAdapter extends RecyclerView.Adapter<ReceiptsListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Receipt> receiptsList;
    private SharedPreferences settingsDataSp;

    public ReceiptsListAdapter(Context context, ArrayList<Receipt> receipts) {
        this.context = context;
        this.receiptsList = receipts;
        this.settingsDataSp = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_reciepts,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receipt receipt = receiptsList.get(position);

        String nameAndAdress = ShopsData.getShopName(receipt.getSTORE_ID()) + ", " + receipt.getAddress();
        holder.storeNameAddressTextView.setText(nameAndAdress);

        String dateTime[] = receipt.getDate().toString().split(".");

        holder.receiptDateTextView.setText(dateTime[0]);

        Currency currency = Currency.getCurrencyFromId(settingsDataSp.getInt("currency",1));
        String totalAmount = receipt.getTotal() + " " + currency.getDisplayName();
        holder.receiptTotalTextView.setText(totalAmount);
        
        holder.itemView.setOnClickListener(view -> {
            Toast.makeText(context, "pressed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return receiptsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView storeNameAddressTextView, receiptDateTextView, receiptTotalTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeNameAddressTextView = itemView.findViewById(R.id.storeName);
            receiptDateTextView = itemView.findViewById(R.id.receiptDate);
            receiptTotalTextView = itemView.findViewById(R.id.receiptTotalAmount);
            
        }
    }

}
