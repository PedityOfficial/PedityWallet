package com.example.pwallet.peditywallet;

public class AccountData {

    private String amount, accountid,type;

    public AccountData(String amount, String accountid, String type) {
        this.amount = amount;
        this.accountid = accountid;
        this.type = type;

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String id) {
        this.amount = amount;
    }

    public String getAcountid() {
        return accountid;
    }

    public void setAcountid(String title) {
        this.accountid = accountid;
    }

    public String getType() {
        return type;
    }

    public void setType(String id) {
        this.type = type;
    }

}