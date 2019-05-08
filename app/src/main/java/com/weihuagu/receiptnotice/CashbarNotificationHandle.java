package com.weihuagu.receiptnotice;
import android.app.Notification;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CashbarNotificationHandle extends NotificationHandle{
        public CashbarNotificationHandle(String pkgtype,Notification notification,IDoPost postpush){
                super(pkgtype,notification,postpush);
        }

        public void handleNotification(){
                if(title.contains("收钱吧")){
                        if(content.contains("成功收款") | content.contains("向你付款")){
                                Map<String,String> postmap=new HashMap<String,String>();
                                postmap.put("type",getCashbarType(content));
                                postmap.put("time",notitime);
                                postmap.put("title","支付宝支付");
                                postmap.put("money",extractMoney(content));
                                postmap.put("content",content);

                                postpush.doPost(postmap);
                                return ;
                        }
                }



        }


        private String getCashbarType(String content){
                Pattern pattern = Pattern.compile("(来自)(微信|支付宝|.*)");
                Matcher matcher = pattern.matcher(content);
                if(matcher.find()){
                        String tmp=matcher.group(2);
                        
                        return "cashbar-"+transType(tmp);
                }else
                        return "";

        }

        private String transType(String chinesetype){
                if(chinesetype.equals("微信"))
                        return "wechat";
                if(chinesetype.equals("支付宝"))
                        return "alipay";
                else return chinesetype;
        }











}
