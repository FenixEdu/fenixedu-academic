/*
 * Created on 15/Mar/2003
 *
 */
package net.sourceforge.fenixedu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Nuno Nunes
 *  
 */
public class EncryptionTools extends FenixUtil {

    /**
     * This Method encrypts a String
     * 
     * @param password
     * @return Encrypted Password
     */
    public static String encrypt(String password) {
        byte[] buf = new byte[password.length()];
        buf = password.getBytes();

        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        algorithm.reset();
        algorithm.update(buf);
        byte[] digest1 = algorithm.digest();
        return new String(digest1);
    }

    /**
     * This Method compares two encrypted passwords and returns the result of
     * the comparison
     * 
     * @param encryptedPassword
     * @param password2
     * @return boolean
     */
    public static boolean compareEncryptedPasswords(String encryptedPassword1, String encryptedPassword2) {
        byte[] buf = new byte[encryptedPassword1.length()];
        buf = encryptedPassword1.getBytes();

        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        algorithm.reset();
        algorithm.update(buf);

        byte[] digest1 = algorithm.digest();

        algorithm.reset();

        buf = new byte[encryptedPassword2.length()];
        buf = encryptedPassword2.getBytes();
        algorithm.update(buf);
        byte[] digest2 = algorithm.digest();

        if (digest1.length != digest2.length)
            return false;

        for (int i = 0; i < digest1.length; i++) {
            if (digest1[i] != digest2[i])
                return false;
        }
        return true;
    }

    /**
     * Compares a encrypted password with a non encrypted one
     * 
     * @param encryptedPassword
     * @param nonEncryptedPassword
     * @return
     */
    public static boolean compareEncryptedPasswordAndNonEncrypted(String encryptedPassword,
            String nonEncryptedPassword) {
        if (encryptedPassword.equals(encrypt(nonEncryptedPassword)))
            return true;
        return false;

    }

}