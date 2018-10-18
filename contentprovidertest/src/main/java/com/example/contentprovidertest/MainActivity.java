package com.example.contentprovidertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Uri uri = Uri.parse("content://com.example.contentprovidertest.BookProvider");
        getContentResolver().query(uri,null,null,null,null);
        getContentResolver().query(uri,null,null,null,null);
        getContentResolver().query(uri,null,null,null,null);*/
        Uri bookUri = BookProvider.BOOK_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put("_id",6);
        values.put("name","Android 编程艺术");
        getContentResolver().insert(bookUri,values);
        Cursor bookCusor = getContentResolver().query(bookUri,new String[]{"_id","name"},null,null,null);
        while(bookCusor.moveToNext())
        {
            Book book = new Book();
            book.bookId = bookCusor.getInt(0);
            book.bookName = bookCusor.getString(1);
            Log.d("panzqww","book-----"+book.toString());
        }
        bookCusor.close();

        Uri userUri = BookProvider.USER_CONTENT_URI;
        Cursor userCursor = getContentResolver().query(userUri,new String[]{"_id","name","sex"},null,null,null);
        while (userCursor.moveToNext()){
            User user = new User();
            user.userId= userCursor.getInt(0);
            user.userName = userCursor.getString(1);
            user.isMale = userCursor.getInt(2) == 1;
            Log.d("panzqww","user-----"+user.toString());
        }
        userCursor.close();

    }
}
