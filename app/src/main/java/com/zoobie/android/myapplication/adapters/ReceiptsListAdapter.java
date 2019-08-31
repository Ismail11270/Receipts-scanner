package com.zoobie.android.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zoobie.android.myapplication.activity.MainActivity;
import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.activity.ReceiptScanner;
import com.zoobie.android.myapplication.market.data.Receipt;
import com.zoobie.android.myapplication.market.shops.ShopsData;
import com.zoobie.android.myapplication.storage.ProductsDB;

import java.util.ArrayList;

public class ReceiptsListAdapter extends RecyclerView.Adapter<ReceiptsListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Receipt> receiptsList;
    private ProductsDB db;
    private RelativeLayout noItemLayout;
    private FrameLayout bottomSheetView;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetDialog bottomSheetDialog;

    public ReceiptsListAdapter(Context context, ArrayList<Receipt> receipts, RelativeLayout noItemLayout, FrameLayout bottomSheetView) {
        this.context = context;
        this.receiptsList = receipts;
        this.noItemLayout = noItemLayout;
        this.bottomSheetView = bottomSheetView;
        this.bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        db = new ProductsDB(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_reciepts, parent, false);
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
        holder.receiptListItem.setOnLongClickListener(view -> {
            Toast.makeText(context, "RECEIPT REMOVED!", Toast.LENGTH_SHORT).show();
            deleteReceipt(receipt, position);
            return true;
        });
        holder.receiptListItem.setOnClickListener(view -> {
            showBottomDialogForReceipt(receipt, position);
        });
    }

    private void deleteReceipt(Receipt receipt, int position) {
        receiptsList.remove(position);
        db.removeReceipt(receipt);
        if (position == 0) {
            noItemLayout.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    private void showBottomDialogForReceipt(Receipt receipt, int position) {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.receipts_bottom_sheet, null);
        ((TextView) view.findViewById(R.id.shopNameTextView)).setText(ShopsData.getShopName(receipt.getSTORE_ID()));
        if (receipt.getAddress().length() > 0)
            ((TextView) view.findViewById(R.id.addressTextView)).setText(receipt.getAddress());

        String total = "Total: " + receipt.getTotal() + MainActivity.currency.getDisplayName();
        ((TextView) view.findViewById(R.id.totalTextView)).setText(total);
        if (receipt.getDescription().length() > 0)
            ((TextView) view.findViewById(R.id.commentTextView)).setText(receipt.getDescription());

        ((Button) view.findViewById(R.id.viewBtn)).setOnClickListener(v -> {
            Toast.makeText(context, "Loading receipt data", Toast.LENGTH_SHORT).show();
            Intent loadReceiptIntent = new Intent(context, ReceiptScanner.class);
            System.out.println(receipt.getRowid() + " row id");
            loadReceiptIntent.putExtra("receipt_id", receipt.getRowid());
            loadReceiptIntent.putExtra("new", false);
            context.startActivity(loadReceiptIntent);
        });

        ((Button) view.findViewById(R.id.deleteBtn)).setOnClickListener(v -> {
            deleteReceipt(receipt, position);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(dialogInterface -> bottomSheetDialog = null);
    }


    @Override
    public int getItemCount() {
        return receiptsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
