package com.example.contentprovidertest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BookProvider extends ContentProvider {
    public static final String TAG = "panzqww";
    public static final String AUTHORITY = "com.example.contentprovidertest.BookProvider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/user");
    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    private Context mContext;
    private SQLiteDatabase mDb;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        mUriMatcher.addURI(AUTHORITY,"book",BOOK_URI_CODE);
        mUriMatcher.addURI(AUTHORITY,"user",USER_URI_CODE);
    }

    private String getTableName(Uri uri)
    {
        String tableName = null;
        switch (mUriMatcher.match(uri))
        {
            case BOOK_URI_CODE:
                tableName = DBOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DBOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        Log.d(TAG,"tableName = "+(tableName==null?"NULL":tableName));
        return tableName;
    }
    @Override
    public boolean onCreate() {
        Log.i(TAG,"onCreate,current thread:" + Thread.currentThread().getName());
        mContext = getContext();
        initProviderData();
        return true;
    }
    private void initProviderData()
    {
        mDb = new DBOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("delete from "+DBOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from "+DBOpenHelper.USER_TABLE_NAME);

        mDb.execSQL("insert into book values(3,'Android');");
        mDb.execSQL("insert into book values(4,'IOS');");
        mDb.execSQL("insert into book values(5,'Java');");
        mDb.execSQL("insert into user values(1,'zhangsan',1);");
        mDb.execSQL("insert into user values(2,'lisi',0);");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.i(TAG,"query,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null)
        {
            throw  new IllegalArgumentException("Unsupported URI :" + uri);
        }
        return mDb.query(table,projection,selection,null,null,sortOrder,null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.i(TAG,"getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.i(TAG,"insert");
        String table = getTableName(uri);
        if (table == null)
        {
            throw  new IllegalArgumentException("Unsupported URI :" + uri);
        }
        mDb.insert(table,null,contentValues);
        mContext.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i(TAG,"delete");
        String table = getTableName(uri);
        if (table == null)
        {
            throw  new IllegalArgumentException("Unsupported URI :" + uri);
        }
        int count = mDb.delete(table,selection,selectionArgs);
        if (count > 0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i(TAG,"update");
        String table = getTableName(uri);
        if (table == null)
        {
            throw  new IllegalArgumentException("Unsupported URI :" + uri);
        }
        int row = mDb.update(table,contentValues,selection,selectionArgs);
        if (row > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }
}
