package com.weihuagu.receiptnotice;
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
                LogUtil.debugLog("没有匹配到合适的Encrypter");
                return null;



        }
}
