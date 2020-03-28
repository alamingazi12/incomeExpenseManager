package com.gktech.incomeexpense.model;

public class Currency {

    public String getCName() {
        return CName;
    }

    public String getMcurrency() {
        return mcurrency;
    }

    String CName;

    public Currency(String CName, String mcurrency,String country) {
        this.CName = CName;
        this.mcurrency = mcurrency;
        this.country=country;
    }

    String mcurrency;

    public String getCountry() {
        return country;
    }

    String country;

}
