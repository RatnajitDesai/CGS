package com.darpg33.hackathon.cgs.Utils;

import java.util.ArrayList;

public class SpinnerListProvider {


    public static ArrayList<String> getCountryList()
    {
        ArrayList<String> list = new ArrayList<>();

        list.add("India");
        list.add("USA");
        list.add("Russia");
        list.add("China");
        list.add("Select Country");

        return list;

    }


    public static ArrayList<String> getStateList()
    {
        ArrayList<String> list = new ArrayList<>();

        list.add("Maharashtra");
        list.add("Gujarat");
        list.add("Karnataka");
        list.add("Delhi");
        list.add("Select State");

        return list;

    }


    public static ArrayList<String> getDistrictList() {
        ArrayList<String> list = new ArrayList<>();

        list.add("Thane");
        list.add("Kolhapur");
        list.add("Pune");
        list.add("Satara");
        list.add("Nashik");
        list.add("Select District");

        return list;

    }


    public static ArrayList<String> getGrievanceCategoriesList() {
        ArrayList<String> list = new ArrayList<>();

        list.add("Road/Transport");
        list.add("Health/Hygiene");
        list.add("Water");
        list.add("Fraud");
        list.add("Bribery/Corruption");
        list.add("Administration");
        list.add("Select Grievance Category");

        return list;

    }
}
