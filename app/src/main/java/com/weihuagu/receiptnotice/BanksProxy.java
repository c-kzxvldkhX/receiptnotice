package com.weihuagu.receiptnotice;
import android.app.Notification;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BanksProxy extends NotificationHandle{
        public BanksProxy(String pkgtype,Notification notification,IDoPost postpush){
                super(pkgtype,notification,postpush);
        }
        
        private String getBankType(){
                BankDistinguisher onedistinguisher=new BankDistinguisher();
                return onedistinguisher.distinguishByMessageContent(content);
        }
        protected  String extractMoney(String content){
                Pattern pattern = Pattern.compile("(收入|存入|转入|入账)(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?元");
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


        public void handleNotification(){
                String banktype=getBankType();
                String type=null;
                if(banktype!=null)
                        if(banktype=="")
                                ;
                        else
                                ;



        }












}
