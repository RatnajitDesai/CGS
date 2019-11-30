package com.darpg33.hackathon.cgs.Utils;

import java.util.regex.Pattern;

public class PatternChecker {

    public static boolean isValidPhoneNumber(String phoneNumber){

        Pattern phoneNumberCheck = Pattern.compile("[0-9]{10}");

        return phoneNumberCheck.matcher(phoneNumber).matches();
    }

    public static boolean isValidPinCode(String pinCode){

        Pattern pinCodeCheck = Pattern.compile("[0-9]{6}");

        return pinCodeCheck.matcher(pinCode).matches();
    }


}
