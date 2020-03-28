package com.gktech.incomeexpense.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gktech.incomeexpense.AddExpenseActivity;
import com.gktech.incomeexpense.MainActivity;
import com.gktech.incomeexpense.MoreActivity;
import com.gktech.incomeexpense.R;
import com.gktech.incomeexpense.RecordActivity;
import com.gktech.incomeexpense.TransactionActivity;
import com.gktech.incomeexpense.database.DatabaseHelper;
import com.gktech.incomeexpense.model.Months;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyContentViewHolder> {

      Context context;
      DatabaseHelper databaseHelper;

    public ContentAdapter(Context context, ArrayList<Months> monthlist, DatabaseHelper databaseHelper) {
        this.context = context;
        this.monthlist = monthlist;
        this.databaseHelper=databaseHelper;
    }

    ArrayList<Months> monthlist;

    @NonNull
    @Override
    public MyContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.month_item,parent,false);
        return new MyContentViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyContentViewHolder holder, int position) {
        //int expense=0;
       final Months month= monthlist.get(position);
       holder.monthtext.setText(month.getMonth());
       holder.yeartext.setText(month.getYear());

        int incomeexpense=  databaseHelper.getIncomeExpense(month.getMonth());
        final int income=databaseHelper.getIncome(month.getMonth(),"1");

      final  int expense=incomeexpense-income;
      final  int savings=income-expense;
      //  Log.d("months",month.getMonth());
        Log.d("months",String.valueOf(incomeexpense));
       // Log.d("months",String.valueOf(income));
        SharedPreferences sharedPreferences=context.getSharedPreferences("Currency",Context.MODE_PRIVATE);

      holder.mincome.setText(String.valueOf(income));
      holder.mexpense.setText(String.valueOf(expense));
      holder.saving.setText(String.valueOf(savings));
      holder.currencyincome.setText(sharedPreferences.getString("cvalue",null));
      holder.currencyexpense.setText(sharedPreferences.getString("cvalue",null));
      holder.saving_currency.setText(sharedPreferences.getString("cvalue",null));
      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              MainActivity.editor_month.putString("monthName",month.getMonth());
              MainActivity.editor_month.apply();
              Bundle bundle=new Bundle();
              Intent intent=new Intent(context, TransactionActivity.class);
              intent.putExtra("month",month.getMonth());
              intent.putExtra("income",income);
              intent.putExtra("expense",expense);
              intent.putExtra("savings",savings);
              context.startActivity(intent);

          }
      });


    }

    @Override
    public int getItemCount() {
        return monthlist.size();
    }

    public class MyContentViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativemonthweekday;
        TextView monthtext,yeartext,mincome,mexpense,currencyincome,currencyexpense,saving,saving_currency;
        public MyContentViewHolder(@NonNull View itemView) {
            super(itemView);
            monthtext=itemView.findViewById(R.id.maonthitem);
            yeartext=itemView.findViewById(R.id.yearitem);
            mincome=itemView.findViewById(R.id.mincome);
            mexpense=itemView.findViewById(R.id.mexpense);

            currencyexpense=itemView.findViewById(R.id.currencyexpense);
            currencyincome=itemView.findViewById(R.id.currencyincome);
            saving=itemView.findViewById(R.id.saving);
            saving_currency=itemView.findViewById(R.id.saving_currency);



        }
    }
}
