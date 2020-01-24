package com.weihuagu.receiptnotice;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.List;

public class ReceiptnoticeAccessibilityService extends AccessibilityService {
    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtil.debugLogWithDeveloper( "onAccessibilityEvent: " + event.toString());
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
        LogUtil.debugLogWithDeveloper( "onAccessibilityEvent: " + "oninterrupt");
    }

    public void postMessageWithget_alipay_transfer_money(String num){
        LiveEventBus
                .get("get_alipay_transfer_money")
                .post(num);
    }

    public void getAlipayTransferInfo(String classname){

        String transnumid="com.alipay.mobile.chatapp:id/biz_desc";
        LogUtil.debugLogWithDeveloper( "onAccessibilityEvent:窗口状态改变,类名为"+classname);
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
            LogUtil.debugLogWithDeveloper( "onAccessibilityEvent:金额为"+transnum);
            postMessageWithget_alipay_transfer_money(transnum);
        }

    }
}
