// IBookManager.aidl
package com.example.aidltest;
import com.example.aidltest.Book;

// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
