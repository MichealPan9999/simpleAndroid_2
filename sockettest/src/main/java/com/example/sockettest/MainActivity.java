package com.example.sockettest;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "panzqww";

    private Socket mClientSocket;
    private PrintWriter mPrintWrite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent service = new Intent(this,TCPService.class);
        startService(service);
        new Thread(){
            @Override
            public void run() {
                connectTcpServer();
                super.run();
            }
        }.start();

    }

    private void connectTcpServer() {
        Socket socket = null;
        while(socket == null)
        {
            try {
                socket = new Socket("10.0.2.15",8688);
                mClientSocket = socket;
                mPrintWrite = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                Log.d(TAG,"connect server success.");
            } catch (IOException e) {
                SystemClock.sleep(2000);
                e.printStackTrace();
                Log.d(TAG,"connect tcp server failed ,retry...");
            }
        }
        //接收服务器的消息
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(!MainActivity.this.isFinishing())
            {
                String msg = br.readLine();
                if (msg != null)
                {
                    String time = formateDateTime(System.currentTimeMillis());
                    final String showedMsg = "server "+time+":"+msg+"\n";
                    Log.d(TAG,showedMsg);
                    SystemClock.sleep(2000);
                    mPrintWrite.println("client :你好服务器，我是客户端！");
                }
            }
            Log.d(TAG,"client quit...");
            MyUtils.close(mPrintWrite);
            MyUtils.close(br);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mClientSocket != null)
        {
            try {
                mClientSocket.shutdownOutput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private String formateDateTime(long l) {
        return new SimpleDateFormat("(HH:mm:ss)").format(new Date(l));
    }
}
