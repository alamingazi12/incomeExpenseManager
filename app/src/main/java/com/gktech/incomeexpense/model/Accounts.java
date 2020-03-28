package com.gktech.incomeexpense.model;

public class Accounts {

    public String getId() {
        return id;
    }

    public    String id;
    public    String description;
    public    String date;
    public    String todaydate;
    public    String ttype;
    public    String amount;
    public    String month;
    public    String year;

    public String getCode() {
        return code;
    }

    public    String code;

    public String getPaymentType() {
        return paymentType;
    }

    public   String paymentType;

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }



    public Accounts(String description, String date, String todaydate, String ttype, String amount,String month,String year,String paymentType,String code,String id) {
        this.description = description;
        this.date = date;
        this.todaydate = todaydate;
        this.ttype = ttype;
        this.amount = amount;
        this.paymentType=paymentType;
        this.code=code;
        this.id=id;

    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTodaydate() {
        return todaydate;
    }

    public String getTtype() {
        return ttype;
    }

    public String getAmount() {
        return amount;
    }
}
