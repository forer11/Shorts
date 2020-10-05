package com.shorts.shortmaker.DataClasses;

public class Contact {

    private String contactName;
    private String contactNumber;
    public Contact(String text1, String text2) {
        contactName = text1;
        contactNumber = text2;
    }

    public String getContactName() {
        return contactName;
    }
    public String getContactNum() {
        return contactNumber;
    }
}
