/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.util;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class FenixDigestUtils {

    private static final Logger logger = LoggerFactory.getLogger(FenixDigestUtils.class);

    public static String createDigest(String data) {
        return Hashing.sha1().hashString(data, Charsets.UTF_8).toString();
    }

    public static void main(String[] args) {
        String res =
                FenixDigestUtils
                        .createDigest("2qq asdsad1a zxfgsdf df 2s2ds2ah hdfg 18sgdyh12 12t rwdfg2sfg 5ty fdgh 13ert11yertyhsg  asdsadsa sdr sw1rt t s");
        String string = getPrettyCheckSum(res);
        logger.info(string);

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
