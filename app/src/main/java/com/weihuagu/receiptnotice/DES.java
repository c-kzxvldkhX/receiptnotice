package com.weihuagu.receiptnotice;
import java.util.Map;    
import java.util.HashMap;
import java.util.Iterator;
import javax.crypto.Cipher;
import com.gcssloop.encrypt.symmetric.DESUtil;
public class DES extends Encrypter{
 public DES(String key){
super(key);
}
 public Map<String,String> transferMapValue(Map<String, String> params){
 Map<String,String> postmap=new HashMap<String,String>();
Iterator entries = params.entrySet().iterator();
while (entries.hasNext()) {
 
    Map.Entry entry = (Map.Entry) entries.next();
 
    String paramkey = (String)entry.getKey();
 
    String paramvalue = (String)entry.getValue();
 
    String aesStr = DESUtil.des(paramvalue, key, Cipher.ENCRYPT_MODE);
    postmap.put(paramkey,aesStr);
 
}
postmap.put("encrypt","1");
return postmap;




}












}
