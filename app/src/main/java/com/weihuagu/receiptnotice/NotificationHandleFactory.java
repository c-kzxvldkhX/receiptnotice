package com.weihuagu.receiptnotice;
import android.app.Notification;
public  class NotificationHandleFactory{
    public NotificationHandle getNotificationHandle(String pkg,Notification notification,IDoPost postpush){
                  //mipush
                if("com.xiaomi.xmsf".equals(pkg)){
                        return  new MipushNotificationHandle("com.xiaomi.xmsf",notification,postpush);
                }
                //支付宝
                if("com.eg.android.AlipayGphone".equals(pkg)){
                        return new AlipayNotificationHandle("com.eg.android.AlipayGphone",notification,postpush);
                }

                  //应用管理GCM代收
                if("android".equals(pkg)){
                        return new XposedmoduleNotificationHandle("github.tornaco.xposedmoduletest",notification,postpush);
                }
                //微信
                if("com.tencent.mm".equals(pkg)){
                        return new WechatNotificationHandle("com.tencent.mm",notification,postpush);
                }
                //收钱吧
                if("com.wosai.cashbar".equals(pkg)){
                        return new CashbarNotificationHandle("com.wosai.cashbar",notification,postpush);
                }
                return null;

        }

}


