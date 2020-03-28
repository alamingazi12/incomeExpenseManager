package com.gktech.incomeexpense.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gktech.incomeexpense.R;
import com.gktech.incomeexpense.model.Accounts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MoreViewHolder> {
    TextView more_currency;
    ArrayList<Accounts> accountsArrayList;
    SharedPreferences sharedPreferences;
    public MoreAdapter(ArrayList<Accounts> accountsArrayList, Context context) {
        this.accountsArrayList = accountsArrayList;
        this.context = context;
    }

    Context context;
    @NonNull
    @Override
    public MoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.more_item_row,parent,false);
        return new MoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreViewHolder holder, int position) {
        Date date1;

        Accounts accounts=accountsArrayList.get(position);

        if(accounts.getCode().equals("2")){
            more_currency.setTextColor(Color.parseColor("#d32f2f"));
            holder.mamount.setTextColor(Color.parseColor("#d32f2f"));

        }
        sharedPreferences=context.getSharedPreferences("Currency",Context.MODE_PRIVATE);
        more_currency.setText(sharedPreferences.getString("cvalue",null));
        String date=accounts.getDate();
        String datearr[]=date.split("-");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");

        try{
            date1= simpleDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("EEE");
            String  weekday= simpleDateFormat1.format(date1);
            Toast.makeText(context,""+weekday,Toast.LENGTH_LONG).show();
            holder.weekday.setText(weekday);
            Log.d("weekday",weekday);
            if(weekday.equals("Sat")){
                holder.relativemonthweekday.setBackgroundColor(Color.parseColor("#c6ff00"));
            }
            else if(weekday.equals("Sun")){
                holder.relativemonthweekday.setBackgroundColor(Color.parseColor("#ffab00"));
            }
            else if(weekday.equals("Mon")){
                holder.relativemonthweekday.setBackgroundColor(Color.parseColor("#ff8a80"));
            }
            else if(weekday.equals("Tue")){
                holder.relativemonthweekday.setBackgroundColor(Color.parseColor("#7b1fa2"));
            }
            else if(weekday.equals("Wed")){
                holder.relativemonthweekday.setBackgroundColor(Color.parseColor("#7b1fa2"));
            }
            else if(weekday.equals("Thu")){
                holder.relativemonthweekday.setBackgroundColor(Color.parseColor("#7b1fa2"));
            }
            else if(weekday.equals("Fri")){
                holder.relativemonthweekday.setBackgroundColor(Color.parseColor("#ff3d00"));
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        holder.dayofmonth.setText(datearr[0]);
        holder.mdescription.setText(accounts.getDescription());
        holder.transtype.setText(accounts.getTtype());
        holder.mamount.setText(accounts.getAmount());
    }

    @Override
    public int getItemCount() {
        return accountsArrayList.size();
    }
    public class MoreViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout relativemonthweekday;
        TextView mdescription,transtype,mamount,weekday,dayofmonth;
        public MoreViewHolder(@NonNull View itemView) {
            super(itemView);

            mdescription=itemView.findViewById(R.id.mdescription);
            transtype=itemView.findViewById(R.id.mttypetransaction);
            mamount=itemView.findViewById(R.id.mamount);
            weekday=itemView.findViewById(R.id.mwday);
            dayofmonth=itemView.findViewById(R.id.mday);
            relativemonthweekday=itemView.findViewById(R.id.mdatelayout);
            more_currency=itemView.findViewById(R.id.more_currency);

        }
    }
}
