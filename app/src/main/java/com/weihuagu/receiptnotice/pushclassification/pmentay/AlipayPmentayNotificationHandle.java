package com.weihuagu.receiptnotice.pushclassification.pmentay;
import android.app.Notification;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.weihuagu.receiptnotice.filteringmiddleware.AlipayTransferBean;
import com.weihuagu.receiptnotice.util.AuthorityUtil;
import com.weihuagu.receiptnotice.action.IDoPost;
import com.weihuagu.receiptnotice.util.LogUtil;
import com.weihuagu.receiptnotice.MainApplication;
import com.weihuagu.receiptnotice.util.message.MessageConsumer;
import com.weihuagu.receiptnotice.util.message.MessageSendBus;
import com.weihuagu.receiptnotice.PmentayNotificationHandle;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AlipayPmentayNotificationHandle extends PmentayNotificationHandle implements MessageConsumer {
        Map<String,String> tmppostmap=new HashMap<String,String>();
        public AlipayPmentayNotificationHandle(String pkgtype, Notification notification, IDoPost postpush){
                super(pkgtype,notification,postpush);

        }
        public void handleNotification(){
                if(title.contains("支付宝") | title.contains("收钱码") | title.contains("收款通知")){
                        //不可将转账判断延后放，以防止通过昵称虚构金额
                        if(content.contains("向你转了1笔钱")){
                                transfercodePush();
				return ;
                        }

                        if(content.contains("成功收款") | content.contains("向你付款")){
                                collectioncodePush(true);
                                return ;
                        }
                }
		if(isInfoHideInTitle()){
			collectioncodePush(false);
			return ;
		}
			



        }

	private void transfercodePush(){
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
                                        MessageSendBus.postMessageWithReceiptAlipayTransfer(tmppostmap.get("time")+tmppostmap.get("content"));
                                        this.openNotify();
                                        return ;
                                }

                                postpush.doPost(postmap);
                                return ;
		
	}
	
	private void collectioncodePush(boolean isinfoincontent){
		  Map<String,String> postmap=new HashMap<String,String>();
                  postmap.put("type","alipay");
                  postmap.put("time",notitime);
                  postmap.put("title","支付宝支付");
		  if(isinfoincontent){
                  	postmap.put("money",extractMoney(content));
                  	postmap.put("content",content);
		  }else
		  {
			postmap.put("money",extractMoney(title));
                  	postmap.put("content",title);  
		  }

                  postpush.doPost(postmap);
                  return ;
	}
	
	private boolean isInfoHideInTitle(){
		if(title.contains("成功收款")&&content.contains("立即查看"))
			return true;
		return false;
			
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

        public void subMessage() {
                LiveEventBus
                        .get("get_alipay_transfer_money", AlipayTransferBean.class)
                        .observeForever( new Observer<AlipayTransferBean>() {
                                @Override
                                public void onChanged(@Nullable AlipayTransferBean transfer) {
                                        if(tmppostmap.size()==0)
                                                return;
                                        LogUtil.debugLog("收到订阅消息:get_alipay_transfer_money " + "money"+transfer.getNum()+"备注"+transfer.getRemark());
                                        MessageSendBus.postActionRequestWithReturn();
                                        MessageSendBus.postActionRequestWithHome();
                                        tmppostmap.put("money",transfer.getNum());
                                        tmppostmap.put("remark",transfer.getRemark());
                                        MessageSendBus.postMessageWithUpdateTheLastPostString(tmppostmap.get("time")+tmppostmap.get("content"));
                                        postpush.doPost(tmppostmap);
                                        tmppostmap=new HashMap<String,String>();
                                        return;
                                }
                        });
        }


}
