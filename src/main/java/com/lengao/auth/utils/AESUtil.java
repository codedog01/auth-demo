package com.lengao.auth.utils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author LengAo
 * @date 2021/11/3 11:00
 */
public class AESUtil {
    private static final String secretKey = "codedog1";
    private static final String initKey = "www.codedog1.com";
    private static final String charsetName = "UTF-8";
    private static final String transformation = "AES/CBC/PKCS5Padding";
    private static final byte[] key;
    private static final byte[] init;
    private static final IvParameterSpec IV;
    private static final SecretKeySpec SECRET;

    private static ThreadLocal<Cipher> encryptCipher = new ThreadLocal<Cipher>() {
        @Override
        protected Cipher initialValue() {
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance(transformation);
                cipher.init(Cipher.ENCRYPT_MODE, SECRET, IV);
            } catch (Exception e) {
                throw new RuntimeException("cipher initial error!", e);
            }
            return cipher;
        }
    };
    private static ThreadLocal<Cipher> decryptCipher = new ThreadLocal<Cipher>() {
        @Override
        protected Cipher initialValue() {
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance(transformation);
                cipher.init(Cipher.DECRYPT_MODE, SECRET, IV);
            } catch (Exception e) {
                throw new RuntimeException("cipher initial error!", e);
            }
            return cipher;
        }
    };
    static {
        try {
            MessageDigest keym = MessageDigest.getInstance("MD5");
            MessageDigest initm = MessageDigest.getInstance("MD5");
            key = keym.digest(secretKey.getBytes(charsetName));
            init = initm.digest(initKey.getBytes(charsetName));
            IV = new IvParameterSpec(init);
            SECRET = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            throw new RuntimeException("initial error!", e);
        }
    }

    private static boolean checkNull(String value) {
        if (value == null || value.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String encrypt(String rawValue) {
        if (checkNull(rawValue)) {
            return null;
        }
        try {
            byte[] encrypted = encryptCipher.get().doFinal(rawValue.getBytes(charsetName));
            String result = Base64.getEncoder().encodeToString(encrypted);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("encrypt error!", e);
        }
    }

    public static String decrypt(String encryptValue) {
        if (checkNull(encryptValue)) {
            return null;
        }
        try {
            byte[] decode = Base64.getDecoder().decode(encryptValue);
            Cipher cipher = decryptCipher.get();
            byte[] original = cipher.doFinal(decode);
            return new String(original, charsetName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("decrypt error!", e);
        }
    }

    public static String encryptUrlSafe(String rawValue) {
        if (checkNull(rawValue)) {
            return null;
        }
        try {
            byte[] encrypted = encryptCipher.get().doFinal(rawValue.getBytes(charsetName));
            String result = Base64.getUrlEncoder().encodeToString(encrypted);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("encrypt error!", e);
        }
    }

    public static String decryptUrlSafe(String encryptValue) {
        if (checkNull(encryptValue)) {
            return null;
        }
        try {
            byte[] original = decryptCipher.get().doFinal(Base64.getUrlDecoder().decode(encryptValue));
            return new String(original, charsetName);
        } catch (Exception e) {
            throw new RuntimeException("decrypt error!", e);
        }
    }

    public static void main(String[] args) {
//		String rawValue = "+-=?@&中国/x";
        String encrypt = encrypt("guohf");
//		String encryptUrl = encryptUrlSafe(rawValue);
        System.out.println(encrypt);
        System.out.println(decrypt("Jg5QXukqaIkRDvjotrTkRQ=="));
//		System.out.println(encryptUrl);
//		System.out.println(decryptUrlSafe(encryptUrl));
    }
}