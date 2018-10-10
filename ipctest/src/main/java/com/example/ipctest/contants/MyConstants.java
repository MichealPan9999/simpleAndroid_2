package com.example.ipctest.contants;

import android.os.Environment;

import java.io.File;

public class MyConstants {
    public static final String  CHAPTER_2_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"chapter";
    public static final String  CACHE_FILE_PATH = CHAPTER_2_PATH+ File.separator+"cache.txt";
}
