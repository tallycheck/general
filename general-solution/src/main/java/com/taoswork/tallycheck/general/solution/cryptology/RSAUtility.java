package com.taoswork.tallycheck.general.solution.cryptology;

import com.taoswork.tallycheck.general.solution.quickinterface.DataHolder;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
//import javax.crypto.CipherInputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by Gao Yuan on 2016/6/21.
 */
public class RSAUtility {
    private static final String RSA_ALGORITHM = "RSA";
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final int DEFAULT_BLOCK_SIZE = 64;
    private static final String CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
//    RSA/ECB/PKCS1Padding (1024, 2048)
//    RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
//    RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)


    public static void generateKeyPair(DataHolder<RSAPublicKey> pubKey, DataHolder<RSAPrivateKey> priKey)
            throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        pubKey.put(publicKey);
        priKey.put(privateKey);
    }

    public static RSAPublicKey getPublicKey(BigInteger modulus, BigInteger exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static RSAPrivateKey getPrivateKey(BigInteger modulus, BigInteger exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static InputStream encrypt(InputStream rawData, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        InputStream cipherInputStream = new BlockCipherInputStream(rawData, cipher, DEFAULT_BLOCK_SIZE);
        return cipherInputStream;
    }

    public static byte[] encrypt(byte[] rawData, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        InputStream rawDataStream = new ByteArrayInputStream(rawData);
        InputStream cipherStream = encrypt(rawDataStream, key);
        return IOUtils.toByteArray(cipherStream);
    }

    public static String encrypt(String rawData, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        byte[] rawBytes = rawData.getBytes(DEFAULT_CHARSET);
        byte[] encryptedBytes = encrypt(rawBytes, key);
        return Base64Utils.encodeToString(encryptedBytes);
    }

    public static InputStream decrypt(InputStream encryptedData, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        RSAKey rsaKey = (RSAKey)key;
        int blockSize = rsaKey.getModulus().bitLength() / 8;

        InputStream cipherInputStream = new BlockCipherInputStream(encryptedData, cipher, blockSize);
        return cipherInputStream;
    }

    public static byte[]  decrypt(byte[] encryptedData, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        InputStream encryptedDataStream = new ByteArrayInputStream(encryptedData);
        InputStream cipherStream = decrypt(encryptedDataStream, key);
        return IOUtils.toByteArray(cipherStream);
    }

    public static String  decrypt(String encryptedData, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        byte[] encryptedDataBytes = Base64Utils.decodeFromString(encryptedData);
        byte[] decryptedBytes = decrypt(encryptedDataBytes, key);
        return new String(decryptedBytes, DEFAULT_CHARSET);
    }


}
