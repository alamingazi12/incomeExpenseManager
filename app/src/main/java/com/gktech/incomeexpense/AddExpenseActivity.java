package com.gktech.incomeexpense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gktech.incomeexpense.adapter.CategoryAdapter;
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

public class AddExpenseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,AdapterView.OnItemSelectedListener {
    public static String categoryname=null;
    ArrayList<Category> categories;
    RecyclerView tyerecyclerview;
    EditText expenseaAmount,expenseDescription,note_save;
    TextView expensedatetext;
    ImageView dateexpic;
    Button save_expense;
    DatabaseHelper databaseHelper;
    private InterstitialAd mInterstitialAd2;
    Spinner spinner_payment;
    String code,note,ptype;
    String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Expense");

       activity= getIntent().getStringExtra("activity");

        initialiseAll();
        initArraylist();

        dateexpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        expensedatetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        save_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });




    }

    private void initArraylist() {
        categories=new ArrayList<>();
        categories.add(new Category("Other",R.drawable.education,"2"));
        categories.add(new Category("Household",R.drawable.household,"2"));
        categories.add(new Category("Loan",R.drawable.eating,"2"));
        categories.add(new Category("Shopping",R.drawable.shopping,"2"));
        categories.add(new Category("Family",R.drawable.grocery,"2"));
        categories.add(new Category("Education",R.drawable.education,"2"));
        categories.add(new Category("Personal",R.drawable.personal,"2"));
        categories.add(new Category("Vacation",R.drawable.taxi,"2"));
        categories.add(new Category("Utilities",R.drawable.utilities,"2"));
        categories.add(new Category("Medical",R.drawable.medical,"2"));
        categories.add(new Category("Transport",R.drawable.fuel,"2"));
        categories.add(new Category("Fuel",R.drawable.taxi,"2"));

        CategoryAdapter categoryAdapter=new CategoryAdapter(AddExpenseActivity.this,categories);
        tyerecyclerview.setAdapter(categoryAdapter);

    }

    private void initialiseAll() {
        initSpinner();

        mInterstitialAd2 = new InterstitialAd(this);
        mInterstitialAd2.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd2.loadAd(new AdRequest.Builder().build());
        expenseaAmount=findViewById(R.id.amount_expense);
        expenseDescription=findViewById(R.id.description_expense);
        expensedatetext=findViewById(R.id.expensedatetext);

        dateexpic=findViewById(R.id.exdatepicimage);
        save_expense=findViewById(R.id.save_expense);
        databaseHelper=new DatabaseHelper(AddExpenseActivity.this);
        tyerecyclerview=findViewById(R.id.typerecyclerview);
        tyerecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 4);
        tyerecyclerview.setLayoutManager(manager);
    }

    public void initSpinner() {
        spinner_payment=findViewById(R.id.spinner_type);
        spinner_payment.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Cash");
        categories.add("Debit Card");
        categories.add("Credit Card");
        categories.add("Net Banking");
        categories.add("Electronic Transfer");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_payment.setAdapter(dataAdapter);


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void saveExpense(){
        String month=null;
        int monthid=-1;
        String year=null;
        String pdate=null;
        String examount=expenseaAmount.getText().toString();
        String  exdescription=expenseDescription.getText().toString();
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy, HH:mm");
        String today=sdf.format(date);
      pdate=expensedatetext.getText().toString();
      final   String pdatarr[];
        if(!pdate.equals("Select Date")){
            Toast.makeText(AddExpenseActivity.this,"date is not null",Toast.LENGTH_LONG).show();
            Log.e("date",pdate);
            pdatarr=pdate.split("-");
            month=getMonthString(pdatarr[1]);
            year=pdatarr[2];
            monthid= getMonthInt(pdatarr[1]);

            if(examount.isEmpty() || exdescription.isEmpty() || categoryname==null || today.isEmpty() || ptype.isEmpty() ||month==null ||year==null || monthid==-1){
                Toast.makeText(AddExpenseActivity.this,"One Field is Empty",Toast.LENGTH_LONG).show();
            }
            else {

                if(databaseHelper.checkMonth(month,year)){
                    long id=  databaseHelper.insertallAccounts(exdescription,pdate,today,categoryname,Integer.parseInt(examount),month,year,"2",ptype);
                    Toast.makeText(AddExpenseActivity.this,"Successfully Added",Toast.LENGTH_LONG).show();
                    onBackPressed();
                    if(mInterstitialAd2.isLoaded()){
                        mInterstitialAd2.show();
                    }
                    else {
                        mInterstitialAd2.loadAd(new AdRequest.Builder().build());
                    }
                }else {
                    //Toast.makeText(AddExpenseActivity.this,"data not  exists",Toast.LENGTH_LONG).show();
                    long id=  databaseHelper.insertallAccounts(exdescription,pdate,today,categoryname,Integer.parseInt(examount),month,year,"2",ptype);
                    databaseHelper.insertMonth(monthid,year,month);
                    Toast.makeText(AddExpenseActivity.this,"Successfully Added",Toast.LENGTH_LONG).show();
                    onBackPressed();

                    if(mInterstitialAd2.isLoaded()){

                        mInterstitialAd2.show();
                    }
                    else {

                        mInterstitialAd2.loadAd(new AdRequest.Builder().build());
                    }
                }

            }
        }
        else {
            Log.d("date2",pdate);
            Toast.makeText(AddExpenseActivity.this,"date is null",Toast.LENGTH_LONG).show();


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
      //  String fdate=day+"-"+monthdate+"-"+years;
        String sday= String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String smonth= String.valueOf(c.get(Calendar.MONTH)+1);
        String syear= String.valueOf(c.get(Calendar.YEAR));
        String fdate=sday+"-"+smonth+"-"+syear;


        expensedatetext.setText(fdate);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
       //  Log.d("items",item);
         ptype=item;
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
