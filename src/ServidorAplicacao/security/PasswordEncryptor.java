/*
 * Created on 16/Mar/2003 by jpvl
 *
 */
package ServidorAplicacao.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author jpvl
 */
public abstract class PasswordEncryptor {
	static public String encryptPassword(String password) {
		if (password == null)
			throw new IllegalArgumentException("Password must be not null!");

		byte buffer[] = new byte[password.length()];
		MessageDigest algorithm = getMessageDigest();
		buffer = password.getBytes();
		algorithm.update(buffer);

		byte[] digest = algorithm.digest();
		BigInteger bi = new BigInteger(digest);
		return bi.toString(16);
	}

	static public boolean areEquals(
		String encryptedPassword,
		String notEncryptedPassword) {
		System.out.println("DIGEST _ENC="+ encryptedPassword);		
		String digest = encryptPassword(notEncryptedPassword);
		System.out.println("DIGEST = "+ encryptedPassword);
		return digest.equals(encryptedPassword);
	}

	private static MessageDigest getMessageDigest() {
		MessageDigest algorithm = null;
		try {
			//algorithm = MessageDigest.getInstance("SHA-1");
			algorithm = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		algorithm.reset();
		return algorithm;
	}
}
