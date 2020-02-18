package com.weihuagu.receiptnotice;

import android.app.PendingIntent;
import android.os.Bundle;
import android.app.Notification;
import android.service.notification.StatusBarNotification;

import com.weihuagu.receiptnotice.action.ActionStatusBarNotification;
import com.weihuagu.receiptnotice.action.IDoPost;
import com.weihuagu.receiptnotice.util.LogUtil;
import com.weihuagu.receiptnotice.util.NotificationUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class PmentayNotificationHandle extends NotificationHandle{
        protected String pkgtype;
        protected Notification notification;
        protected Bundle extras;
        protected String title;
        protected String content;
        protected String notitime;
        protected IDoPost postpush;
        protected ActionStatusBarNotification actionstatusbar;
        public StatusBarNotification sbn;
        public PmentayNotificationHandle(String pkgtype, Notification notification, IDoPost postpush){
                super(pkgtype, notification, postpush);

        }

        public void setStatusBarNotification(StatusBarNotification sbn){
                this.sbn=sbn;
        }
        public void setActionStatusbar(ActionStatusBarNotification actionstatusbar){
                this.actionstatusbar=actionstatusbar;
        }
        public  abstract void handleNotification();
        protected  String extractMoney(String content){
                Pattern pattern = Pattern.compile("(收款|收款￥|向你付款|向您付款|入账)(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?元");
                Matcher matcher = pattern.matcher(content);
                if(matcher.find()){
                        String tmp=matcher.group();
                        Pattern patternnum = Pattern.compile("(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?");
                        Matcher matchernum = patternnum.matcher(tmp);
                        if(matchernum.find())
                                return matchernum.group();
                        return null;
                }else
                        return null;


        }
        protected boolean predictIsPost(String content){
                Pattern pattern = Pattern.compile("(收到|收款|向你付款|向您付款|入账)(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?元");
                Matcher matcher = pattern.matcher(content);
                if(matcher.find())
                        return true;
                else
                        return false;

        }

        protected void removeNotification(){
                if(actionstatusbar==null|sbn==null)
                        return ;
                if(predictIsPost(content))
                        actionstatusbar.removeNotification(sbn);
        }
        protected void printNotify(){
                LogUtil.debugLog("-----------------");
                LogUtil.debugLog("接受到支付类app消息");
                LogUtil.debugLog("包名是"+this.pkgtype);
                NotificationUtil.printNotify(notification);
                LogUtil.debugLog("**********************");


        }






















}

