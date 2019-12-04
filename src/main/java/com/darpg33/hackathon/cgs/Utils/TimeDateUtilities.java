package com.darpg33.hackathon.cgs.Utils;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeDateUtilities {

    public static String getDateAndTime(Timestamp timestamp)
    {

        Date date = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        return simpleDateFormat.format(date);
    }

}
