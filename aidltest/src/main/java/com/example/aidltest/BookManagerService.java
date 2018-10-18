package com.example.aidltest;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {
    public BookManagerService() {
    }

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    //private CopyOnWriteArrayList<IOnNewBookListener> mListenerList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookListener> mListenerList = new RemoteCallbackList<IOnNewBookListener>();
    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerNewBookArrivedListener(IOnNewBookListener listener) throws RemoteException {
            /*if (mListenerList.contains(listener)) {
                Log.d("panzqww", listener + " already exists");
            } else {
                mListenerList.add(listener);
            }
            Log.d("panzqww", "registerListener size : " + mListenerList.size());
            */
            mListenerList.register(listener);
        }

        @Override
        public void unregisterNewBookArrivedListener(IOnNewBookListener listener) throws RemoteException {
            /*if (mListenerList.contains(listener)) {
                mListenerList.remove(listener);
                Log.d("panzqww", "unregister listener successed.");
            } else {
                Log.d("panzqww", "not found , cannot unregister");
            }
            Log.d("panzqww", "unregisterListener current size:" + mListenerList.size());
            */
            mListenerList.unregister(listener);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.example.aidltest.permission.ACCESS_BOOK_SERVICE");
        Log.d("panzqww","check = "+check);
        if (check == PackageManager.PERMISSION_DENIED)
        {
            Log.d("panzqww","------没有权限"+"com.example.aidltest.permission.ACCESS_BOOK_SERVICE");
            return null;
        }
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));
        new Thread(new ServiceWorker()).start();
    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {

            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        /*Log.i("panzqww", "onNewBookArrived size:" + mListenerList.size());
        for (int i = 0; i < mListenerList.size(); i++) {
            IOnNewBookListener listener = mListenerList.get(i);
            Log.i("panzqww", "listener: " + listener);
            listener.onNewBookArrived(book);
        }*/
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookListener l = mListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestoryed.set(true);
    }
}
