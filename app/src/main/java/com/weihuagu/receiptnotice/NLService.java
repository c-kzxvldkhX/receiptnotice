package com.weihuagu.receiptnotice;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.app.Notification;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.os.Bundle;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NLService extends NotificationListenerService implements AsyncResponse {
    public String TAG="NLService";
    public String posturl=null;
    public String getPostUrl(){
	SharedPreferences sp=getSharedPreferences("url", 0);
	this.posturl =sp.getString("posturl", ""); 
	if (posturl==null)
	return null;
	else
	return posturl;
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        super.onNotificationPosted(sbn);
        //这里只是获取了包名和通知提示信息，其他数据可根据需求取，注意空指针就行
	Log.d(TAG,"接受到通知消息");
	if(getPostUrl()==null)
	return;
	Notification notification = sbn.getNotification();
	String pkg = sbn.getPackageName();
    	if (notification == null) {
        	return;
    	}

        long when=notification.when;
        Date date=new Date(when);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String notitime=format.format(date);
        

	Bundle extras = notification.extras;
	if(extras==null)
	return;
	
	String title=null;
	String content=null;
        // 获取通知标题
        title = extras.getString(Notification.EXTRA_TITLE, "");
        // 获取通知内容
        content = extras.getString(Notification.EXTRA_TEXT, "");

	Log.d(TAG,"-----------------");

	//mipush
	if("com.xiaomi.xmsf".equals(pkg)){
		if(title.contains("支付宝")){
			//printNotify(notitime,title,content);
		}
		
	}

	//支付宝
	if("com.eg.android.AlipayGphone".equals(pkg)){
		if(title.contains("支付宝")){
			if(content.contains("成功收款")){
			Map<String,String> postmap=new HashMap<String,String>();
			if(this.posturl!=null){
				postmap.put("time",notitime);
				postmap.put("title","支付宝支付");
				postmap.put("money",extractMoney(content));
				postmap.put("content",content);
				
				doPost(posturl,postmap);
				return ;
			}
			}
		}

		//return;

	}
	//应用管理GCM代收
	if("android".equals(pkg)){
		if(content.contains("微信支付")&&content.contains("收款")){
			Map<String,String> postmap=new HashMap<String,String>();
			if(this.posturl!=null){
				postmap.put("time",notitime);
				postmap.put("title","微信支付");
				postmap.put("money",extractMoney(content));
				postmap.put("content",content);
				doPost(posturl,postmap);
				return ;
			}
		}

	}
	//微信
	if("com.tencent.mm".equals(pkg)){
		if(title.contains("微信支付")&&content.contains("收款")){
			Map<String,String> postmap=new HashMap<String,String>();
			if(this.posturl!=null){
				postmap.put("time",notitime);
				postmap.put("title",title);
				postmap.put("money",extractMoney(content));
				postmap.put("content",content);
				doPost(posturl,postmap);
				return ;
			}
		}
			
			

	}
	
	Log.d(TAG,"这是检测之外的其它通知");
	Log.d(TAG,"包名是"+pkg);
	printNotify(notitime,title,content);
	
	Log.d(TAG,"**********************");
	
        
    }
    
    private void sendBroadcast(String msg) {
        Intent intent = new Intent(getPackageName());
        intent.putExtra("text", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void printNotify(String notitime,String title,String content){
			Log.d(TAG,notitime);
			Log.d(TAG,title);
			Log.d(TAG,content);
	}

    private void doPost(String url,Map<String, String> params){
		Log.d(TAG,"开始准备进行post");
		PostTask mtask = new PostTask();
		mtask.setOnAsyncResponse(this);
		params.put("url",url);
		mtask.execute(params);
	
    }

   private  String extractMoney(String content){
	 Pattern pattern = Pattern.compile("收款(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?元"); 
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
  
    
    @Override
	public void onDataReceivedSuccess(String returnstr) {
		Log.d(TAG,"Post Receive-returned post string");
		Log.d(TAG,returnstr);
		
		
	}
	@Override
	public void onDataReceivedFailed() {
		// TODO Auto-generated method stub
		Log.d(TAG,"Post Receive-post error");
		
	}
}
