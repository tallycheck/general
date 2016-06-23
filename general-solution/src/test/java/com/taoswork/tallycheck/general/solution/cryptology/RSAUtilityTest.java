package com.taoswork.tallycheck.general.solution.cryptology;

import com.taoswork.tallycheck.general.solution.quickinterface.DataHolder;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.NoSuchPaddingException;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Random;

/**
 * Created by Gao Yuan on 2016/6/22.
 */
public class RSAUtilityTest {
    public String randomString(int len) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }

    @Test
    public void testEncryptAndDecrypt () throws Exception {
        DataHolder<RSAPublicKey> publicKeyDataHolder = new DataHolder<RSAPublicKey>();
        DataHolder<RSAPrivateKey> privateKeyDataHolder = new DataHolder<RSAPrivateKey>();
        RSAUtility.generateKeyPair(publicKeyDataHolder, privateKeyDataHolder);

        int[] lengths = new int[] {59, 64, 125, 128, 253, 256, 509, 512, 1021, 1024};
        for (int len : lengths){
            boolean success = false;
            String rawString = randomString(len);
            String encryptedString = "";
            String decryptedString = "";
            try {
                encryptedString = RSAUtility.encrypt(rawString, publicKeyDataHolder.data);
                decryptedString = RSAUtility.decrypt(encryptedString, privateKeyDataHolder.data);
                if(rawString.equals(decryptedString))
                    success = true;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                if(!success){
                    System.out.println("Len: " + len);
                    System.out.println("PubKey: " + publicKeyDataHolder.data);
                    System.out.println("PriKey: " + privateKeyDataHolder.data);

                    System.out.println("Raw: " + rawString);
                    System.out.println("Encrypted: " + encryptedString);
                    System.out.println("Decrypted: " + decryptedString);
                    Assert.fail();
                }
            }
        }
        for (int len : lengths){
            boolean success = false;
            String rawString = randomString(len);
            String encryptedString = "";
            String decryptedString = "";
            try {
                encryptedString = RSAUtility.encrypt(rawString, privateKeyDataHolder.data);
                decryptedString = RSAUtility.decrypt(encryptedString, publicKeyDataHolder.data);
                if(rawString.equals(decryptedString))
                    success = true;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                if(!success){
                    System.out.println("Len: " + len);
                    System.out.println("PubKey: " + publicKeyDataHolder.data);
                    System.out.println("PriKey: " + privateKeyDataHolder.data);

                    System.out.println("Raw: " + rawString);
                    System.out.println("Encrypted: " + encryptedString);
                    System.out.println("Decrypted: " + decryptedString);
                    Assert.fail();
                }
            }
        }
    }
}
