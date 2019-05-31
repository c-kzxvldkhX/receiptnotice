package com.weihuagu.receiptnotice;
import android.app.Notification;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class IcbcelifeNotificationHandle extends NotificationHandle{
        public IcbcelifeNotificationHandle(String pkgtype,Notification notification,IDoPost postpush){
                super(pkgtype,notification,postpush);
        }

        public void handleNotification(){
                if(title.contains("工银商户")){
                        if(content.contains("已收到")&&content.contains("元")){
                                Map<String,String> postmap=new HashMap<String,String>();
                                postmap.put("type","icbcelife");
                                postmap.put("time",notitime);
                                postmap.put("title","工银商户之家");
                                postmap.put("money",extractMoney(content));
                                postmap.put("content",content);

                                postpush.doPost(postmap);
                                return ;
                        }
                }



        }

        
		@Override
        protected  String extractMoney(String content){
                Pattern pattern = Pattern.compile("(收到|收款|向你付款)(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?元");
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














}
