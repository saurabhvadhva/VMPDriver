package app.vmp.driver.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoHandler {

    String SecretKey = "kq5ruimd1d3wu3p6vy3b4dg770oi87xs";
//    String IV = "";

    private static CryptoHandler instance = null;

    public static CryptoHandler getInstance() {

        if (instance == null) {
            instance = new CryptoHandler();
        }
        return instance;
    }


    public String decrypt(String encrypted) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {

        SecretKeySpec skeySpec = new
                SecretKeySpec(SecretKey.substring(0,32).getBytes(), "AES");
//        IvParameterSpec ivSpec = new
//                IvParameterSpec(IV.substring(0,16).getBytes());
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
//        SecureRandom rnd = new SecureRandom();
//        byte[] iv = new byte[ecipher.getBlockSize()];
//        rnd.nextBytes(iv);
//        IvParameterSpec ivParams = new IvParameterSpec(iv);
//        ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParams);
        ecipher.init(Cipher.DECRYPT_MODE, skeySpec,new IvParameterSpec(new byte[16]));
        byte[] raw = Base64.decode(encrypted, Base64.DEFAULT);
        byte[] originalBytes = ecipher.doFinal(raw);
        String original = new String(originalBytes, "UTF8");
        return original;
    }

}
