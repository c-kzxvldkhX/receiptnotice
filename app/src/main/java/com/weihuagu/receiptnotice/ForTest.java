package com.weihuagu.receiptnotice;

import android.app.Application;
import android.app.Notification;
import android.content.Intent;
import android.content.Context.*;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.widget.Toast;


import com.weihuagu.receiptnotice.action.HandlePost;
import com.weihuagu.receiptnotice.view.MainActivity;

public class ForTest {
    public void makeAPostTest(String pkg, Notification notification){
        //接受推送处理
        NotificationHandle notihandle =new NotificationHandleFactory().getNotificationHandle(pkg,notification,new HandlePost());
        if(notihandle!=null){
            notihandle.printNotify();
            notihandle.handleNotification();
            notihandle.removeNotification();
            return;
        }
    }
    private void GenerateNotification() {
        Intent intent = new Intent(MainApplication.getAppContext(), MainActivity.class);
        PendingIntent pintent = PendingIntent.getActivity(MainApplication.getAppContext(), 0, intent, 0);
        Builder builder = new Builder(MainApplication.getAppContext());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("这是手记状态栏提示");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("woshi biaoti");
        builder.setContentText("标题内容我是");
        builder.setContentIntent(pintent);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setDefaults(Notification.DEFAULT_LIGHTS);
        // builder.getNotification();//4.0以及以下版本用这个获取notification
        Notification notification = builder.build();// 4.1以及以上版本用这个
        Toast.makeText(MainApplication.getAppContext(), "生成通知", 50).show();
        NotificationManager manager = (NotificationManager) MainApplication.getAppContext()
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        manager.notify(23, notification);// 发出通知
    }

}
