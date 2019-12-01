package com.darpg33.hackathon.cgs.Utils;

import android.Manifest;

public class Permissions {

    public static String[] PERMISSIONS =
            {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                    };


    public static String[] LOCATION_PERMISSIONS =
            {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };



    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;

    public static final String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

    public static final String WRITE_EXTERNAL_STORAGE_PERMISSION =  Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static final String ACCESS_FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;




}
