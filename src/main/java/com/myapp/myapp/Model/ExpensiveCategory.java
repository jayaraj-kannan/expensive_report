package com.myapp.myapp.Model;

public enum ExpensiveCategory {
    SHOPPING, FOOD, HOUSEHOLD, FUEL, DUE, TRAVEL, ENTERTAIN, OTHERS;

    public String toString() {
        switch (this) {
            case SHOPPING:
                return "Shopping";
            case FOOD:
                return "Food";
            case HOUSEHOLD:
                return "Household";
            case FUEL:
                return "Fuel";
            case DUE:
                return "Due";
            case TRAVEL:
                return "Travel";
            case ENTERTAIN:
                return "Entertain";
            case OTHERS:
                return "Others";
        }
        return "";
    }
}
