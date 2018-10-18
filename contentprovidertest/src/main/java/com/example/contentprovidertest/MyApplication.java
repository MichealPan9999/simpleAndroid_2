package com.example.contentprovidertest;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("panzqww","currentProcess :" +getCurrentProcessName());
    }
    private String getCurrentProcessName()
    {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcessInfo:mActivityManager.getRunningAppProcesses())
        {
            if (appProcessInfo.pid == pid)
            {
                return appProcessInfo.processName;
            }
        }
        return "";
    }
}
