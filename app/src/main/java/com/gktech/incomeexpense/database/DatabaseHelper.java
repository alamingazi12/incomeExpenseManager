package com.gktech.incomeexpense.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import com.gktech.incomeexpense.MainActivity;
import com.gktech.incomeexpense.model.Accounts;
import com.gktech.incomeexpense.model.Months;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
      public  static  final  String database_name="Accounts";
    public  static  final  String account_table="account_table";
    public  static  final  String currency_table="CURRENCY";
    public  static  final  String CURRENCY="CURRENCYNAME";
    public  static  final  String DESCRIPTION="DESCRIPTION";
    public  static  final  String DATE="DATE";
    public  static  final  String TO_DATE="TDATE";
    public  static  final  String T_TYPE="TTYPE";
    public  static  final  String AMOUNT="AMOUNT";
    public  static  final  String CODE="CODE";
    public  static  final  String NOTE="NOTE";
    public  static  final  String PTYPE="PTYPE";





    public  static  final  String MONTH_TABLE="MONTH_TABLE";
    public  static  final  String MONTH_ID="MONTHID";
    public  static  final  String YEAR="YEAR";
    public  static  final  String MONTH="MONTH";
    public  static  final  int version_no=1;
   Context context;
    public DatabaseHelper(@Nullable Context context) {
        super(context, database_name, null, version_no);
        this.context=context;
    }

        @Override
        public void onCreate(SQLiteDatabase db) {
        String Accountquery="create table "+account_table+"(id integer primary key autoincrement,"+DESCRIPTION+" varchar(200),"+DATE+" varchar (20),"+TO_DATE+" varchar (30),"+T_TYPE+" varchar (20),"+AMOUNT+" integer,"+MONTH+" varchar(20),"+YEAR+" varchar(5),"+CODE+" varchar(2),"+NOTE+" varchar(400),"+PTYPE+" varchar (50))";
        String monthrecord="create table "+MONTH_TABLE+"(id integer primary key autoincrement,"+MONTH_ID+" integer,"+MONTH+" varchar(15),"+YEAR+" varchar (10))";
        String currency="create table "+currency_table+"(id integer primary key autoincrement,"+CURRENCY+" varchar (20))";
       try{
           db.execSQL(Accountquery);
           db.execSQL(monthrecord);
           db.execSQL(currency);
           Log.e("suc","table created Successfully");
           Toast.makeText(context,"table created",Toast.LENGTH_LONG).show();

       }catch (Exception e){
           Toast.makeText(context,"exception"+e.getStackTrace(),Toast.LENGTH_LONG).show();

       }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context,"upgrade called",Toast.LENGTH_LONG).show();
        Log.e("upgrade","upgrade called");
       /* onCreate(db);
        String currency="create table "+currency_table+"(id integer primary key autoincrement,"+CURRENCY+" varchar (20))";

                    try{
                        db.execSQL("drop table if exists "+account_table+"");
                        onCreate(db);
                        db.execSQL(currency);
        }catch (Exception e){
            Toast.makeText(context,"exception"+e.getStackTrace(),Toast.LENGTH_LONG).show();
        }
*/


    }

    public Long insertallAccounts(String description,String date,String todaydate,String transaction_type,int amount,String month,String year,String code,String payment_type){


        SQLiteDatabase database= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DESCRIPTION,description);
        contentValues.put(DATE,date);
        contentValues.put(TO_DATE,todaydate);
        contentValues.put(T_TYPE,transaction_type);
        contentValues.put(AMOUNT,amount);
        contentValues.put(MONTH,month);
        contentValues.put(YEAR,year);

        contentValues.put(CODE,code);
        //contentValues.put(NOTE,note);
        contentValues.put(PTYPE,payment_type);


        Long id=database.insert(account_table,null,contentValues);
        //getCategorySum("Income","February");
        return id;


    }


    public Long insertMonth(int monthid,String year,String month){
        Long id=null;
        SQLiteDatabase database= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MONTH_ID,monthid);
        contentValues.put(MONTH,month);
        contentValues.put(YEAR,year);

        try {
            id=database.insert(MONTH_TABLE,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,""+e.getStackTrace(),Toast.LENGTH_LONG).show();
        }
        return id;
    }

    public Long insert_Currency(String currency){
        Long id=null;
        SQLiteDatabase database= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(CURRENCY,currency);

        try {
            id=database.insert(currency_table,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,""+e.getStackTrace(),Toast.LENGTH_LONG).show();
        }
        return id;
    }

    public ArrayList<Months> displayData() {
        ArrayList<Months> monthsList=new ArrayList<>();
        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  MONTH_TABLE order by "+MONTH_ID+"",null);
        if (cursor.moveToFirst()) {
            do {
                String month=cursor.getString(cursor.getColumnIndex(MONTH));
                String year= cursor.getString(cursor.getColumnIndex(YEAR));
                monthsList.add(new Months(month,year));
                //Toast.makeText(this.context,"salary"+salarys,Toast.LENGTH_LONG).show();
            } while (cursor.moveToNext());
        }
        return monthsList;
    }

    public ArrayList<Accounts> displayAcccountData(String months) {
        ArrayList<Accounts> accountsArrayList=new ArrayList<>();
        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  "+account_table+" where "+MONTH+"='"+months+"'",null);

        if (cursor.moveToFirst()) {
            do {
                String description=cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                String date= cursor.getString(cursor.getColumnIndex(DATE));
                String todaydate=cursor.getString(cursor.getColumnIndex(TO_DATE));
                String transaction_type= cursor.getString(cursor.getColumnIndex(T_TYPE));
                String amount=cursor.getString(cursor.getColumnIndex(AMOUNT));
                String month= cursor.getString(cursor.getColumnIndex(MONTH));
                String year= cursor.getString(cursor.getColumnIndex(YEAR));
                String code=cursor.getString(cursor.getColumnIndex(CODE));
                String note= cursor.getString(cursor.getColumnIndex(NOTE));
                String ptype= cursor.getString(cursor.getColumnIndex(PTYPE));
                String id=cursor.getString(cursor.getColumnIndex("id"));

                accountsArrayList.add(new Accounts(description,date,todaydate,transaction_type,amount,month,year,ptype,code,id));
                //Toast.makeText(this.context,"salary"+salarys,Toast.LENGTH_LONG).show();
            } while (cursor.moveToNext());
        }
        return accountsArrayList;
    }

    public ArrayList<Accounts> displayAcccountData3(String months) {
        ArrayList<Accounts> accountsArrayList=new ArrayList<>();
        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  "+account_table+" where "+MONTH+"='"+months+"'order by id desc" ,null);

        if (cursor.moveToFirst()) {
            do {
                String description=cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                String date= cursor.getString(cursor.getColumnIndex(DATE));
                String todaydate=cursor.getString(cursor.getColumnIndex(TO_DATE));
                String transaction_type= cursor.getString(cursor.getColumnIndex(T_TYPE));
                String amount=cursor.getString(cursor.getColumnIndex(AMOUNT));
                String month= cursor.getString(cursor.getColumnIndex(MONTH));
                String year= cursor.getString(cursor.getColumnIndex(YEAR));
                String ptype= cursor.getString(cursor.getColumnIndex(PTYPE));
                String code=cursor.getString(cursor.getColumnIndex(CODE));
                String id=cursor.getString(cursor.getColumnIndex("id"));

                accountsArrayList.add(new Accounts(description,date,todaydate,transaction_type,amount,month,year,ptype,code,id));
                //Toast.makeText(this.context,"salary"+salarys,Toast.LENGTH_LONG).show();
            } while (cursor.moveToNext());
        }
        return accountsArrayList;
    }

    public String  fetch_Currency() {
        String currency=null;
        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  "+currency_table+"" ,null);

        if (cursor.moveToFirst()) {
            do {
            currency=cursor.getString(cursor.getColumnIndex(CURRENCY));
            } while (cursor.moveToNext());
        }
       return currency;
    }



    public boolean update_currency(String id,String currency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CURRENCY,currency);
        db.update(currency_table, contentValues, "id = ?",new String[]{id});
        return true;
    }

    public boolean update_AccountsData(String id,String description,int amount,String transaction_type,String payment_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DESCRIPTION,description);
        contentValues.put(T_TYPE,transaction_type);
        contentValues.put(AMOUNT,amount);
        contentValues.put(PTYPE,payment_type);
        db.update(account_table, contentValues, "id = ?",new String[]{id});
        return true;
    }




    public int getSumValue(){
        int sum=0;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String sumQuery=String.format("SELECT SUM(salary) as Total FROM MONTH_TABLE where name='hasan'");
        Cursor cursor=sqLiteDatabase.rawQuery(sumQuery,null);
        if(cursor.moveToFirst())
            sum= cursor.getInt(cursor.getColumnIndex("Total"));
        Toast.makeText(context,"exception"+sum,Toast.LENGTH_LONG).show();
        return sum;
    }

    public int getCategorySum(String category,String month){
        int csum=0;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String sumQuery=String.format("SELECT SUM("+AMOUNT+") as Total FROM "+account_table+" where "+T_TYPE+"='"+category+"' and "+MONTH+"='"+month+"'");
        Cursor cursor=sqLiteDatabase.rawQuery(sumQuery,null);
        if(cursor.moveToFirst())
            csum= cursor.getInt(cursor.getColumnIndex("Total"));

        Log.d("Category",String.valueOf(csum));
        Toast.makeText(context,"exception"+csum,Toast.LENGTH_LONG).show();
        return csum;
    }

    public int getIncomeExpense(String months){
        int sumincome=0;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String sumQuery=String.format("SELECT SUM("+AMOUNT+") as Total FROM "+account_table+" where "+MONTH+"='"+months+"'");
        Cursor cursor=sqLiteDatabase.rawQuery(sumQuery,null);
        if(cursor.moveToFirst())
            sumincome= cursor.getInt(cursor.getColumnIndex("Total"));
        Log.d("Expense",String.valueOf(sumincome));

        //Toast.makeText(context,"exception"+sum,Toast.LENGTH_LONG).show();
        return sumincome;
    }
    public int getIncome(String month,String code){
        int monthlyincome=0;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String sumQuery=String.format("SELECT SUM("+AMOUNT+") as Total FROM account_table where "+MONTH+"='"+month+"' and "+CODE+"='"+code+"'");
        Cursor cursor=sqLiteDatabase.rawQuery(sumQuery,null);
        if(cursor.moveToFirst())
            monthlyincome= cursor.getInt(cursor.getColumnIndex("Total"));
       // Log.d("Expense",String.valueOf(monthlyincome));

        //Toast.makeText(context,"exception"+sum,Toast.LENGTH_LONG).show();
        return monthlyincome;
    }

    public boolean isMonthExists(String month,String year){
        boolean exist=false;
        String emonths=null,eyear=null;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String sumQuery=String.format("SELECT * FROM "+MONTH_TABLE+" where  "+MONTH+"="+month+" and "+YEAR+"="+year+"");
        Cursor cursor=sqLiteDatabase.rawQuery(sumQuery,null);
        if(cursor.moveToFirst()){
            emonths= cursor.getString(cursor.getColumnIndex(MONTH));
            eyear=cursor.getString(cursor.getColumnIndex(YEAR));
        }

       // Toast.makeText(context,"exception"+emonths,Toast.LENGTH_LONG).show();


        if(!emonths.isEmpty() && !eyear.isEmpty()){

            Toast.makeText(context,"exception"+emonths,Toast.LENGTH_LONG).show();
           exist=true;
        }
        else {
            Toast.makeText(context,"No data Found"+emonths,Toast.LENGTH_LONG).show();

        }
      return exist;
    }

    public boolean updateData(String id,String name,String salary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //contentValues.put("name",name);
        contentValues.put("salary",salary);

        db.update("student_datails", contentValues, "ID = ? and name=? ",new String[]{id,name});
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(account_table, "id = ?",new String[] {id});
    }

    public boolean checkMonth(String month, String year){
        String[] columns = {MONTH};
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "MONTH=? and YEAR = ?";
        String[] selectionArgs = {month, year};

        Cursor cursor = db.query(MONTH_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        close();
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }
}
