package com.example.panzq.simpleandroid_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends Activity {

    private IBookManager mBookManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //UserManager.sUserId = 2;
        //Log.d("panzqww","MainActivity sUserId = "+UserManager.sUserId);
        //序列化过程
        User user = new User(0,"jake",true);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("cache.txt"));
            out.writeObject(user);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //反序列化过程
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("cache.txt"));
            User newUser = (User)in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void toSecondActivity(View view)
    {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }
    public void toThirdActivity(View view)
    {
        Intent intent = new Intent(this,ThirdActivity.class);
        startActivity(intent);
    }
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient()
    {

        @Override
        public void binderDied() {
            if (mBookManager == null)
                return;
            mBookManager.asBinder().unlinkToDeath(mDeathRecipient,0);
            mBookManager = null;
        }
    };
}
