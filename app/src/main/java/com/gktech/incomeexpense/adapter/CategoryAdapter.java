package com.gktech.incomeexpense.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gktech.incomeexpense.AddExpenseActivity;
import com.gktech.incomeexpense.MainActivity;
import com.gktech.incomeexpense.R;
import com.gktech.incomeexpense.UpdateActivity;
import com.gktech.incomeexpense.model.Category;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.gktech.incomeexpense.R.*;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
     int index=-1;
    Context context;
    ArrayList<Category> categories;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(layout.categories_item,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {

         final Category category=categories.get(position);
        holder.caName.setText(category.getCname());
        holder.caImage.setImageResource(category.getImage());

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=position;
                notifyDataSetChanged();
            }
        });
        if(index==position){
            holder.layout_item.setBackgroundColor(Color.parseColor("#FFEB3B"));
            AddExpenseActivity.categoryname=category.getCname();
            UpdateActivity.categorynames=category.getCname();
           // Toast.makeText(context,"Name"+AddExpenseActivity.categoryname,Toast.LENGTH_SHORT).show();
        }
        else
        {
            holder.layout_item.setBackgroundColor(Color.parseColor("#ffffff"));

        }



    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_item;
        TextView caName;
        ImageView caImage;
        @SuppressLint("WrongViewCast")
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            caImage=itemView.findViewById(R.id.typeimage);
            caName=itemView.findViewById(R.id.typenames);
            layout_item=itemView.findViewById(id.citem_layout);
        }
    }
}
