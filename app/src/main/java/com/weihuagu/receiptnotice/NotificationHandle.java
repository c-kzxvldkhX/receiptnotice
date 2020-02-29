package com.weihuagu.receiptnotice;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

import com.weihuagu.receiptnotice.action.ActionStatusBarNotification;
import com.weihuagu.receiptnotice.action.IDoPost;
import com.weihuagu.receiptnotice.util.LogUtil;
import com.weihuagu.receiptnotice.util.NotificationUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class NotificationHandle {
        protected String pkgtype;
        protected Notification notification;
        protected Bundle extras;
        protected String title;
        protected String content;
        protected String notitime;
        protected IDoPost postpush;
        protected ActionStatusBarNotification actionstatusbar;
        public StatusBarNotification sbn;
        public NotificationHandle(String rawpkgtype, Notification rawnotification, IDoPost rawpostpush){
                pkgtype=rawpkgtype;
                notification=rawnotification;
                postpush=rawpostpush;

                extras=notification.extras;
                // 获取通知标题
                title = extras.getString(Notification.EXTRA_TITLE, "");
                // 获取通知内容
                content = extras.getString(Notification.EXTRA_TEXT, "");
                long when=notification.when;
                Date date=new Date(when);
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                notitime=format.format(date);


        }

        public void setStatusBarNotification(StatusBarNotification sbn){
                this.sbn=sbn;
        }
        public void setActionStatusbar(ActionStatusBarNotification actionstatusbar){
                this.actionstatusbar=actionstatusbar;
        }
        public  abstract void handleNotification();



        protected void removeNotification(){
                if(actionstatusbar==null|sbn==null)
                        return ;
                actionstatusbar.removeNotification(sbn);
        }

        protected void printNotify(){
                LogUtil.debugLog("-----------------");
                LogUtil.debugLog("接受到app消息");
                LogUtil.debugLog("包名是"+this.pkgtype);
                NotificationUtil.printNotify(notification);
                LogUtil.debugLog("**********************");


        }

        protected void openNotify(){
                PendingIntent pendingIntent = notification.contentIntent;
                try{
                        pendingIntent.send();
                }catch(PendingIntent.CanceledException e){

                }
        }



















}

