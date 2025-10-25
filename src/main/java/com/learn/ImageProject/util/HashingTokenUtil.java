package com.learn.ImageProject.util;

import java.security.MessageDigest;
import java.security.Security;
import java.util.Base64;
import java.util.HexFormat;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.ResponseEntity;

import com.learn.ImageProject.response.ImageListResponseDTO;

public class HashingTokenUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            byte[] hash = digest.digest(password.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    public static boolean getUsernameFromToenMatched(String userName, String token) {
		 byte[] obfuscatedUserNameBytes = Base64.getDecoder().decode(token);
		 String userNameFromToken = Rc4Obfuscator.deobfuscate(new String(obfuscatedUserNameBytes));
//		 System.out.println("Username from token = " + userNameFromToken);    	
		 if(userName.equals(userNameFromToken)) {
			 return true;
		 }else {
			 return false;
		 }

    }
}
