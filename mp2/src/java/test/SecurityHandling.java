package test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.*;

public class SecurityHandling {
    
	public static String encrypt(String strToEncrypt, String cipherType, String choosenKey) {
                String alg = cipherType.substring(0, Math.min(cipherType.length(), 3));
		String encryptedString = null;
		try {
			Cipher cipher = Cipher.getInstance(cipherType);
			final SecretKeySpec secretKey = new SecretKeySpec(choosenKey.getBytes(), alg);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return encryptedString;
	}

	public static String decrypt(String codeDecrypt, String cipherType, String choosenKey){
                String alg = cipherType.substring(0, Math.min(cipherType.length(), 3));
		String decryptedString = null;
		try{
			Cipher cipher = Cipher.getInstance(cipherType);
			final SecretKeySpec secretKey = new SecretKeySpec(choosenKey.getBytes(), alg);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			decryptedString = new String(cipher.doFinal(Base64.decodeBase64(codeDecrypt)));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return decryptedString;
	}	
}
