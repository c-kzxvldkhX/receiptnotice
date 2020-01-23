package com.weihuagu.receiptnotice;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class ReceiptnoticeAccessibilityService extends AccessibilityService {
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

                break;
        }


    }

    @Override
    public void onInterrupt() {

    }

    public void getAlipayTransferInfo(){

    }
}
