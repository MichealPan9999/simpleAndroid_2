package com.example.sockettest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPService extends android.app.Service {
    private static final String TAG = "panzqww";
    private boolean mIsServiceDestoeyed = false;
    private String[] mDefinedMessages =new String[] {
            "你好呀,哈哈",
            "请问你叫什么名字呀？",
            "今天北京天气不错啊，shy",
            "你知道吗？我可是可以和多个人同时聊天的哦",
            "不笑运气差，一笑就脸大，笑哭"
    };

    public TCPService() {
    }

    @Override
    public void onCreate() {
        new Thread(new TCPRunnable()).start();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoeyed = true;
        super.onDestroy();
    }

    private class TCPRunnable implements Runnable {
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                //监听本地8688端口
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                Log.d(TAG,"establish tcp server failed,prot:8688");
                e.printStackTrace();
                return;
            }
            while(!mIsServiceDestoeyed)
            {
                try {
                    final Socket client = serverSocket.accept();
                    Log.d(TAG,"TCPService : accept");
                    new Thread()
                    {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void responseClient(Socket client) throws  IOException{
        //接收客户端消息
        BufferedReader in = new BufferedReader( new InputStreamReader(client.getInputStream()));
        //用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
        out.println("欢迎来到聊天室！");
        while(!mIsServiceDestoeyed)
        {
            String str = in.readLine();
            Log.d(TAG,"msg from client:"+str);
            if (str == null)
            {
                Log.d(TAG,"客户端发送消息为空，断开连接");
                break;
            }
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            out.println(msg);
        }
        Log.d(TAG,"server quit .");
        //关闭流
        MyUtils.close(out);
        MyUtils.close(in);
        client.close();
        Log.d(TAG,"sever socket close...");

    }
}
