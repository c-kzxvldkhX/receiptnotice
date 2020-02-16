/*
 * Created By WeihuaGu (email:weihuagu_work@163.com)
 * Copyright (c) 2017
 * All right reserved.
 */

package com.weihuagu.receiptnotice.util;
import java.util.Map;
import java.util.HashMap;

public class ExternalInfoUtil {
        /**
         *
         * 中国银行:106573095566、777795566；

         招商银行:1065795555、10657559555、1065502010095555；

         建设银行:106573095533、80095533；

         工商银行：95588；62019558；010095588；
         　　
         民生银行：10657109095568000、1069088895568；

         农业银行：106366695599、6201395599；

         华夏银行：1065800895577、1069088895577；

         交通银行：106573095559、777795559、555595559000、80095559、

         788895559、797995559
         *
         * */
        public final static Map bankmessagenum = new HashMap() {{
                put("95588", "icbc");
                put("62019558", "icbc");
                put("010095588", "icbc");
                put("106573095566", "boc");
                put("777795566", "boc");
                put("1065795555", "cmb");
                put("10657559555", "cmb");
                put("1065502010095555", "cmb");
                put("106573095533", "ccb");
                put("80095533", "ccb");
                put("10657109095568000", "cmbc");
                put("1069088895568", "cmbc");
        }};

        public final static Map banksname = new HashMap() {{
                put("工商银行", "icbc");
                put("农业银行", "abc");
                put("中国银行", "boc");
                put("建设银行", "ccb");
                put("邮政储蓄银行", "psbc");
                put("招商银行", "cbm");
                put("浦发银行", "spdb");
                put("兴业银行", "cib");
                put("民生银行", "cmbc");
                put("光大银行", "ceb");
                put("网商银行", "my");
                put("北京银行", "bob");
        }};

        public final static String [] maybankmessagefeature = new String[]{
                "收入",
                "存入",
                "转入",
                "入账",
                "来帐"
        };


        public static Map getBanksMessageNum(){
                return bankmessagenum;
        }
        public static Map getAllBanksNameMap(){
                return banksname;
        }
        public static String[] getBankmessageFeature(){
                return maybankmessagefeature;
        }
        public static boolean containsBankmessageFeature(String content){
                for(String x : maybankmessagefeature){
                        if(content.contains(x))
                                return true;
                }
                return false;
        }
        public static Map getCustomPostOption(String custom){
               return getCustomOption(custom);

        }
        
        public static Map getCustomOption(String custom){
                String s[] = custom.split(";");
                Map customoption = new HashMap();
                for(String x : s){
                        String ss[] = getOneitemKeyandValue(x);
                        if(ss!=null){
                                customoption.put(ss[0],ss[1]);
                        }
                }
                if(customoption.size()>0)
                        return customoption;
                else
                        return null;
        }

       public static String[] getOneitemKeyandValue(String item){
               String s[] = item.split(":");
               if(s.length==2)
                       return s;
               else 
                       return null;
       }



}
