// IBookManager.aidl
package com.example.panzq.simpleandroid_2;
import com.example.panzq.simpleandroid_2.Book;

// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
