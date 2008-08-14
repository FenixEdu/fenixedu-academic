package net.sourceforge.fenixedu.util;

import java.math.BigDecimal;

import org.apache.commons.codec.digest.DigestUtils;

public class FenixDigestUtils {

    public static String createDigest(String data) {
	return DigestUtils.shaHex(data);
    }

    public static void main(String[] args) {
	String res = FenixDigestUtils
		.createDigest("2qq asdsad1a zxfgsdf df 2s2ds2ah hdfg 18sgdyh12 12t rwdfg2sfg 5ty fdgh 13ert11yertyhsg  asdsadsa sdr sw1rt t s");
	String string = getPrettyCheckSum(res);
	System.out.println(string);

    }

    public static String getPrettyCheckSum(String digest) {
	int digestLength = digest.length();
	if ((digestLength % 2) == 1) {
	    digest = "0" + digest;
	    digestLength++;
	}

	byte[] result = new byte[digestLength / 2];

	for (int i = 0, min = 0, max = 2; max <= digestLength; min += 2, max += 2, i++) {
	    result[i] = (byte) Integer.parseInt(digest.substring(min, max), 16);
	}

	BigDecimal bigDecimal = new BigDecimal("17");
	BigDecimal bigDecimal2 = new BigDecimal("101");

	for (int i = result.length; i > 0; i--) {
	    short a = (short) (0xFF & result[i - 1]);
	    bigDecimal = bigDecimal.multiply(bigDecimal2).add(BigDecimal.valueOf(a + i));
	}

	BigDecimal result2 = bigDecimal.remainder(new BigDecimal("99997"));
	return result2.abs().toPlainString();
    }

}
