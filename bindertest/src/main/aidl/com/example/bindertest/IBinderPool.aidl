// IBinderPool.aidl
package com.example.bindertest;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
