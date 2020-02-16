package com.weihuagu.receiptnotice.util.encrypt;
import com.weihuagu.receiptnotice.util.encrypt.Encrypter;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class AES extends Encrypter {
        public AES(String key){
                super(key);
        }
        public Map<String,String> transferMapValue(Map<String, String> params){
                Map<String,String> postmap=new HashMap<String,String>();
                Iterator entries = params.entrySet().iterator();
                while (entries.hasNext()) {

                        Map.Entry entry = (Map.Entry) entries.next();

                        String paramkey = (String)entry.getKey();

                        String paramvalue = (String)entry.getValue();

                        //String aesStr = AESUtil.aes(paramvalue, key, Cipher.ENCRYPT_MODE);
                        String aesStr=null;
                        postmap.put(paramkey,aesStr);

                }
                postmap.put("encrypt","1");
                return postmap;




        }












}
