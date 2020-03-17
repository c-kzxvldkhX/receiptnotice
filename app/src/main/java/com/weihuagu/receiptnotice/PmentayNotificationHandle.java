package com.weihuagu.receiptnotice;

import android.app.Notification;
import android.service.notification.StatusBarNotification;

import com.weihuagu.receiptnotice.action.ActionStatusBarNotification;
import com.weihuagu.receiptnotice.action.IDoPost;
import com.weihuagu.receiptnotice.util.LogUtil;
import com.weihuagu.receiptnotice.util.NotificationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class PmentayNotificationHandle extends NotificationHandle{
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

        protected  String extractMoney(String content){
                Pattern pattern = Pattern.compile("(收款|收款￥|向你付款|向您付款|入账|到帐)(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?元");
                Matcher matcher = pattern.matcher(content);
                List<String> list = new ArrayList<>();
                while(matcher.find()){
                        list.add(matcher.group());
                }
                if(list.size()>0){
                        String tmp=list.get(list.size()-1);
                        System.out.println(tmp);
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
                NotificationUtil.printNotify(this.notification);
                LogUtil.debugLog("**********************");


        }






















}

