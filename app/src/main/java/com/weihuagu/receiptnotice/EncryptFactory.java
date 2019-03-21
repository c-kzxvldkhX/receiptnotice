package com.weihuagu.receiptnotice;
public class EncryptFactory{
        private String key;
        public EncryptFactory(String key){
                this.key=key;
        }
        public Encrypter getEncrypter(String encrypt_type){
                if(encrypt_type=="des")
                        return new DES(key);
                return null;



        }
}
