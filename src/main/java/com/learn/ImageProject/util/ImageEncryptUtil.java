package com.learn.ImageProject.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.SecureRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.springframework.web.multipart.MultipartFile;


public class ImageEncryptUtil {
	
	private static final Logger logger = LogManager.getLogger(ImageEncryptUtil.class);

	private static final byte[] AES_KEY = "1234567890123456".getBytes();
	public static void encryptAndSaveFile(MultipartFile file, Path targetPath) throws IOException {

	 	logger.debug("Entered encryptAndSaveFile");		 

		
		// Generate random IV
	    byte[] iv = new byte[16];
	    SecureRandom random = new SecureRandom();
	    random.nextBytes(iv);
	    
	 	logger.debug("secure random creagted for encryption");		 
	    	
	    PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
	    CipherParameters params = new ParametersWithIV(new KeyParameter(AES_KEY), iv);
	    cipher.init(true, params);

	    logger.debug("Cipher initiated for encryption");		 
	
	    try (FileOutputStream fos = new FileOutputStream(targetPath.toFile())) {
	        // Save IV at beginning of file (so we can decrypt later)
	        fos.write(iv);
	
	        try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
	            file.getInputStream().transferTo(cos);
	        }
	    }
	    
	 	logger.info("Encryption completed and writtem to file pasth: " + targetPath.toString());		 
	    
	}
	 
	
	public static byte[] decryptFile(byte[] fileBytes) throws IOException {
		
	    logger.debug("Entered decryptFile");		 
		
	    // First 16 bytes = IV
	    byte[] iv = new byte[16];
	    System.arraycopy(fileBytes, 0, iv, 0, 16);
	
	    // Remaining bytes = ciphertext
	    byte[] cipherData = new byte[fileBytes.length - 16];
	    System.arraycopy(fileBytes, 16, cipherData, 0, cipherData.length);
	
	    PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
	    CipherParameters params = new ParametersWithIV(new KeyParameter(AES_KEY), iv);
	    cipher.init(false, params);

	    logger.debug("Cipher initiated for decryption of encrypted file content");		 

	    byte[] output = new byte[cipher.getOutputSize(cipherData.length)];
	    int len = cipher.processBytes(cipherData, 0, cipherData.length, output, 0);
	    try {
	        len += cipher.doFinal(output, len);
	    } catch (Exception e) {
	        throw new IOException("Error during decryption", e);
	    }
	
	    // Trim to actual size
	    byte[] result = new byte[len];
	    System.arraycopy(output, 0, result, 0, len);

	    logger.info("Decryption completed and returned");		 
	    
	    return result;
	}
	
}
