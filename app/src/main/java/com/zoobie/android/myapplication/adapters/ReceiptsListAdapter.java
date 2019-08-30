package com.zoobie.android.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zoobie.android.myapplication.activity.MainActivity;
import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.activity.ReceiptScanner;
import com.zoobie.android.myapplication.market.data.Product;
import com.zoobie.android.myapplication.market.data.Receipt;
import com.zoobie.android.myapplication.market.shops.ShopsData;

import java.util.ArrayList;

public class ReceiptsListAdapter extends RecyclerView.Adapter<ReceiptsListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Receipt> receiptsList;

    public ReceiptsListAdapter(Context context, ArrayList<Receipt> receipts) {
        this.context = context;
        this.receiptsList = receipts;
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

        String dateTime[] = receipt.getDate().toString().split("[.]");

        holder.receiptDateTextView.setText(dateTime[0]);


        String totalAmount = receipt.getTotal() + " " + MainActivity.currency.getDisplayName();
        holder.receiptTotalTextView.setText(totalAmount);
        holder.receiptListItem.setOnClickListener(view -> {
            Toast.makeText(context, "Loading receipt data", Toast.LENGTH_SHORT).show();
            Intent loadReceiptIntent = new Intent(context,ReceiptScanner.class);
            System.out.println(receipt.getRowid() + " row id");
            loadReceiptIntent.putExtra("receipt_id",receipt.getRowid());
            loadReceiptIntent.putExtra("new",false);
            context.startActivity(loadReceiptIntent);
        });
    }

    @Override
    public int getItemCount() {
        return receiptsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout receiptListItem;
        TextView storeNameAddressTextView, receiptDateTextView, receiptTotalTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeNameAddressTextView = itemView.findViewById(R.id.storeName);
            receiptDateTextView = itemView.findViewById(R.id.receiptDate);
            receiptTotalTextView = itemView.findViewById(R.id.receiptTotalAmount);
            receiptListItem = itemView.findViewById(R.id.receiptListElement);
        }
    }

}
