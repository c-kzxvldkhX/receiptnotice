package com.weihuagu.receiptnotice;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Cipher;

public class MD5 extends Encrypter {
    public MD5(String key) {
        super(key);
    }
    public MD5(){

    }

    public Map<String, String> transferMapValue(Map<String, String> params) {
        if(params.get("type")!=null&&params.get("money")!=null&&key!=null) {
            Map<String, String> postmap = new HashMap<String, String>();
            postmap.putAll(params);
            postmap.put("sign", getSignMd5WithSecretkey(params.get("type"), params.get("money"), key));
            LogUtil.debugLogWithJava("调试，sign md5");
            return postmap;
        }else
        return params;


    }


    public String getMd5String(String str) {
        try {
            byte[] bytesOfMessage = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            return new String(thedigest,"UTF-8");
        } catch (java.io.UnsupportedEncodingException exception) {
            return null;

        } catch (java.security.NoSuchAlgorithmException exception) {
            return null;
        }

    }

    public String getSignMd5(String type, String price) {
        return getMd5String(getMd5String(type+price));
    }

    public String getSignMd5WithSecretkey(String type, String price, String secretkey) {
        return getMd5String(getMd5String(type+price)+secretkey);
    }


}
