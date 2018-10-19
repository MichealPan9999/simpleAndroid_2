package com.example.bindertest;

import android.os.IBinder;
import android.os.RemoteException;

public class BinderPoolImpl extends IBinderPool.Stub {
    public BinderPoolImpl() {
        super();
    }

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        switch (binderCode) {
            case Contents.BINDER_SECURUITY_CENTER:
                binder = new SecurityCenterImpl();
                break;
            case Contents.BINDER_COMPUTE:
                binder = new ComputeImpl();
                break;
        }
        return binder;
    }
}
