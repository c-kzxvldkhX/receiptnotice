package com.weihuagu.receiptnotice;
import android.annotation.SuppressLint;
import android.support.annotation.IntDef;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import  java.security.InvalidAlgorithmParameterException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
public class DESUtilWithIV{
@IntDef({Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE})
@interface DESType {}
 /**
     * Des加密/解密
     *
     * @param content  字符串内容
     * @param password 密钥
     * @param type     加密：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
     * @return 加密/解密结果
     */
  
    public static String des(String content, String password,  int type) {
        try {
            
            IvParameterSpec iv = new IvParameterSpec(password.getBytes());
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(type, keyFactory.generateSecret(desKey), iv);

            if (type == Cipher.ENCRYPT_MODE) {
                byte[] byteContent = content.getBytes("utf-8");
                return ByteUtil.parseByte2HexStr(cipher.doFinal(byteContent));
            } else {
                byte[] byteContent = ByteUtil.parseHexStr2Byte(content);
                return new String(cipher.doFinal(byteContent));
            }
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException |
                UnsupportedEncodingException | InvalidKeyException | NoSuchPaddingException |
                InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
}

}
