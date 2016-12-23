package lee.study.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class CodecUtil {
	
	public static byte[] SHA1(String key){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			return digest.digest(key.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String base64(byte[] bts){
		return new String(Base64.encodeBase64(bts));
	}
	
	public static void mask(byte[] key,byte[] data){
		for (int i = 0; i < data.length; i++) {
			//int mod = i%4;
			int mod = i&3;
			data[i] = (byte) (data[i]^key[mod]);
		}
	}
}
