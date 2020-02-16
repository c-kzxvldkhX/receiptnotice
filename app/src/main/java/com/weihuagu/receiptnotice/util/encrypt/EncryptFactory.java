package com.weihuagu.receiptnotice.util.encrypt;

import com.weihuagu.receiptnotice.util.LogUtil;

public class EncryptFactory{
        private String key;
        public EncryptFactory(String key){
                this.key=key;
        }
        public Encrypter getEncrypter(String encrypt_type){
                if(encrypt_type.equals("des"))
                        return new DES(key);
                if(encrypt_type.equals("aes"))
                        return new AES(key);
                if(encrypt_type.equals("md5"))
                        return new MD5(key);
                LogUtil.debugLog("没有匹配到合适的Encrypter");
                return null;



        }
}
