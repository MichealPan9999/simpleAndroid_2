package com.example.aidltest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBookManager bookManager = IBookManager.Stub.asInterface(iBinder);
            try {
                List<Book> list = bookManager.getBookList();
                Log.d("panzqww","query book list ,list type :"+list.getClass().getCanonicalName());
                for (int i =0 ;i< list.size();i++) {
                    Log.d("panzqww", "query book list ("+i+"):" + list.get(i).toString());
                }
                Book newBook = new Book(3,"JAVA");
                bookManager.addBook(newBook);
                Log.d("panzqww","add new book:"+newBook.toString());
                List<Book> newList = bookManager.getBookList();
                for (int i =0 ;i< newList.size();i++) {
                    Log.d("panzqww", "query new book list ("+i+"):" + newList.get(i).toString());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,BookManagerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
