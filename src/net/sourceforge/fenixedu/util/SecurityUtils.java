package net.sourceforge.fenixedu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    public static byte[] getMD5Bytes(byte[] data) {
	try {
	    MessageDigest md5Digest = MessageDigest.getInstance("MD5");

	    return md5Digest.digest(data);
	} catch (NoSuchAlgorithmException nsae) {
	    throw new RuntimeException();
	}
    }

    public static String getMD5String(byte[] data) {
	return convertToHex(getMD5Bytes(data));
    }

    private static String convertToHex(byte[] data) {
	StringBuilder result = new StringBuilder();

	for (int i = 0; i < data.length; i++) {
	    int low = (int) (data[i] & 0x0F);
	    int high = (int) (data[i] & 0xF0);

	    result.append(Integer.toHexString(high).substring(0, 1));
	    result.append(Integer.toHexString(low));
	}

	return result.toString();
    }

}
