package com.weihuagu.receiptnotice.util.message;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.weihuagu.receiptnotice.filteringmiddleware.AlipayTransferBean;
import com.weihuagu.receiptnotice.TestBeanWithPostFullInformationMap;

public class MessageSendBus {
    //请求硬件模拟类
    public static void postActionRequestWithReturn(){
        LiveEventBus
                .get("action_request_return")
                .post("return");
    }

    public static void postActionRequestWithHome(){
        LiveEventBus
                .get("action_request_home")
                .post("home");
    }
    //收到活动改变等检测信息
    public static void postMessageWithCommonAccessibilityEvent(String event){
        LiveEventBus.get("get_new_accessibilityevent").post(event);
    }

    //消息发送类
    public static void postMessageWithFinishedonePost(String[] returnstr){
        LiveEventBus
                .get("message_finished_one_post")
                .post(returnstr);
    }
    public static void postMessageWithReceiptAlipayTransfer(String transferinfo){
        LiveEventBus
                .get("message_noti_alipay_transfer_arrive")
                .post(transferinfo);
    }

    public static void postMessageWithUpdateTheLastPostString(String str){
        LiveEventBus.get("update_laststr").post(str);
    }

    public static void postMessageWithget_alipay_transfer_money(AlipayTransferBean transferbean){
        LiveEventBus
                .get("get_alipay_transfer_money")
                .post(transferbean);
    }

    //模型改变通知界面类
    public static void postInterfaceMessageWithUpdateTheRecordlist(){
        LiveEventBus
                .get("update_recordlist")
                .post("update");
    }

    //用户点击类消息
    public static void userMessageWithSetPostUrl(String url){
        LiveEventBus
                .get("user_set_posturl")
                .post(url);
    }

    //测试消息类
    public static void  postTestMessageWithPostFullInformationMap(TestBeanWithPostFullInformationMap bean){
        LiveEventBus
                .get("testmessage_post_full_information_map")
                .post(bean);
    }

    //时间心跳类

    //默认以一定时间发送一次的时间间隔消息.时间间隔是一分钟.
    public static void postBaseTimeInterval(){
       LiveEventBus
	       .get("time_interval")
	       .post("base_time_interval");
    }



}
