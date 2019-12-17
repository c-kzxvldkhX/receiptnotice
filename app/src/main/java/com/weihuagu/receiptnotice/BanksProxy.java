package com.weihuagu.receiptnotice;
import android.app.Notification;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BanksProxy extends NotificationHandle{
        private BankDistinguisher onedistinguisher=new BankDistinguisher();

        public BanksProxy(String pkgtype,Notification notification,IDoPost postpush){
                super(pkgtype,notification,postpush);
        }

        private String getBankType(){
                return onedistinguisher.distinguishByMessageContent(content);
        }


        public void handleNotification(){
                String banktype=getBankType();
                if(banktype==null)
                        return;

                String type=null;
                if(banktype=="")
                        type="message-bank";
                else
                        type="message-bank-"+banktype;

                Map<String,String> postmap=new HashMap<String,String>();
                postmap.put("type",type);
                postmap.put("time",onedistinguisher.extractTime(content,notitime));
                postmap.put("title","短信银行卡入账");
                postmap.put("phonenum",title);
                postmap.put("money",onedistinguisher.extractMoney(content));
                postmap.put("cardnum",onedistinguisher.extractCardNum(content));
                postmap.put("content",content);

                postpush.doPost(postmap);
                                return ;






        }












}
