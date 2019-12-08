package com.darpg33.hackathon.cgs.Utils;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeDateUtilities {

    public static String getDateAndTime(Timestamp timestamp)
    {

        Date date = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM,yy HH:mm", Locale.US);
        return simpleDateFormat.format(date);
    }

    public static String getTime(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        return simpleDateFormat.format(date);
    }

    public static String getDate(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM,yy", Locale.US);
        return simpleDateFormat.format(date);
    }
}
