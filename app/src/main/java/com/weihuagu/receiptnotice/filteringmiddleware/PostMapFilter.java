package com.weihuagu.receiptnotice.filteringmiddleware;

import com.weihuagu.receiptnotice.util.DeviceInfoUtil;
import com.weihuagu.receiptnotice.util.ExternalInfoUtil;
import com.weihuagu.receiptnotice.util.LogUtil;
import com.weihuagu.receiptnotice.util.PreferenceUtil;
import com.weihuagu.receiptnotice.util.encrypt.EncryptFactory;
import com.weihuagu.receiptnotice.util.encrypt.Encrypter;
import com.weihuagu.receiptnotice.util.encrypt.MD5;

import java.util.HashMap;
import java.util.Map;

public class PostMapFilter {
    private Map<String, String> unmodifiedmap;
    private PreferenceUtil preference;
    private String posturl;

    public PostMapFilter(PreferenceUtil preference, Map<String, String> unmodifiedmap, String posturl) {
        this.preference = preference;
        this.unmodifiedmap = unmodifiedmap;
        this.posturl = posturl;
    }

    public String getDeviceid() {
        String deviceid = preference.getDeviceid();
        if (deviceid.equals(""))
            deviceid = DeviceInfoUtil.getUniquePsuedoID();
        else if (preference.isAppendDeviceiduuid())
            deviceid = deviceid + '-' + DeviceInfoUtil.getUniquePsuedoID();
        else
            deviceid = deviceid;
        return deviceid;
    }


    public Map getPostMap() {
        Map<String, String> postmap = new HashMap<String, String>();
        postmap.putAll(getLogMap());
        postmap.put("sign",new MD5().getSignMd5(postmap.get("type"),postmap.get("money")));
        if (preference.isEncrypt()) {
            String encrypt_type = preference.getEncryptMethod();
            if (encrypt_type != null) {
                String key = preference.getPasswd();
                EncryptFactory encryptfactory = new EncryptFactory(key);
                LogUtil.debugLog("加密方法" + encrypt_type);
                LogUtil.debugLog("加密秘钥" + key);
                Encrypter encrypter = encryptfactory.getEncrypter(encrypt_type);
                if (encrypter != null && key != null) {

                    postmap = encrypter.transferMapValue(postmap);
                    postmap.put("url", this.posturl);
                    if (preference.isSkipEncryptDeviceid())
                        postmap.put("deviceid", getDeviceid());
                }

            }
        } else
            postmap.put("encrypt", "0");
        return postmap;
    }

    public Map getLogMap() {
        Map<String, String> recordmap = new HashMap<String, String>();
        recordmap.putAll(this.unmodifiedmap);
        recordmap.put("url", this.posturl);
        recordmap.put("deviceid", getDeviceid());
        if (preference.getCustomOption().equals("") == false) {
            Map custompostoption = ExternalInfoUtil.getCustomPostOption(preference.getCustomOption());
            if (custompostoption != null)
                recordmap.putAll(custompostoption);
        }
        return recordmap;

    }


}
