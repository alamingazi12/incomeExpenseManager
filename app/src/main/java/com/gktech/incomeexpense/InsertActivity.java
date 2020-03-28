package com.gktech.incomeexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gktech.incomeexpense.database.DatabaseHelper;
import com.gktech.incomeexpense.model.Accounts;

import net.ozaydin.serkan.easy_csv.EasyCsv;
import net.ozaydin.serkan.easy_csv.FileCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.content.FileProvider.getUriForFile;

public class InsertActivity extends AppCompatActivity {

    public final int WRITE_PERMISSON_REQUEST_CODE = 1;
    Uri uri;
    File file;
    EasyCsv easyCsv;
    ArrayList<String> datalist;
    DatabaseHelper databaseHelper;
    ArrayList<Accounts> accountsArrayList;
    List<String> headerList;
    int income,expense;
    String month;

    private EditText mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;

    Button sent_to_mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        month=getIntent().getStringExtra("month");
        income= getIntent().getIntExtra("income",0);
        expense= getIntent().getIntExtra("expense",0);
        sent_to_mail=findViewById(R.id.button_send);

        mEditTextTo = findViewById(R.id.edit_text_to);
        mEditTextSubject = findViewById(R.id.edit_text_subject);

        getSupportActionBar().setTitle(month);
         easyCsv=new EasyCsv(InsertActivity.this);
         datalist=new ArrayList<>();

         sendData();
         sent_to_mail.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendMail();
             }
         });
    }

    private void sendMail() {
        String recipientList = mEditTextTo.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject = mEditTextSubject.getText().toString();
//        String message = mEditTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("text/csv");

        if(uri!=null){
            Log.d("uri",String.valueOf(uri));
            intent.putExtra(Intent.EXTRA_STREAM,uri);
        }else {
            Toast.makeText(InsertActivity.this,"Uri is null",Toast.LENGTH_LONG).show();
        }

        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    private void sendData() {
       //  Log.d("senddata","send data calling");
         headerList = new ArrayList<>();
        headerList.add("Description*PaymentType*AddedOn*Time*TransactionType*Amount/");
        databaseHelper=new DatabaseHelper(InsertActivity.this);
        accountsArrayList=databaseHelper.displayAcccountData(month);

         if(accountsArrayList.size()>0){
            // Log.d("senddata","send data calling");
             for(Accounts accounts :accountsArrayList){
                 datalist.add(accounts.getDescription()+"*"+accounts.getPaymentType()+"*"+accounts.getTodaydate()+"*"+accounts.ttype+"*"+accounts.getAmount()+"/");
             }
             SharedPreferences sharedPreferencesmonth=InsertActivity.this.getSharedPreferences("month",MODE_PRIVATE);
             String fileName=sharedPreferencesmonth.getString("monthName",null)+"-"+"2020";
             easyCsv.setSeparatorColumn("*");
             easyCsv.setSeperatorLine("/");

             if(datalist.size()>0){
               //  Log.d("senddata","send data calling");
                 easyCsv.createCsvFile(fileName, headerList, datalist, WRITE_PERMISSON_REQUEST_CODE, new FileCallback() {
                     @Override
                     public void onSuccess(File file) {
                         file.toString();
                         file=new File(String.valueOf(file));
                         uri=  getUriForFile(InsertActivity.this,"com.gktech.incomeexpense.fileprovider",file);
                         //  uri = Uri.fromFile(new File(file.getAbsolutePath()));
                         Log.d("uri", String.valueOf(uri));
                       //  Toast.makeText(InsertActivity.this,"Saved file"+uri,Toast.LENGTH_LONG).show();
                     }

                     @Override
                     public void onFail(String err) {
                       //  Log.d("uri",err);
                      //   Toast.makeText(InsertActivity.this,"Error"+err,Toast.LENGTH_SHORT).show();

                     }
                 });
             }


         }






    }


}
