package com.example.messengertest;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.messengertest.contants.MyConstants;

public class MessengerService extends Service {
    public MessengerService() {
    }

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FROM_CLIENT:
                    Log.d("panzqww", "receive msg from client : " + msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Message relpyMessage = Message.obtain(null,MyConstants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","嗯，你的消息我已经收到，稍后会回复你。");
                    relpyMessage.setData(bundle);
                    try {
                        client.send(relpyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {

        return mMessenger.getBinder();
    }
}
