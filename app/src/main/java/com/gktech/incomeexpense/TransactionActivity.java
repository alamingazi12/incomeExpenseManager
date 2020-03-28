package com.gktech.incomeexpense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.gktech.incomeexpense.adapter.CategoryListAdapter;
import com.gktech.incomeexpense.adapter.MoreThreeAdapter;
import com.gktech.incomeexpense.database.DatabaseHelper;
import com.gktech.incomeexpense.fragment.DatePickerFragment;
import com.gktech.incomeexpense.model.Category;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{
    TextView datetext,incomeDescription,incomeAmount,savingCurrency,tsaving;
  public View  dialogview;
    DatabaseHelper databaseHelper;
    ArrayList<Category> categories;
    SharedPreferences sharedPreferences;
    String month,ptype,category;
    public static   int income=0,expense=0,savings=0;
    RecyclerView categorySumlistContainer,transactioncontainer3item;
    TextView incometext,expensetext,showmoretext,currency_expense,currency_income;
    InterstitialAd mInterstitialAd;
    Spinner payment_spinner,category_spinner;
    SharedPreferences savemonth;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

               sharedPreferences=TransactionActivity.this.getSharedPreferences("Currency", Context.MODE_PRIVATE);

               month= getIntent().getStringExtra("month");
               income= getIntent().getIntExtra("income",0);
               expense= getIntent().getIntExtra("expense",0);
               savings= getIntent().getIntExtra("savings",0);

               initAll();

               showMoreData();
               showCategoryListsum();
               initFloatingButton();

    }


    private void initFloatingButton() {
        FloatingActionButton floatingActionButtonexpense=findViewById(R.id.fabexpense);
        floatingActionButtonexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TransactionActivity.this,AddExpenseActivity.class);
                intent.putExtra("activity","2");
                startActivity(intent);
                Toast.makeText(TransactionActivity.this,"expense Clicked",Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton floatingActionButtonincome=findViewById(R.id.fabincome);
        floatingActionButtonincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showMoreData() {
        SharedPreferences sharedPreferencesmonth=TransactionActivity.this.getSharedPreferences("month",MODE_PRIVATE);
        MoreThreeAdapter moreAdapter=new MoreThreeAdapter((databaseHelper.displayAcccountData3(sharedPreferencesmonth.getString("monthName",null))),TransactionActivity.this);
        transactioncontainer3item.setAdapter(moreAdapter);
    }






    private void initAll() {

        databaseHelper=new DatabaseHelper(TransactionActivity.this);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        currency_income=findViewById(R.id.tincome_currency);
        currency_income.setText(sharedPreferences.getString("cvalue",null));
        currency_expense=findViewById(R.id.expense_currency);
        currency_expense.setText(sharedPreferences.getString("cvalue",null));
        incometext=findViewById(R.id.incomestext);
        expensetext=findViewById(R.id.expensetext);
        savingCurrency=findViewById(R.id.tcurrency);
        tsaving=findViewById(R.id.tsavings);

        savingCurrency.setText(sharedPreferences.getString("cvalue",null));

        SharedPreferences sharedPreferencesmonth=TransactionActivity.this.getSharedPreferences("month",MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(sharedPreferencesmonth.getString("monthName",null)+"-"+"2020");


        int incomeexpense=  databaseHelper.getIncomeExpense(sharedPreferencesmonth.getString("monthName",null));

        final int income=databaseHelper.getIncome(sharedPreferencesmonth.getString("monthName",null),"1");
        incometext.setText(" "+String.valueOf(income));
        int expenses=incomeexpense-income;
        tsaving.setText(String.valueOf(income-expenses));

        expensetext.setText(" "+String.valueOf(incomeexpense-income));
        showmoretext=findViewById(R.id.showmoretext);


        showmoretext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TransactionActivity.this, MoreActivity.class);
                intent.putExtra("month",month);
                intent.putExtra("income",income);
                intent.putExtra("expense",expense);
                startActivity(intent);
            }
        });

        categories=new ArrayList<>();
        categorySumlistContainer=findViewById(R.id.typesumlistcontainer);
        transactioncontainer3item=findViewById(R.id.recordcontainer3item);
        transactioncontainer3item.setHasFixedSize(true);
        transactioncontainer3item.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
        categorySumlistContainer.setHasFixedSize(true);
        categorySumlistContainer.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
    }

    @Override
    protected void onResume() {


        SharedPreferences sharedPreferencesmonth=TransactionActivity.this.getSharedPreferences("month",MODE_PRIVATE);
        int incomeexpense=  databaseHelper.getIncomeExpense(sharedPreferencesmonth.getString("monthName",null));
        final int income=databaseHelper.getIncome(sharedPreferencesmonth.getString("monthName",null),"1");
        incometext.setText(" "+String.valueOf(income));
        expensetext=findViewById(R.id.expensetext);
        expensetext.setText(" "+String.valueOf(incomeexpense-income));
        int expense=incomeexpense-income;
        tsaving.setText(String.valueOf(income-expense));

        super.onResume();
    }

    private void showCategoryListsum() {
        categories.add(new Category("Household",R.drawable.household,"2"));
        categories.add(new Category("Savings",R.drawable.taxi,"1"));
        categories.add(new Category("Loan",R.drawable.eating,"2"));
        categories.add(new Category("Shopping",R.drawable.shopping,"2"));
        categories.add(new Category("Salary",R.drawable.taxi,"1"));
        categories.add(new Category("Family",R.drawable.grocery,"2"));
        categories.add(new Category("Education",R.drawable.education,"2"));
        categories.add(new Category("Personal",R.drawable.personal,"2"));
        categories.add(new Category("Pensions",R.drawable.taxi,"1"));
        categories.add(new Category("Business",R.drawable.taxi,"1"));
        categories.add(new Category("Utilities",R.drawable.utilities,"2"));
        categories.add(new Category("Other",R.drawable.other,"2"));
        categories.add(new Category("Medical",R.drawable.medical,"2"));
        categories.add(new Category("Transport",R.drawable.personal,"2"));
        categories.add(new Category("Rent & Royelties",R.drawable.taxi,"1"));
        categories.add(new Category("Vacation",R.drawable.entertainment,"2"));
        categories.add(new Category("Fuel",R.drawable.fuel,"2"));

        SharedPreferences sharedPreferencesmonth=TransactionActivity.this.getSharedPreferences("month",MODE_PRIVATE);
        CategoryListAdapter categoryListAdapter=new CategoryListAdapter(sharedPreferencesmonth.getString("monthName",null),TransactionActivity.this,categories);
        categorySumlistContainer.setAdapter(categoryListAdapter);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        String currentDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        String datearr[]=currentDate.split("/");
        String monthdate=datearr[0];
        String day=datearr[1];
        String years="20"+datearr[2];
       // String fdate=day+"-"+monthdate+"-"+years;
        String sday= String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String smonth= String.valueOf(c.get(Calendar.MONTH)+1);
        String syear= String.valueOf(c.get(Calendar.YEAR));
        String fdate=sday+"-"+smonth+"-"+syear;

        datetext.setText(fdate);

    }


    public  void showDialog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        dialogview =this.getLayoutInflater().inflate(R.layout.layout_dialog,null);
        Button button_save=dialogview.findViewById(R.id.save_income);
        Button button_cancel=dialogview.findViewById(R.id.save_cancel);
        ImageView calendarpic=dialogview.findViewById(R.id.datepicimage);
        incomeDescription=dialogview.findViewById(R.id.description_income);
        incomeAmount=dialogview.findViewById(R.id.amount_income);
        datetext = dialogview.findViewById(R.id.datetext);

        category_spinner=dialogview.findViewById(R.id.spinner_category_income);
        payment_spinner=dialogview.findViewById(R.id.spinner_paymenttype_income);
        payment_spinner.setOnItemSelectedListener(this);
        category_spinner.setOnItemSelectedListener(this);
        List<String> paymentlist = new ArrayList<String>();
        paymentlist.add("Payment Type");
        paymentlist.add("Cash");
        paymentlist.add("Debit Card");
        paymentlist.add("Credit Card");
        paymentlist.add("Net Banking");
        paymentlist.add("Electronic Transfer");

        List<String> categorylist = new ArrayList<String>();
        categorylist.add("Salary");
        categorylist.add("Savings");
        categorylist.add("Pensions");
        categorylist.add("Business");
        categorylist.add("Rent & Royelties");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentlist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payment_spinner.setAdapter(dataAdapter);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorylist);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(categoryAdapter);

        builder.setView(dialogview);
       dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        calendarpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIncome();

              //  Toast.makeText(TransactionActivity.this,"button clicked",Toast.LENGTH_LONG).show();

            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
                //  Toast.makeText(TransactionActivity.this,"button clicked",Toast.LENGTH_LONG).show();

            }
        });
        dialog.show();
    }



    public void saveIncome(){
        String incomeamount=incomeAmount.getText().toString();
        String  description=incomeDescription.getText().toString();
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy, HH:mm");
        String today=sdf.format(date);
        String pdate=datetext.getText().toString();
        String pdatarr[]=pdate.split("-");

        if(pdate.equals("Select Date") || incomeamount.isEmpty() || description.isEmpty() || ptype.isEmpty()){
            Toast.makeText(TransactionActivity.this,"One or More Fields Empty",Toast.LENGTH_LONG).show();
        }else {

            if(databaseHelper.checkMonth(getMonthString(pdatarr[1]),pdatarr[2])){
                long id=  databaseHelper.insertallAccounts(description,pdate,today,category,Integer.parseInt(incomeamount),getMonthString(pdatarr[1]),pdatarr[2],"1",ptype);
                Toast.makeText(TransactionActivity.this,"Inserted Successfully",Toast.LENGTH_LONG).show();
                onResume();
                showMoreData();
                dialog.dismiss();

                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else {

                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            }else {
                Toast.makeText(TransactionActivity.this,"data not  exists",Toast.LENGTH_LONG).show();
                long id=  databaseHelper.insertallAccounts(description,pdate,today,category,Integer.parseInt(incomeamount),getMonthString(pdatarr[1]),pdatarr[2],"1",ptype);
                databaseHelper.insertMonth(getMonthInt(pdatarr[1]),pdatarr[2],getMonthString(pdatarr[1]));
                dialog.dismiss();
                Toast.makeText(TransactionActivity.this,"Inserted Successfully",Toast.LENGTH_LONG).show();
                onResume();

                showMoreData();
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        }



    }


    public int getMonthInt(String months){
        int month=0;

        switch (months){

            case "1":
                month=1;
                break;
            case "2":
                month=2;
                break;
            case "3":
                month=3;
                break;
            case "4":
                month=4;
                break;
            case "5":
                month=5;
                break;
            case "6":
                month=6;
                break;
            case "7":
                month=7;
                break;
            case "8":
                month=8;
                break;
            case "9":
                month=9;
                break;
            case "10":
                month=10;
                break;
            case "11":
                month=11;
                break;
            case "12":
                month=12;
                break;

        }

        return month;
    }



    private String getMonthString(String s) {

        String monthName=null;

        switch (s){
            case "1":
                monthName="January";
                break;
            case "2":
                monthName="February";
                break;
            case "3":
                monthName="March";
                break;
            case "4":
                monthName="April";
                break;
            case "5":
                monthName="May";
                break;
            case "6":
                monthName="June";
                break;
            case "7":
                monthName="July";
                break;
            case "8":
                monthName="August";
                break;
            case "9":
                monthName="September";
                break;
            case "10":
                monthName="October";
                break;
            case "11":
                monthName="November";
                break;
            case "12":
                monthName="December";
                break;

        }
        return monthName;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Log.d("items",item);
        ptype=item;

        switch (parent.getId()) {
            case R.id.spinner_paymenttype_income:
                ptype = payment_spinner.getSelectedItem().toString();
                Toast.makeText(TransactionActivity.this, "payment Type:" + ptype, Toast.LENGTH_LONG).show();
                break;
            case R.id.spinner_category_income:
                category = category_spinner.getSelectedItem().toString();
                Toast.makeText(TransactionActivity.this, "category Type:" + category, Toast.LENGTH_LONG).show();
        }
        // Showing selected spinner item
        //   Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
