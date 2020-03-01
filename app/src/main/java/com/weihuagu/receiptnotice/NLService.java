package com.weihuagu.receiptnotice;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.app.Notification;
import android.service.notification.StatusBarNotification;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.weihuagu.receiptnotice.action.ActionStatusBarNotification;
import com.weihuagu.receiptnotice.action.HandlePost;
import com.weihuagu.receiptnotice.util.LogUtil;
import com.weihuagu.receiptnotice.util.NotificationUtil;
import com.weihuagu.receiptnotice.util.PreferenceUtil;
import com.weihuagu.receiptnotice.util.message.MessageConsumer;



public class NLService extends NotificationListenerService implements  ActionStatusBarNotification, MessageConsumer {
        private String TAG="NLService";
        private String posturl=null;
        private Context context=null;
        private String getPostUrl(){
                SharedPreferences sp=getSharedPreferences("url", 0);
                this.posturl =sp.getString("posturl", "");
                if (posturl==null)
                        return null;
                else
                        return posturl;
        }


        @Override
        public void onNotificationPosted(StatusBarNotification sbn) {
                //        super.onNotificationPosted(sbn);
                //这里只是获取了包名和通知提示信息，其他数据可根据需求取，注意空指针就行

                if(getPostUrl()==null)
                        return;

                Notification notification = sbn.getNotification();
                String pkg = sbn.getPackageName();
                if (notification == null) {
                        return;
                }

                Bundle extras = notification.extras;
                if(extras==null)
                        return;

                //接受推送处理
                NotificationHandle notihandle =new NotificationHandleFactory().getNotificationHandle(pkg,notification,new HandlePost());
                if(notihandle!=null){
                        notihandle.setStatusBarNotification(sbn);
                        notihandle.setActionStatusbar(this);
                        notihandle.printNotify();
                        notihandle.handleNotification();
                        notihandle.removeNotification();
                        return;
                }
                LogUtil.debugLog("-----------------");
                LogUtil.debugLog("接受到通知消息");
                LogUtil.debugLog("这是检测之外的其它通知");
                LogUtil.debugLog("包名是"+pkg);
                NotificationUtil.printNotify(notification);

                LogUtil.debugLog("**********************");


        }

        @Override
        public void onNotificationRemoved(StatusBarNotification sbn) {
                if (Build.VERSION.SDK_INT >19)
                        super.onNotificationRemoved(sbn);
        }

        public void removeNotification(StatusBarNotification sbn){
                PreferenceUtil preference=new PreferenceUtil(getBaseContext());
                if(preference.isRemoveNotification()){
                        if (Build.VERSION.SDK_INT >=21)
                                cancelNotification(sbn.getKey());
                        else
                                cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
                        sendToast("receiptnotice移除了包名为"+sbn.getPackageName()+"的通知");
                }
        }

        private void sendBroadcast(String msg) {
               Intent intent = new Intent(getPackageName());
                intent.putExtra("text", msg);
              LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        private void sendToast(String msg){
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        }



        public void subMessage() {
                LiveEventBus
                    .get("get_alipay_transfer_money", TestBeanWithPostFullInformationMap.class)
                    .observeForever( new Observer<TestBeanWithPostFullInformationMap>() {
                        @Override
                        public void onChanged(@Nullable TestBeanWithPostFullInformationMap testpostbean) {

                        }
                    });

        }
}
