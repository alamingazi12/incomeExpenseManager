package com.gktech.incomeexpense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import com.gktech.incomeexpense.adapter.RecordAdapter;
import com.gktech.incomeexpense.database.DatabaseHelper;
import com.gktech.incomeexpense.fragment.DatePickerFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    DatabaseHelper databaseHelper;
    TextView send_to_sheet;
    TextView datetext,incomeDescription,incomeAmount;
    RecyclerView reportContainer;
    String month;
    int income,expense;
    private View dialogview;

    String ptype,category;

    InterstitialAd mInterstitialAd;
    AlertDialog dialog;
    Spinner payment_spinner,category_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        SharedPreferences sharedPreferencesmonth=RecordActivity.this.getSharedPreferences("month",MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(sharedPreferencesmonth.getString("monthName",null)+"-"+"2020");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        month=getIntent().getStringExtra("month");
        income= getIntent().getIntExtra("income",0);
        expense= getIntent().getIntExtra("expense",0);
        initAll();
        initializeReport();
        send_to_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecordActivity.this, InsertActivity.class);
                intent.putExtra("month",month);
                intent.putExtra("income",income);
                intent.putExtra("expense",expense);
                startActivity(intent);
            }
        });
      //  initFloatingButton();
    }

    private void initAll() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }


    private void initFloatingButton() {
        FloatingActionButton floatingActionButtonexpense=findViewById(R.id.fabexpense);
        floatingActionButtonexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecordActivity.this,AddExpenseActivity.class);
                startActivity(intent);
                Toast.makeText(RecordActivity.this,"expense Clicked",Toast.LENGTH_LONG).show();
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

    private void initializeReport() {
        send_to_sheet=findViewById(R.id.send_to_sheet);
        databaseHelper=new DatabaseHelper(RecordActivity.this);
        reportContainer=findViewById(R.id.reportlistcontainers);
        reportContainer.setHasFixedSize(true);
        reportContainer.setLayoutManager(new LinearLayoutManager(RecordActivity.this));
        SharedPreferences sharedPreferencesmonth=RecordActivity.this.getSharedPreferences("month",MODE_PRIVATE);
        RecordAdapter recordAdapter =new RecordAdapter(RecordActivity.this,databaseHelper.displayAcccountData(sharedPreferencesmonth.getString("monthName",null)));
        reportContainer.setAdapter(recordAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Log.d("items",item);
        ptype=item;

        switch (parent.getId()) {
            case R.id.spinner_paymenttype_income:
                ptype = payment_spinner.getSelectedItem().toString();
                Toast.makeText(RecordActivity.this, "payment Type:" + ptype, Toast.LENGTH_LONG).show();
                break;
            case R.id.spinner_category_income:
                category = category_spinner.getSelectedItem().toString();
                Toast.makeText(RecordActivity.this, "category Type:" + category, Toast.LENGTH_LONG).show();
        }
        // Showing selected spinner item
        //   Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIncome();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {

        SharedPreferences sharedPreferencesmonth=RecordActivity.this.getSharedPreferences("month",MODE_PRIVATE);
        RecordAdapter recordAdapter =new RecordAdapter(RecordActivity.this,databaseHelper.displayAcccountData(sharedPreferencesmonth.getString("monthName",null)));
        reportContainer.setAdapter(recordAdapter);
        super.onResume();
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
            Toast.makeText(RecordActivity.this,"One Or More Fields Empty",Toast.LENGTH_LONG).show();

        }else{
            if(databaseHelper.checkMonth(getMonthString(pdatarr[1]),pdatarr[2])){

                long id=  databaseHelper.insertallAccounts(description,pdate,today,category,Integer.parseInt(incomeamount),getMonthString(pdatarr[1]),pdatarr[2],"1",ptype);
                //  long id=  databaseHelper.insertallAccounts(description,pdate,today,"Income",Integer.parseInt(incomeamount),getMonthString(pdatarr[1]),pdatarr[2]);
                Toast.makeText(RecordActivity.this,"Inserted Successfully",Toast.LENGTH_LONG).show();
                dialog.dismiss();
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            }else {

                long id=  databaseHelper.insertallAccounts(description,pdate,today,category,Integer.parseInt(incomeamount),getMonthString(pdatarr[1]),pdatarr[2],"1",ptype);
                //   long id=  databaseHelper.insertallAccounts(description,pdate,today,"Income",Integer.parseInt(incomeamount),getMonthString(pdatarr[1]),pdatarr[2]);
                databaseHelper.insertMonth(getMonthInt(pdatarr[1]),pdatarr[2],getMonthString(pdatarr[1]));
                Toast.makeText(RecordActivity.this,"Inserted Successfully",Toast.LENGTH_LONG).show();
                dialog.dismiss();
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            }


        }




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
        String fdate=day+"-"+monthdate+"-"+years;
        datetext.setText(fdate);
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
}
