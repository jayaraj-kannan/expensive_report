package com.myapp.myapp.Model;

public enum ExpensiveSource {
    CASH,ACCOUNT,CARD;

    public String toString() {
        switch(this){
            case CASH: return "Cash";
            case ACCOUNT: return "Account";
            case CARD: return "Card";
        }
        return "";
    }
}
