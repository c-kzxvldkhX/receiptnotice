package com.weihuagu.receiptnotice;
import android.app.Notification;
import android.provider.Telephony.Sms;

import com.weihuagu.receiptnotice.action.IDoPost;
import com.weihuagu.receiptnotice.pushclassification.pmentay.AlipayPmentayNotificationHandle;
import com.weihuagu.receiptnotice.pushclassification.pmentay.BanksProxy;
import com.weihuagu.receiptnotice.pushclassification.pmentay.CashbarPmentayNotificationHandle;
import com.weihuagu.receiptnotice.pushclassification.pmentay.IcbcelifePmentayNotificationHandle;
import com.weihuagu.receiptnotice.pushclassification.pmentay.MipushPmentayNotificationHandle;
import com.weihuagu.receiptnotice.pushclassification.pmentay.UnionpayPmentayNotificationHandle;
import com.weihuagu.receiptnotice.pushclassification.pmentay.WechatPmentayNotificationHandle;
import com.weihuagu.receiptnotice.pushclassification.pmentay.XposedmodulePmentayNotificationHandle;

public  class NotificationHandleFactory{
    public PmentayNotificationHandle getNotificationHandle(String pkg, Notification notification, IDoPost postpush){
                //mipush
                if("com.xiaomi.xmsf".equals(pkg)){
                        return  new MipushPmentayNotificationHandle("com.xiaomi.xmsf",notification,postpush);
                }
                //支付宝
                if("com.eg.android.AlipayGphone".equals(pkg)){
                        return new AlipayPmentayNotificationHandle("com.eg.android.AlipayGphone",notification,postpush);
                }

                //应用管理GCM代收
                if("android".equals(pkg)){
                        return new XposedmodulePmentayNotificationHandle("github.tornaco.xposedmoduletest",notification,postpush);
                }
                //微信
                if("com.tencent.mm".equals(pkg)){
                        return new WechatPmentayNotificationHandle("com.tencent.mm",notification,postpush);
                }
                //收钱吧
                if("com.wosai.cashbar".equals(pkg)){
                        return new CashbarPmentayNotificationHandle("com.wosai.cashbar",notification,postpush);
                }
                //云闪付
                if("com.unionpay".equals(pkg)){
                        return new UnionpayPmentayNotificationHandle("com.unionpay",notification,postpush);
                }
                //工银商户之家
                if("com.icbc.biz.elife".equals(pkg)){
                        return new IcbcelifePmentayNotificationHandle("com.icbc.biz.elife",notification,postpush);
                }
                //接到短信
                if(getMessageAppPkg().equals(pkg)){
                        return new BanksProxy(getMessageAppPkg(),notification,postpush);
                }



                return null;

        }
        private String getMessageAppPkg(){
                return Sms.getDefaultSmsPackage(MainApplication.getAppContext());

        }

}


