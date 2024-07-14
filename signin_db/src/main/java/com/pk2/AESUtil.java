package com.pk2;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class AESUtil{
	private static final String AES="AES";
	private static final String AES_CIPHER_ALGORITHM="AES/GCM/NoPadding";
	private static final int GCM_TAG_LENGTH=16;
	private static final int GCM_IV_LENGTH=12;
	private static final int KEY_SIZE=256;
	private static SecretKey sk;
    public static SecretKey generateSecretKey() throws Exception{
     KeyGenerator keyGenerator=KeyGenerator.getInstance(AES);
     keyGenerator.init(KEY_SIZE);
     return keyGenerator.generateKey();
    }
    public static void storeKeyToFile(SecretKey key) throws Exception{
    	byte encodedKey[]=key.getEncoded();
    	try(FileOutputStream fos=new FileOutputStream("secret.key")){
    		fos.write(encodedKey);
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    		throw new Exception("Failed to write key to file");
    	}
    }
    public static SecretKey loadkeyFromFile() throws Exception{
    	Path keyPath=Paths.get("secret.key");
    	if(!Files.exists(keyPath))
    	{
    		return null;
    	}
    	byte encodedKey[]=Files.readAllBytes(keyPath);
    	return new SecretKeySpec(encodedKey,"AES");
    }
    public static synchronized SecretKey getSecretKey() {
    	if(sk==null)
    	{
    		try {
    			sk=loadkeyFromFile();
    			if(sk==null)
    			{
    				sk=generateSecretKey();
    				storeKeyToFile(sk);
    			}
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return sk;
    }
    public static String encrypt(String accountNumber, SecretKey key) throws Exception{
    	Cipher cipher= Cipher.getInstance(AES_CIPHER_ALGORITHM);
    	byte[] iv=new byte[GCM_IV_LENGTH];
    	SecureRandom secureRandom= new SecureRandom();
    	secureRandom.nextBytes(iv);
    	GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(accountNumber.getBytes(StandardCharsets.UTF_8));
        byte[] encryptedBytesWithIv = ByteBuffer.allocate(iv.length + encryptedBytes.length).put(iv).put(encryptedBytes).array();
        return Base64.getEncoder().encodeToString(encryptedBytesWithIv);
    }
    public static String decrypt(String encryptedAccountNumber, SecretKey key) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedAccountNumber);
        ByteBuffer byteBuffer = ByteBuffer.wrap(decodedBytes);
        byte[] iv = new byte[GCM_IV_LENGTH];
        byteBuffer.get(iv);
        byte[] encryptedBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(encryptedBytes);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}