package com.peachy.helper;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class DataEncryption {

	private static final String ALGO = "AES";
	private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't',
			'K', 'e', 'y' };

	public static String encrypt(String data) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(data.getBytes());
		Encoder encryptedValue = Base64.getEncoder();

		return encryptedValue.encodeToString(encVal);
	}

	public static String decrypt(String encryptedData) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		Decoder decordedValue = Base64.getDecoder();
		byte[] decValue = c.doFinal(decordedValue.decode(encryptedData.getBytes()));
		String decryptedValue = new String(decValue);
		
		return decryptedValue;
	}

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}
	
	public static String encode(String in) {
		byte[] en = in.getBytes();
		StringBuilder out = new StringBuilder();

		for (int i= 0; i < en.length; i++) {
			byte b =  hashTable(en[i], i, true);
			out.append(String.format("%02X", b));
		}
		
		return out.toString();
	}

	public static String decode(String in) {
		StringBuilder sb = new StringBuilder();
		int c = 0;
		for (int i=0; i < in.length(); i+=2) {
			String output = in.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			int h = hashTable(decimal, c, false);
			sb.append((char)h);
			c++;
		}
		
		return sb.toString();
	}
	private static byte hashTable(int inp, int pos, boolean en) {
		int p = pos % 16;
		final int[] encode = {2,1,3,4,16,5,15,6,14,7,13,8,12,9,11,10};
		if (en) return (byte) (inp + encode[p]);
		else return (byte) (inp - encode[p]);
	}

}
