package com.example.aidltest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private IBookManager mRemoteBookManager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    try {
                        if (mRemoteBookManager!=null){
                            Log.i("panzqww", "Handler --- "+mRemoteBookManager.getBookList().size());
                            List<Book> bookList = mRemoteBookManager.getBookList();
                            for (int i = 0;i<bookList.size();i++)
                            {
                                Log.i("---- panzqww",bookList.get(i).toString());
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mRemoteBookManager = IBookManager.Stub.asInterface(iBinder);
            try {
                List<Book> list = mRemoteBookManager.getBookList();
                Log.d("panzqww","query book list ,list type :"+list.getClass().getCanonicalName());
                for (int i =0 ;i< list.size();i++) {
                    Log.d("panzqww", "query book list ("+i+"):" + list.get(i).toString());
                }
                Book newBook = new Book(3,"JAVA");
                mRemoteBookManager.addBook(newBook);
                Log.d("panzqww","add new book:"+newBook.toString());
                List<Book> newList = mRemoteBookManager.getBookList();
                for (int i =0 ;i< newList.size();i++) {
                    Log.d("panzqww", "query new book list ("+i+"):" + newList.get(i).toString());
                }
                //注册
                mRemoteBookManager.registerNewBookArrivedListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mRemoteBookManager = null;
            Log.d("panzqww","binder died.");
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
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive())
        {
            try {
                Log.d("panzqww","unregister listener : "+mOnNewBookArrivedListener.getClass());
                handler.removeMessages(MESSAGE_NEW_BOOK_ARRIVED);
                mRemoteBookManager.unregisterNewBookArrivedListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
            }
        }
        unbindService(mConnection);
    }

    private IOnNewBookListener mOnNewBookArrivedListener = new IOnNewBookListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    };
}
