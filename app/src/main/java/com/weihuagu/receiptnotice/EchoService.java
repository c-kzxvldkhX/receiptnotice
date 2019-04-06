package com.weihuagu.receiptnotice;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.System;
import android.app.IntentService;
import android.content.Intent;
import io.socket.client.IO;
import io.socket.client.Socket;
public class EchoService extends IntentService {
private String echoserver=null;
public EchoService () {
        super("EchoService e");
    }

@Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtil preference=new PreferenceUtil(getBaseContext());
        echoserver=preference.getEchoServer();
                 
        
    }

@Override
    protected void onHandleIntent(Intent intent) {
            PreferenceUtil preference=new PreferenceUtil(getBaseContext());
            if(!preference.isEncrypt())
                    return;
             if(echoserver!=null){
                    LogUtil.debugLog("start to echo");
    try {
                   Socket mSocket= IO.socket(echoserver);
                    Date date=new Date(System.currentTimeMillis());
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time=format.format(date);
                   mSocket.connect();
                   mSocket.emit("echo", time);
                   mSocket.disconnect();
} catch (URISyntaxException e) {}
        }






}












}
