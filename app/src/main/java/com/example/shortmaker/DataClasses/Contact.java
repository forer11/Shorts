package com.example.shortmaker.DataClasses;

public class Contact {

    private String contactName;
    private String contactNumber;
    public Contact(String text1, String text2) {
        contactName = text1;
        contactNumber = text2;
    }

    public String getText1() {
        return contactName;
    }
    public String getText2() {
        return contactNumber;
    }
}
