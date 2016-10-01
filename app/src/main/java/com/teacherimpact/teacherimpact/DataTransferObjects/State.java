package com.teacherimpact.teacherimpact.DataTransferObjects;

import java.util.ArrayList;

public class State {
    private ArrayList<String> arrayList = new ArrayList<>();

    public State(){
        this.arrayList = new ArrayList<>();
        populate();
    }

    public void populate(){
        arrayList.add("");
        arrayList.add("Alabama - AL");
        arrayList.add("Alaska - AK");
        arrayList.add("Arizona - AZ");
        arrayList.add("Arkansas - AR");
        arrayList.add("California - CA");
        arrayList.add("Colorado - CO");
        arrayList.add("Connecticut - CT");
        arrayList.add("Delaware - DE");
        arrayList.add("Florida - FL");
        arrayList.add("Georgia - GA");
        arrayList.add("Hawaii - HI");
        arrayList.add("Idaho - ID");
        arrayList.add("Illinois - IL");
        arrayList.add("Indiana - IN");
        arrayList.add("Iowa - IA");
        arrayList.add("Kansas - KS");
        arrayList.add("Kentucky - KY");
        arrayList.add("Louisiana - LA");
        arrayList.add("Maine - ME");
        arrayList.add("Maryland - MD");
        arrayList.add("Massachusetts - MA");
        arrayList.add("Michigan - MI");
        arrayList.add("Minnesota - MN");
        arrayList.add("Mississippi - MS");
        arrayList.add("Missouri - MO");
        arrayList.add("Montana - MT");
        arrayList.add("Nebraska - NE");
        arrayList.add("Nevada - NV");
        arrayList.add("New Hampshire - NH");
        arrayList.add("New Jersey - NJ");
        arrayList.add("New Mexico - NM");
        arrayList.add("New York - NY");
        arrayList.add("North Carolina - NC");
        arrayList.add("North Dakota - ND");
        arrayList.add("Ohio - OH");
        arrayList.add("Oklahoma - OK");
        arrayList.add("Oregon - OR");
        arrayList.add("Pennsylvania - PA");
        arrayList.add("Rhode Island - RI");
        arrayList.add("South Carolina - SC");
        arrayList.add("South Dakota - SD");
        arrayList.add("Tennessee - TN");
        arrayList.add("Texas - TX");
        arrayList.add("Utah - UT");
        arrayList.add("Vermont - VT");
        arrayList.add("Virginia - VA");
        arrayList.add("Washington - WA");
        arrayList.add("West Virginia - WV");
        arrayList.add("Wisconsin - WI");
        arrayList.add("Wyoming - WY");
        arrayList.add("American Samoa - AS");
        arrayList.add("District of Columbia - DC");
        arrayList.add("Federated States of Micronesia - FM");
        arrayList.add("Guam - GU");
        arrayList.add("Marshall Islands - MH");
        arrayList.add("Northern Mariana Islands - MP");
        arrayList.add("Palau - PW");
        arrayList.add("Puerto Rico - PR");
        arrayList.add("Virgin Islands - VI");
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }
}
