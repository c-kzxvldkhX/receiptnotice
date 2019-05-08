package com.weihuagu.receiptnotice;
import android.app.Notification;

import java.util.Map;
import java.util.HashMap;


public class UnionpayNotificationHandle extends NotificationHandle{
        public UnionpayNotificationHandle(String pkgtype,Notification notification,IDoPost postpush){
                super(pkgtype,notification,postpush);
        }

        public void handleNotification(){
                if(title.contains("消息推送")&&content.contains("云闪付收款")){
                        Map<String,String> postmap=new HashMap<String,String>();
                                postmap.put("type","unionpay");
                                postmap.put("time",notitime);
                                postmap.put("title",title);
                                postmap.put("money",extractMoney(content));
                                postmap.put("content",content);
                                postpush.doPost(postmap);
                                return ;
                }



        }













}
