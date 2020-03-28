package com.gktech.incomeexpense.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gktech.incomeexpense.CurrencyActivity;
import com.gktech.incomeexpense.MainActivity;
import com.gktech.incomeexpense.R;
import com.gktech.incomeexpense.database.DatabaseHelper;
import com.gktech.incomeexpense.fragment.DatePickerFragment;
import com.gktech.incomeexpense.model.Currency;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {
    Button ok_button,cancel_button;
    ArrayList<Currency> currencies;
    Context context;
  DatabaseHelper databaseHelper;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CurrencyAdapter(ArrayList<Currency> currencies, Context context) {
        this.currencies = currencies;
        this.context = context;
        databaseHelper=new DatabaseHelper(context);
    }


    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_currency,parent,false);
        return new CurrencyViewHolder(view,mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, final int position) {
       final Currency currency=currencies.get(position);
        holder.currency.setText(currency.getMcurrency());
        holder.currencyname.setText(currency.getCName());
        holder.country.setText(currency.getCountry());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(currency.getMcurrency());
               // Log.d("selected Item",currencies.get(position).getMcurrency());
            }
        });

    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public class CurrencyViewHolder extends RecyclerView.ViewHolder {

        private TextView currencyname,currency,country;
        Button ok_button,ok_cancel;
        public CurrencyViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            currencyname=itemView.findViewById(R.id._curr_name);
            currency=itemView.findViewById(R.id._currency);
            country=itemView.findViewById(R.id._curr_country);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }



    private void showDialog(final String mcurrency) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View  Dialogview =LayoutInflater.from(context).inflate(R.layout.layout_dialog_currency,null);
        // Button button=Dialogview.findViewById(R.id.saveincome);
        ok_button=Dialogview.findViewById(R.id.selectok);
        cancel_button=Dialogview.findViewById(R.id.selectcancel);
        builder.setView(Dialogview);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences= context.getSharedPreferences("Currency",Context.MODE_PRIVATE);
                String value=sharedPreferences.getString("cvalue",null);
                //Toast.makeText(context,"button clicked"+mcurrency,Toast.LENGTH_LONG).show();
                databaseHelper.update_currency("1",mcurrency);
                MainActivity.editor.putString("cvalue",databaseHelper.fetch_Currency());
                MainActivity.editor.apply();
                dialog.dismiss();
                Intent intent=new Intent(context,MainActivity.class);
                context.startActivity(intent);
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context,"cancel clicked",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void filterList(ArrayList<Currency> filteredList) {
        currencies = filteredList;
        notifyDataSetChanged();
    }
}
