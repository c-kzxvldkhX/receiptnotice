package com.weihuagu.receiptnotice;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.List;

public class ReceiptnoticeAccessibilityService extends AccessibilityService {
    PowerManager pm=null;
    String TAG="onAccessibilityEvent";
    @Override
    public void onServiceConnected(){
        debugLogWithDeveloper("accessibility service connected");
        subMessage();
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        postMessageWithCommonAccessibilityEvent(event.toString());
        debugLogWithDeveloper( event.toString());
        final int eventType = event.getEventType();
        //根据事件回调类型进行处理
        switch (eventType) {
            //当通知栏发生改变时
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:

                break;
            //当窗口的状态发生改变时
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                String className = event.getClassName().toString();
                getAlipayTransferInfo(className);

                break;
        }


    }

    @Override
    public void onInterrupt() {
        debugLogWithDeveloper(  "oninterrupt");
    }

    public void postMessageWithget_alipay_transfer_money(String num){
        LiveEventBus
                .get("get_alipay_transfer_money")
                .post(num);
    }

    public void postMessageWithCommonAccessibilityEvent(String event){
        LiveEventBus.get("get_new_accessibilityevent").post(event);
    }

    public void subMessage(){
        LiveEventBus
                .get("action_request_return", String.class)
                .observeForever( new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        LogUtil.debugLog("收到订阅消息:action_request_return " + s);
                        if(s.equals("return")){
                            performGlobalAction(GLOBAL_ACTION_BACK);

                        }
                    }
                });
        LiveEventBus
                .get("action_request_home", String.class)
                .observeForever( new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        LogUtil.debugLog("收到订阅消息:action_request_home " + s);
                        if(s.equals("home")){
                            performGlobalAction(GLOBAL_ACTION_HOME);

                        }
                    }
                });
    }

    public void getAlipayTransferInfo(String classname){

        String transnumid="com.alipay.mobile.chatapp:id/biz_desc";
        debugLogWithDeveloper( ":窗口状态改变,类名为"+classname);
        if(classname.equals("com.alipay.mobile.chatapp.ui.PersonalChatMsgActivity_")){
            AccessibilityNodeInfo nodepersonalchat=null;
            AccessibilityWindowInfo windowInfopersonalchat=null;
            if(pm.isScreenOn()) {
                 nodepersonalchat= getRootInActiveWindow();
            }else {
                if(Build.VERSION.SDK_INT >= 21) {
                    windowInfopersonalchat = getWindows().get(1);
                    nodepersonalchat=windowInfopersonalchat.getRoot();
                }
            }
            if (nodepersonalchat== null) {
                return;
            }
            // 找到领取红包的点击事件
            List<AccessibilityNodeInfo> list = nodepersonalchat.findAccessibilityNodeInfosByViewId(transnumid);
            String transnum=list.get(list.size()-1).getText().toString();
            debugLogWithDeveloper( ":金额为"+transnum);
            postMessageWithget_alipay_transfer_money(transnum);
        }

    }

    public void debugLogWithDeveloper(String info){
        Log.d(TAG,info);
    }
}
