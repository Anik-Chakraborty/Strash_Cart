package com.example.strash_cart;

public class ReadWriteUserDetails {
    public String FullName, EmailAddress, PhoneNumber, UserMode;


    public ReadWriteUserDetails(){}

    public ReadWriteUserDetails(String phoneNumber){
        this.PhoneNumber = phoneNumber;
    }

    public ReadWriteUserDetails(String textfullname, String email, String phoneNumber, String userMode){
        this.FullName = textfullname;
        this.EmailAddress = email;
        this.PhoneNumber = phoneNumber;
        this.UserMode = userMode;
    }
}
