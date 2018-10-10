package com.example.ipctest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ipctest.bean.User;
import com.example.ipctest.contants.MyConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        recoverFromFile();
    }

    private void recoverFromFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = null;
                File cachedFile = new File(MyConstants.CACHE_FILE_PATH);
                if (cachedFile.exists())
                {
                    ObjectInputStream in = null;
                    try {
                        in = new ObjectInputStream(new FileInputStream(cachedFile));
                        user = (User) in.readObject();
                        in.close();
                    } catch (IOException e) {
                        Log.e("panzqww","IOException"+e.getMessage());
                    } catch (ClassNotFoundException e) {
                        Log.e("panzqww","ClassNotFoundException"+e.getMessage());
                    } finally {
                    }
                    if (user != null) {
                        Log.d("panzqww", "recover user = " + user.toString());
                    }
                    else{
                        Log.d("panzqww","recover user is null");
                    }
                }else{
                    Log.d("panzqww",cachedFile.getAbsolutePath()+"not exists");
                }
            }
        }).start();

    }

}
