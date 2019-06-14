package com.weihuagu.receiptnotice;
import android.app.Notification;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MipushNotificationHandle extends NotificationHandle{
        public MipushNotificationHandle(String pkgtype,Notification notification,IDoPost postpush){
                super(pkgtype,notification,postpush);
        }

        public void handleNotification(){
                if(title.contains("支付宝")){
                        if(content.contains("成功收款")){
                                Map<String,String> postmap=new HashMap<String,String>();
                                postmap.put("type","alipay");
                                postmap.put("time",notitime);
                                postmap.put("title","支付宝支付");
                                postmap.put("money",extractMoney(content));
                                postmap.put("content",content);
                                postpush.doPost(postmap);
                                return ;
                        }
                        if(content.contains("向你转了1笔钱")){
                                Map<String,String> postmap=new HashMap<String,String>();
                                postmap.put("type","alipay-transfer");
                                postmap.put("time",notitime);
                                postmap.put("title","转账");
                                postmap.put("money","-0.00");
                                postmap.put("content",content);
                                postmap.put("transferor",whoTransferred(content));

                                postpush.doPost(postmap);
                                return ;
                        }
                }
                if(title.contains("动账通知")){
                        if(content.contains("入账")&&content.contains("尾号为")){
                                Map<String,String> postmap=new HashMap<String,String>();
                                postmap.put("type","unionpay");
                                postmap.put("time",notitime);
                                postmap.put("title","云闪付扫码收款");
                                postmap.put("money",extractMoney(content));
                                postmap.put("content",content);
                                //postmap.put("payer",getPayer(content));
                                postpush.doPost(postmap);
                                return ;
                        }
                }




        }


        
        private String getPayer(String content){
                Pattern pattern = Pattern.compile("(.*)(通过扫码向您付款)");
                Matcher matcher = pattern.matcher(content);
                if(matcher.find()){
                        String tmp=matcher.group(2);
                        return tmp;
                }else
                        return "";

        }

        private String whoTransferred(String content){
                Pattern pattern = Pattern.compile("(.*)(已成功向你转了)");
                Matcher matcher = pattern.matcher(content);
                if(matcher.find()){
                    String tmp=matcher.group(1);
					return tmp;
                
                }
                else
                    return "";  
        
        }


}
