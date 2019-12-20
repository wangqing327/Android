package com.example.mypassword;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    //加密
    public static String encrypt(String seed, String cleartext) {
            /*
            加密程序
             */
        try {
            byte[] rawKey = getRawKey(seed.getBytes());
            byte[] result = encrypt(rawKey, cleartext.getBytes());
            return toHex(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //解密
    public static String decrypt(String seed, String encrypted) {
        try {
            byte[] rawKey = getRawKey(seed.getBytes());
            byte[] enc = toByte(encrypted);
            byte[] result = decrypt(rawKey, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getRawKey(byte[] seed) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = null;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] src) throws Exception {
        SecretKeySpec skeyspec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
        byte[] encrypted = cipher.doFinal(src);
        //Base64加密
        encrypted = Base64.encode(encrypted, 2);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        //Base64解密
        encrypted = Base64.decode(encrypted, 2);
        SecretKey skeyspec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeyspec);
        byte[] decrypted = cipher.doFinal(encrypted);

        return decrypted;
    }

    private static String toHex(String text) {
        return toHex(text.getBytes());
    }

    private static String toHex(byte[] bytes) {
        if (bytes == null)
            return "";
        StringBuffer buffer = new StringBuffer(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            //buffer.append(bytes[i]);
            appendHex(buffer, bytes[i]);
        }
        return buffer.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;

    }

}
