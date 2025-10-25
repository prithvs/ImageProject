package com.learn.ImageProject.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Rc4Obfuscator {

    // Use a strong, securely stored key in a real application.
    // This key is for demonstration only.
    private static final String SECRET_KEY = "a_super_secret_key"; 

    /**
     * Obfuscates a string using RC4 encryption.
     * @param plaintext The string to obfuscate.
     * @return The Base64-encoded, obfuscated string.
     * @throws Exception if encryption fails.
     */
    public static String obfuscate(String plaintext) {
    		try {
	        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "RC4");
	        Cipher cipher = Cipher.getInstance("RC4");
	        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
	        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
	        return Base64.getEncoder().encodeToString(encryptedBytes);
    		}catch(Exception e) {
    			e.printStackTrace();
    			return null;
    		}
    }

    /**
     * De-obfuscates a string using RC4 decryption.
     * @param obfuscatedText The Base64-encoded, obfuscated string.
     * @return The original plaintext string.
     * @throws Exception if decryption fails.
     */
    public static String deobfuscate(String obfuscatedText){
    		try {
	        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "RC4");
	        Cipher cipher = Cipher.getInstance("RC4");
	        cipher.init(Cipher.DECRYPT_MODE, keySpec);
	        byte[] decodedBytes = Base64.getDecoder().decode(obfuscatedText);
	        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
	        return new String(decryptedBytes, StandardCharsets.UTF_8);
    		}catch(Exception e) {
    			e.printStackTrace();
    			return null;
    		}
    }
}
