package com.gktech.incomeexpense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gktech.incomeexpense.adapter.CurrencyAdapter;
import com.gktech.incomeexpense.database.DatabaseHelper;
import com.gktech.incomeexpense.model.Currency;

import java.util.ArrayList;

public class CurrencyActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    Button ok_button,cancel_button;
    RecyclerView currencycontainer;
    ArrayList<Currency> currencies;
    CurrencyAdapter currencyAdapter;
    public  static SharedPreferences sharedPreferences;
   public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        initializeAll();
        initializeArraylist();


        EditText editText = findViewById(R.id.edittext_search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<Currency> filteredList = new ArrayList<>();

        for (Currency item : currencies) {
            if (item.getCName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        currencyAdapter.filterList(filteredList);
    }

    private void initializeArraylist() {
        currencies.add(new Currency("Afghan afghani","AFN","Afghanistan"));
        currencies.add(new Currency("European euro","€","Akrotiri and Dhekelia"));
        currencies.add(new Currency("European euro","€","Aland Islands"));
        currencies.add(new Currency("Albanian lek","ALL","Albania"));
        currencies.add(new Currency("Algerian dinar","DZD","Algeria"));
        currencies.add(new Currency("United States dollar","$","American Samoa "));
        currencies.add(new Currency("Angolan kwanza","AOA","Angola"));
        currencies.add(new Currency("East Caribbean dollar","XCD","Anguilla"));
        currencies.add(new Currency("Argentine peso","ARS","Argentina"));
        currencies.add(new Currency("Armenian dram","AMD","Armenia"));
        currencies.add(new Currency("Aruban florin","AWG","Aruba (Netherlands)"));
        currencies.add(new Currency("Saint Helena pound","SHP","Ascension Island (UK)"));
        currencies.add(new Currency("Australian dollar","AUD","Australia"));
        currencies.add(new Currency("Azerbaijan manat","AZN","Azerbaijan"));
        currencies.add(new Currency("Bahamian dollar","BSD","Bahamas"));
        currencies.add(new Currency("Bahraini dinar","BHD","Bahrain"));
        currencies.add(new Currency("Bangladeshi taka","BDT","Bangladesh"));
        currencies.add(new Currency("Barbadian dollar","BBD","Barbados"));
        currencies.add(new Currency("Belarusian ruble","BYN","Belarus"));
        currencies.add(new Currency("Belize dollar","BZD","Belgium"));

        currencies.add(new Currency("West African CFA franc","XOF","Benin"));
        currencies.add(new Currency("Bermudian dollar","BMD","Bermuda"));
        currencies.add(new Currency("Bhutanese ngultrum","BTN","Bhutan"));
        currencies.add(new Currency("Bolivian boliviano","BOB","Bolivia"));
        currencies.add(new Currency("United States dollar","$","Bonaire (Netherlands)"));
        currencies.add(new Currency("Bosnia and Herzegovina convertible mark","BAM","Bosnia and Herzegovina"));

        currencies.add(new Currency("Brazilian real","BRL","Brazil"));


        currencies.add(new Currency("Cambodian riel","KHR","Cambodia"));
        currencies.add(new Currency("Central African CFA franc","XAF","Cameroon"));
        currencies.add(new Currency("Canadian dollar","CAD","Canada"));
        currencies.add(new Currency("Cayman Islands dollar","KYD","Cayman Islands "));
        currencies.add(new Currency("New Zealand dollar","NZD","Chatham Islands "));


        currencies.add(new Currency("Danish krone","DKK","Denmark"));
        currencies.add(new Currency("Djiboutian franc","DJF","Djibouti"));
        currencies.add(new Currency("East Caribbean dollar","XCD","Dominica"));
        currencies.add(new Currency("Dominican peso","DOP","Dominican Republic"));

        currencies.add(new Currency("Egyptian pound","EGP","Egypt"));

        currencies.add(new Currency("Eritrean nakfa","ERN","Eritrea"));
        currencies.add(new Currency("Swazi lilangeni","SZL","Eswatini"));
        currencies.add(new Currency("Ethiopian birr","ETB","Ethiopia"));

        currencies.add(new Currency("Falkland Islands pound","FKP","Falkland Islands"));

        currencies.add(new Currency("Fijian dollar","FJD","Fiji"));
        currencies.add(new Currency("CFP franc","XPF","French Polynesia"));


        currencies.add(new Currency("Hong Kong dollar","HKD","Hong Kong "));
        currencies.add(new Currency("Hungarian forint","HUF","Hungary"));
        currencies.add(new Currency("Icelandic krona","ISK","Iceland"));
        currencies.add(new Currency("Indian rupee","₹","India")); //$€£¥₽₩₪฿₫₴₹
        currencies.add(new Currency("Indonesian rupiah","IDR","Indonesia"));
        currencies.add(new Currency("Iranian rialr","IRR","Iran"));
        currencies.add(new Currency("Iraqi dinar","IQD","Iraq"));


        currencies.add(new Currency("Israeli new shekel","₪","Israel"));
        currencies.add(new Currency("Jamaican dollar","JMD","Jamaica")); //$€£¥₽₩₪฿₫₴₹
        currencies.add(new Currency("Japanese yen","¥","Japan"));
        currencies.add(new Currency("Jersey pound","JEP","Jersey"));
        currencies.add(new Currency("Jordanian dinar","JOD","Jordan"));

        currencies.add(new Currency("Kenyan shilling","KES","Kenya"));
        currencies.add(new Currency("Lebanese pound","LBP","Lebanon")); //$€£¥₽₩₪฿₫₴₹
        currencies.add(new Currency("Libyan dinar","LYD","Libya"));
        currencies.add(new Currency("Malaysian ringgit","MYR","Malaysia"));
        currencies.add(new Currency("Mexican peso","MXN","Mexico"));


        currencies.add(new Currency("Kenyan shilling","KES","Myanmar"));
        currencies.add(new Currency("Nepalese rupee","NPR","Nepal")); //$€£¥₽₩₪฿₫₴₹
        currencies.add(new Currency("New Zealand dollar","NZD","New Zealand"));
        currencies.add(new Currency("Nigerian naira","NGN","Nigeria"));
        currencies.add(new Currency("Omani rial","OMR","Oman"));

        currencies.add(new Currency("Pakistani rupee","PKR","Pakistan"));
        currencies.add(new Currency("Paraguayan guarani","PYG","Paraguay")); //$€£¥₽₩₪฿₫₴₹
        currencies.add(new Currency("Philippine peso","PHP","Philippines"));
        currencies.add(new Currency("European euro","€","Portugal"));
        currencies.add(new Currency("Qatari riyal","QAR","Qatar"));


        currencies.add(new Currency("Russian ruble","RUB","Russia"));
        currencies.add(new Currency("Saudi Arabian riyal","SAR","Saudi Arabia")); //$€£¥₽₩₪฿₫₴₹
        currencies.add(new Currency("Serbian dinar","RSD","Serbia"));
        currencies.add(new Currency("Singapore dollar","SGD","Singapore"));
        currencies.add(new Currency("South African rand","ZAR","South Africa"));


        currencies.add(new Currency("South Korean won","KRW","South Korea"));
        currencies.add(new Currency("European euro","€","Spain")); //$€£¥₽₩₪฿₫₴₹
        currencies.add(new Currency("Sri Lankan rupee","LKR","Sri Lanka"));
        currencies.add(new Currency("Swedish krona","SEK","Sweden"));
        currencies.add(new Currency("Swiss franc","CHF","Switzerland"));


        currencies.add(new Currency("Syrian pound","SYP","Syria"));
        currencies.add(new Currency("Tajikistani somoni","TJS","Tajikistan")); //$€£¥₽₩₪฿₫₴₹
        currencies.add(new Currency("Thai baht","THB","Thailand"));
        currencies.add(new Currency("Tunisian dinar","TND","Tunisia"));
        currencies.add(new Currency("Turkish lira","TRY","Turkey"));

        currencies.add(new Currency("Ugandan shilling","UGX","Uganda"));
        currencies.add(new Currency("UAE dirham","AED","United Arab Emirates")); //$€£¥₽₩₪฿₫₴₹
        currencies.add(new Currency("Pound sterling","GBP","United Kingdom"));
        currencies.add(new Currency("United States dollar","$","United States of America"));
        currencies.add(new Currency("Uruguayan peso","UYU","Uruguay"));
        currencies.add(new Currency("Uzbekistani som","UZS","Uzbekistan"));


        currencies.add(new Currency("Vietnamese dong","VND","Vietnam"));
        currencies.add(new Currency("Yemeni rial","YER","Yemen"));
        currencies.add(new Currency("United States dollar","$","Zimbabwe"));





        currencyAdapter=new CurrencyAdapter(currencies,CurrencyActivity.this);
        currencycontainer.setAdapter(currencyAdapter);

        currencyAdapter.setOnItemClickListener(new CurrencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
               showDialog(currencies.get(position).getMcurrency());
                Log.d("selected Item",currencies.get(position).getMcurrency());
            }
        });




    }


    private void showDialog(final String mcurrency) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(CurrencyActivity.this);
        View  Dialogview =LayoutInflater.from(CurrencyActivity.this).inflate(R.layout.layout_dialog_currency,null);
       // Button button=Dialogview.findViewById(R.id.saveincome);
        ok_button=Dialogview.findViewById(R.id.selectok);
        cancel_button=Dialogview.findViewById(R.id.selectcancel);
        builder.setView(Dialogview);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences= CurrencyActivity.this.getSharedPreferences("Currency",Context.MODE_PRIVATE);
                String value=sharedPreferences.getString("cvalue",null);
                Toast.makeText(CurrencyActivity.this,"button clicked"+mcurrency,Toast.LENGTH_LONG).show();
                databaseHelper.update_currency("1",mcurrency);
                MainActivity.editor.putString("cvalue",databaseHelper.fetch_Currency());
                MainActivity.editor.apply();
                dialog.dismiss();
                Intent intent=new Intent(CurrencyActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CurrencyActivity.this,"cancel clicked",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }




    private void initializeAll() {

        databaseHelper=new DatabaseHelper(CurrencyActivity.this);
        if(sharedPreferences==null){
            sharedPreferences=this.getSharedPreferences("Currency",MODE_PRIVATE);
            editor=sharedPreferences.edit();
            editor.putString("cvalue","default");
            editor.commit();
        }

        currencies=new ArrayList<>();
        currencycontainer=findViewById(R.id.currency_container);
        currencycontainer.setHasFixedSize(true);
        currencycontainer.setLayoutManager(new LinearLayoutManager(CurrencyActivity.this));
    }
}
