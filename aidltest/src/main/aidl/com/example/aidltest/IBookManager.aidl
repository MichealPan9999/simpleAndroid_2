// IBookManager.aidl
package com.example.aidltest;
import com.example.aidltest.Book;
import com.example.aidltest.IOnNewBookListener;

// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerNewBookArrivedListener(IOnNewBookListener listener);
    void unregisterNewBookArrivedListener(IOnNewBookListener listener);
}
