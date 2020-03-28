package com.gktech.incomeexpense;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.gktech.incomeexpense.adapter.ContentAdapter;
import com.gktech.incomeexpense.adapter.MenuAdapter;
import com.gktech.incomeexpense.database.DatabaseHelper;
import com.gktech.incomeexpense.fragment.DatePickerFragment;
import com.gktech.incomeexpense.model.Currency;
import com.gktech.incomeexpense.model.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
     public  static SharedPreferences.Editor editor;
    private InterstitialAd mInterstitialAd;
    public static DrawerLayout drawer;
    private static View Dialogview;
    Toolbar toolbar;
    public static SharedPreferences sPcurrency;
    DatabaseHelper databaseHelper;
    TextView datetext,incomeDescription,incomeAmount;
    String ptype;
    Spinner payment_spinner,category_spinner;
    ArrayList<MenuItem> items;
    ContentAdapter contentAdapter;
    RecyclerView recyclercontents,menucontentitems;
    private String category;
    AlertDialog dialog;
   public static SharedPreferences monthsharedPreference;
   public static SharedPreferences.Editor editor_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        databaseHelper=new DatabaseHelper(MainActivity.this);
        initializeAll();

        if(databaseHelper.fetch_Currency()==null){
            databaseHelper.insert_Currency("default");
        }

      if(databaseHelper.fetch_Currency().equals("default")){
          Log.e("currency",databaseHelper.fetch_Currency());
          Intent intent=new Intent(MainActivity.this, CurrencyActivity.class);
          startActivity(intent);
      }else {
          editor.putString("cvalue",databaseHelper.fetch_Currency());
          editor.apply();
      }
        initDrawerListener();
        initFloatingButton();
        showMonthData();
        //$€£¥₽₩₪฿₫₴₹
    }


    @Override
    public void onBackPressed() {
       finishAffinity();
    }

    public void todayDate(){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy-HH:mm");
        String today=sdf.format(date);
        Toast.makeText(MainActivity.this,"today time:"+today,Toast.LENGTH_LONG).show();
    }

    public void saveIncome(){
       String incomeamount=incomeAmount.getText().toString();
       String  description=incomeDescription.getText().toString();
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy, HH:mm");
        String today=sdf.format(date);
        String pdate=datetext.getText().toString();
        if(pdate.equals("Select Date")){
            Log.d("date",pdate);
            Toast.makeText(MainActivity.this,"One or More Fields Empty",Toast.LENGTH_LONG).show();
        }else {
            String pdatarr[] = pdate.split("-");
            if (description.isEmpty() || pdate.isEmpty() || today.isEmpty() || category.isEmpty() || String.valueOf(incomeamount).isEmpty() || getMonthString(pdatarr[1]).isEmpty() || ptype.isEmpty()) {
                Log.d("empty", "Fields Empty");
                Toast.makeText(MainActivity.this,"One or More Fields Empty",Toast.LENGTH_LONG).show();
            } else {

                if (databaseHelper.checkMonth(getMonthString(pdatarr[1]), pdatarr[2])) {
                    long id = databaseHelper.insertallAccounts(description, pdate, today, category, Integer.parseInt(incomeamount), getMonthString(pdatarr[1]), pdatarr[2], "1", ptype);
                    // Toast.makeText(MainActivity.this,"data exists",Toast.LENGTH_LONG).show();
                    contentAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                    showMonthData();
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }

                } else {
                    //Toast.makeText(MainActivity.this,"data not  exists",Toast.LENGTH_LONG).show();
                    long id = databaseHelper.insertallAccounts(description, pdate, today, category, Integer.parseInt(incomeamount), getMonthString(pdatarr[1]), pdatarr[2], "1", ptype);
                    databaseHelper.insertMonth(getMonthInt(pdatarr[1]), pdatarr[2], getMonthString(pdatarr[1]));
                    contentAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                    showMonthData();
                    //  showMenuData();
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                }
            }

        }

    }

    private void showMenuData() {
        items=new ArrayList<>();

        items.add(new MenuItem(R.drawable.ic_home_24dp,"Home"));
        items.add(new MenuItem(R.drawable.ic_currency_icon,"Chosen Currency"));
        items.add(new MenuItem(R.drawable.images1,"Rate Us"));
        items.add(new MenuItem(R.drawable.aboutus,"About Us"));
        MenuAdapter menuAdapter=new MenuAdapter(items,MainActivity.this);
        menucontentitems.setAdapter(menuAdapter);

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

    private void showMonthData() {
         contentAdapter=new ContentAdapter(MainActivity.this,databaseHelper.displayData(),databaseHelper);
         recyclercontents.setAdapter(contentAdapter);
         contentAdapter.notifyDataSetChanged();
         Log.d("method","method called");
         showMenuData();
    }


    @Override
    protected void onResume() {
        showMonthData();
        Log.d("onResume","onResume called");
        //Toast.makeText(MainActivity.this,"onresume called",Toast.LENGTH_LONG).show();
        super.onResume();
    }

    public  void showDialog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
          Dialogview =this.getLayoutInflater().inflate(R.layout.layout_dialog,null);
          Button button_save=Dialogview.findViewById(R.id.save_income);
          Button button_cancel=Dialogview.findViewById(R.id.save_cancel);
          ImageView calendarpic=Dialogview.findViewById(R.id.datepicimage);
          incomeDescription=Dialogview.findViewById(R.id.description_income);
          incomeAmount=Dialogview.findViewById(R.id.amount_income);
          datetext = Dialogview.findViewById(R.id.datetext);
          datetext.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  DialogFragment datePicker = new DatePickerFragment();
                  datePicker.show(getSupportFragmentManager(), "date picker");
              }
          });



            category_spinner=Dialogview.findViewById(R.id.spinner_category_income);
            payment_spinner=Dialogview.findViewById(R.id.spinner_paymenttype_income);
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


        builder.setView(Dialogview);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

         calendarpic.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 DialogFragment datePicker = new DatePickerFragment();
                 datePicker.show(getSupportFragmentManager(), "date picker");
             }
         });

          button_cancel.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                 dialog.dismiss();
                //  Toast.makeText(MainActivity.this,"button clicked",Toast.LENGTH_LONG).show();

              }
          });
            button_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    saveIncome();
                    //  Toast.makeText(MainActivity.this,"button clicked",Toast.LENGTH_LONG).show();

                }
            });
      dialog.show();
    }

    private void initDrawerListener() {
        toolbar=findViewById(R.id.toolbar);
         drawer=findViewById(R.id.mydrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    private void initFloatingButton() {
        FloatingActionButton floatingActionButtonexpense=findViewById(R.id.fabexpense);
        floatingActionButtonexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddExpenseActivity.class);
                intent.putExtra("activity","1");
                startActivity(intent);
                Toast.makeText(MainActivity.this,"expense Clicked",Toast.LENGTH_LONG).show();
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

    private void initializeAll() {


        if(sPcurrency==null){
            Log.d("init","int share preference");
            sPcurrency=MainActivity.this.getSharedPreferences("Currency",MODE_PRIVATE);
            editor=sPcurrency.edit();
            editor.putString("cvalue","default");
            editor.commit();
        }
        if(monthsharedPreference==null){
            monthsharedPreference=MainActivity.this.getSharedPreferences("month",MODE_PRIVATE);
            editor_month=monthsharedPreference.edit();
            editor_month.putString("monthName",null);
            editor_month.commit();
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        menucontentitems=findViewById(R.id.myrecyclermenucontents);
        menucontentitems.setHasFixedSize(true);
        menucontentitems.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclercontents=findViewById(R.id.myrecyclercontents);
        recyclercontents.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
        recyclercontents.setLayoutManager(manager);
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

          String sday= String.valueOf(c.get(Calendar.DAY_OF_MONTH));
          String smonth= String.valueOf(c.get(Calendar.MONTH)+1);
          String syear= String.valueOf(c.get(Calendar.YEAR));
          String fdate=sday+"-"+smonth+"-"+syear;

            datetext.setText(fdate);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_paymenttype_income:
                 ptype = payment_spinner.getSelectedItem().toString();
                 break;
            case R.id.spinner_category_income:
                category = category_spinner.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
