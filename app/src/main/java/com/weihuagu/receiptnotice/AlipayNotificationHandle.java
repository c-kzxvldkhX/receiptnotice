package com.weihuagu.receiptnotice;
import android.app.Notification;
import android.content.Intent;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AlipayNotificationHandle extends NotificationHandle{
        Map<String,String> tmppostmap=new HashMap<String,String>();
        public AlipayNotificationHandle(String pkgtype,Notification notification,IDoPost postpush){
                super(pkgtype,notification,postpush);

        }

        public void handleNotification(){
                if(title.contains("支付宝")){
                        if(content.contains("成功收款") | content.contains("向你付款")){
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

                                //use acceesbility service to get info
                                if (AuthorityUtil.isAccessibilitySettingsOn(MainApplication.getAppContext())) {
                                        tmppostmap.putAll(postmap);
                                        subMessage();
                                        //open notify
                                        postMessageWithReceiptAlipayTransfer(tmppostmap.get("time")+tmppostmap.get("content"));
                                        this.openNotify();
                                        return ;
                                }

                                postpush.doPost(postmap);
                                return ;
                        }
                }



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

        public void postActionRequestWithReturn(){
                LiveEventBus
                        .get("action_request_return")
                        .post("return");
        }

        public void postActionRequestWithHome(){
                LiveEventBus
                        .get("action_request_home")
                        .post("home");
        }

        public void postMessageWithReceiptAlipayTransfer(String transferinfo){
                LiveEventBus
                        .get("message_noti_alipay_transfer_arrive")
                        .post(transferinfo);
        }

        public void postMessageWithUpdateTheLastPostString(String str){
                LiveEventBus.get("update_laststr").post(str);
        }

        private void subMessage() {
                LiveEventBus
                        .get("get_alipay_transfer_money", AlipayTransferBean.class)
                        .observeForever( new Observer<AlipayTransferBean>() {
                                @Override
                                public void onChanged(@Nullable AlipayTransferBean transfer) {
                                        LogUtil.debugLog("收到订阅消息:get_alipay_transfer_money " + "money"+transfer.getNum()+"备注"+transfer.getRemark());
                                        postActionRequestWithReturn();
                                        postActionRequestWithHome();
                                        tmppostmap.put("money",transfer.getNum());
                                        tmppostmap.put("remark",transfer.getRemark());
                                        postMessageWithUpdateTheLastPostString(tmppostmap.get("time")+tmppostmap.get("content"));
                                        postpush.doPost(tmppostmap);
                                }
                        });
        }


}
