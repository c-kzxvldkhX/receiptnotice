package com.weihuagu.receiptnotice;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
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
    private PowerManager.WakeLock mWakeLock = null;
    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock kl;
    private String lastpoststr = "";
    private String lastnotistr = "";
    private void setLastPostStr(String str){
        lastpoststr=str;
    }

    private void setLastNotiStr(String str){
        lastnotistr=str;
    }
    @Override
    public void onServiceConnected(){
        debugLogWithDeveloper("accessibility service connected");
        subMessage();
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl = mKeyguardManager.newKeyguardLock("myapp:kllock");



    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

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

    public void postMessageWithget_alipay_transfer_money(AlipayTransferBean transferbean){
        LiveEventBus
                .get("get_alipay_transfer_money")
                .post(transferbean);
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
        LiveEventBus
                .get("message_noti_alipay_transfer_arrive", String.class)
                .observeForever( new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        LogUtil.debugLog("收到订阅消息:message_noti_alipay_transfer_arrive " + s);
                        wakeAndUnlock(true);
                        setLastNotiStr(s);

                    }
                });

        LiveEventBus
                .get("update_laststr", String.class)
                .observeForever( new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        LogUtil.debugLog("收到订阅消息:update_laststr " + s);
                        setLastPostStr(s);
                    }
                });

    }

    public void getAlipayTransferInfo(String classname){

        String transnumid="com.alipay.mobile.chatapp:id/biz_desc";
        String transremarkid="com.alipay.mobile.chatapp:id/biz_title";
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
            try {
                List<AccessibilityNodeInfo> list = nodepersonalchat.findAccessibilityNodeInfosByViewId(transnumid);
                AccessibilityNodeInfo thelastnode= list.get(list.size() - 1);
                String transnum = thelastnode.getText().toString();
                List<AccessibilityNodeInfo> remarklist=thelastnode.getParent().findAccessibilityNodeInfosByViewId(transremarkid);
                String transremark=remarklist.get(remarklist.size()-1).getText().toString();
                debugLogWithDeveloper(":金额为" + transnum);
                debugLogWithDeveloper(":备注为" + transremark);
                AlipayTransferBean transferbean=new AlipayTransferBean();
                transferbean.setNum(transnum);
                transferbean.setRemark(transremark);
                if(!lastpoststr.equals(lastnotistr))
                postMessageWithget_alipay_transfer_money(transferbean                                                        );
            }catch (ArrayIndexOutOfBoundsException e){

            }
        }

    }

    public void debugLogWithDeveloper(String info){
        Log.d(TAG,info);
    }

    /**
     * 唤醒屏幕和解锁
     * @param unLock 是否点亮屏幕
     */
    private void wakeAndUnlock(boolean unLock)
    {
        if(unLock)
        {
            //若为黑屏状态则唤醒屏幕
            if(!pm.isScreenOn()) {
                //获取电源管理器对象，ACQUIRE_CAUSES_WAKEUP这个参数能从黑屏唤醒屏幕
                mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myapp:bright");
                //点亮屏幕
                mWakeLock.acquire();
                kl.disableKeyguard();
                Log.i("QHB", "亮屏");
            }
        }
        else
        {
            //若之前唤醒过屏幕则释放使屏幕不保持常亮
            if(mWakeLock != null) {
                mWakeLock.release();
                mWakeLock = null;
                Log.i("QHB", "锁屏");
            }
        }
    }
}
