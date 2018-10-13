// IOnNewBookListener.aidl
package com.example.aidltest;
import com.example.aidltest.Book;

// Declare any non-default types here with import statements

interface IOnNewBookListener {
    void onNewBookArrived(in Book newBook);
}
