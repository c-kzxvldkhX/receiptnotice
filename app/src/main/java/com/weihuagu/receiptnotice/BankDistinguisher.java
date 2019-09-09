package com.weihuagu.receiptnotice;
import android.app.Notification;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BankDistinguisher extends BankDistinguisher{
        public BankDistinguisher(){
        }
        public String distinguishByNum(String num){
                Map <String, String> map=ExternalInfoUtil.getBanksMessageNum();
                String whatsback=map.get(num);
                if(whatsback)
                        return whatsback;
                else
                        return "";

        }

        public String distinguishByMessageContent(String content){
                if(!content.contains("银行"))
                        return null;
                Map <String,String> map=ExternalInfoUtil.getAllBanksNameMap();
                for (String key : map.keySet()) {
                        if(content.contains(key)
                                        return map.get(key);
                }
                return "";



        }














}

