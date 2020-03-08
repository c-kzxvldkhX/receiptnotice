package com.weihuagu.receiptnotice.pushclassification.pmentay;
import android.app.Notification;

import com.weihuagu.receiptnotice.action.IDoPost;
import com.weihuagu.receiptnotice.PmentayNotificationHandle;

import java.util.Map;
import java.util.HashMap;


public class XposedmodulePmentayNotificationHandle extends PmentayNotificationHandle {
        public XposedmodulePmentayNotificationHandle(String pkgtype, Notification notification, IDoPost postpush){
                super(pkgtype,notification,postpush);
        }

        public void handleNotification(){
                if(content.contains("微信支付")&&content.contains("收款")){
                        Map<String,String> postmap=new HashMap<String,String>();
                                postmap.put("type","wechat");
                                postmap.put("time",notitime);
                                postmap.put("title","微信支付");
                                postmap.put("money",extractMoney(content));
                                postmap.put("content",content);
                                postpush.doPost(postmap);
                                return ;
                }




        }













}
