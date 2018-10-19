package com.example.bindertest;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private ISecurityCenter mSecurityCenter;
    private ICompute mCompute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dowork();
            }
        }).start();
    }

    private void dowork() {
        BinderPool binderPool = BinderPool.getInstance(MainActivity.this);
        IBinder securityBinder = binderPool.queryBinder(Contents.BINDER_SECURUITY_CENTER);
        mSecurityCenter = SecurityCenterImpl.asInterface(securityBinder);
        String msg = "hello world --- 安卓！";
        try {
            String password = mSecurityCenter.encrypt(msg);
            Log.d("panzqww","encrypt -------- "+password);
            String decryptpassword = mSecurityCenter.decrypt(password);
            Log.d("panzqww","decrypt -------- "+decryptpassword);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder computeBinder = binderPool.queryBinder(Contents.BINDER_COMPUTE);
        mCompute = ComputeImpl.asInterface(computeBinder);
        try {
            Log.d("panzqww","mCompute.add(3, 5) = "+(mCompute.add(3, 5)));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
