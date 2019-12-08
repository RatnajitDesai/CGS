package com.darpg33.hackathon.cgs.Utils;

import android.util.Log;

public class FileUtilities {

    private static final String TAG = "FileUtilities";
    private static final Long MAX_FILE_SIZE = 3000000L;


    public static boolean checkFileSize(long l)
    {

        if (l>MAX_FILE_SIZE)
        {
            Log.d(TAG, "checkFileSize: l in bytes:"+l);
            return false;
        }
        return true;

    }

}
