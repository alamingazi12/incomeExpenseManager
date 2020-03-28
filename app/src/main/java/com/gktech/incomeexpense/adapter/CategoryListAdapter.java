package com.gktech.incomeexpense.adapter;

import android.content.Context;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gktech.incomeexpense.R;
import com.gktech.incomeexpense.database.DatabaseHelper;
import com.gktech.incomeexpense.model.Accounts;
import com.gktech.incomeexpense.model.Category;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    SharedPreferences sharedPreferences;
    DatabaseHelper databaseHelper;
    TextView category_currency;

    public CategoryListAdapter(String month, Context context, ArrayList<Category> categories) {
        databaseHelper=new DatabaseHelper(context);
        this.month = month;
        this.context = context;
        this.categories = categories;
    }

    String month;
    Context context;
    ArrayList<Category> categories;

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.category_sum_row,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        sharedPreferences=context.getSharedPreferences("Currency",Context.MODE_PRIVATE);

        Category category=categories.get(position);
        category_currency.setText(sharedPreferences.getString("cvalue",null));

        if(category.getCode().equals("1")){
            holder.viewcategory.setBackground(ContextCompat.getDrawable(context, R.drawable.view_backgorund_income));
        }
        int sum= databaseHelper.getCategorySum(category.getCname(),month);

        if(sum>0 && category.getCode().equals("2")){

            holder.viewcategory.setBackground(ContextCompat.getDrawable(context, R.drawable.view_backgrund_expense));
        }
            holder.camount.setText(String.valueOf(sum));
        holder.categoryname.setText(category.getCname());
        holder.categoryimage.setImageResource(category.getImage());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryimage;
        View viewcategory;
        TextView categoryname,camount;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryimage=itemView.findViewById(R.id.cimage);
            categoryname=itemView.findViewById(R.id.listcategoryname);
            camount=itemView.findViewById(R.id.camount);
            viewcategory=itemView.findViewById(R.id.categoryview);
            category_currency=itemView.findViewById(R.id.currency_category);
        }
    }
}
