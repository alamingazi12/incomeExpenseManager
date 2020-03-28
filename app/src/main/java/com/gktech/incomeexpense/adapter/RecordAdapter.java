package com.gktech.incomeexpense.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.gktech.incomeexpense.R;
import com.gktech.incomeexpense.UpdateActivity;
import com.gktech.incomeexpense.database.DatabaseHelper;
import com.gktech.incomeexpense.model.Accounts;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>{

    Context context;
    DatabaseHelper databaseHelper;

    public RecordAdapter(Context context, ArrayList<Accounts> accountsrecords) {
        databaseHelper=new DatabaseHelper(context);
        this.context = context;
        this.accountsrecords = accountsrecords;
    }

    ArrayList<Accounts> accountsrecords;
    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.record_item_row,parent,false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordViewHolder holder, int position) {

         final Accounts accounts=accountsrecords.get(position);
         if(position%2==0){
             holder.linearLayout.setBackgroundColor(Color.parseColor("#f3e5f5"));
         }
         holder.rtdescription.setText(accounts.getDescription());
         holder.reportdate.setText(accounts.getDate());
         holder.adddate.setText(accounts.getTodaydate());
         holder.reportransaction.setText(accounts.getTtype());
         holder.rtamount.setText(accounts.getAmount());
         holder.report_edit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 PopupMenu popup = new PopupMenu(context, holder.report_edit);
                 //inflating menu from xml resource
                 popup.inflate(R.menu.options_menu);
                 //adding click listener
                 popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem item) {
                         switch (item.getItemId()) {
                             case R.id.menu_update:
                                 updateItem(accounts.getId());
                                 break;
                             case R.id.menu_delete:
                                 deleteItem(accounts.getId(),accounts);
                                 break;
                         }
                         return false;
                     }
                 });
                 popup.show();
             }
         });

    }

    private void deleteItem(String id, Accounts accounts) {
        databaseHelper.deleteData(id);
        accountsrecords.remove(accounts);
        this.notifyDataSetChanged();
    }

    private void updateItem(String id) {
        Intent intent=new Intent(context, UpdateActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return accountsrecords.size();
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView rtdescription,reportdate,adddate,reportransaction,rtamount;
        ImageView report_edit;
        LinearLayout linearLayout;
        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            rtdescription=itemView.findViewById(R.id.rtdescription);
            reportdate=itemView.findViewById(R.id.rtdate);
            adddate=itemView.findViewById(R.id.rtondate);
            reportransaction=itemView.findViewById(R.id.rttranstype);
            rtamount=itemView.findViewById(R.id.reportamount);
            report_edit=itemView.findViewById(R.id.editview);
            linearLayout=itemView.findViewById(R.id.item_row_layout);
        }
    }
}
