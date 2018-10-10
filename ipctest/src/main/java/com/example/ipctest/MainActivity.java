package com.example.ipctest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ipctest.bean.User;
import com.example.ipctest.contants.MyConstants;
import com.example.ipctest.utils.PermissionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        persistToFile();
    }
    public void toSecondActivity(View view)
    {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    private void persistToFile()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user2 = new User(1,"hello world",false);
                File dir = new File(MyConstants.CHAPTER_2_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                    dir.setWritable(true);
                    dir.setReadable(true);
                }
                File cacheFile = new File(MyConstants.CACHE_FILE_PATH);
                try {
                    cacheFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(new FileOutputStream(cacheFile));
                    out.writeObject(user2);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
                Log.d("panzqww","persist user : "+user2.toString());
            }
        }).start();
    }
}
