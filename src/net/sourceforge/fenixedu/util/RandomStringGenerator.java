/*
 * Created on 18/Mar/2003
 */
package net.sourceforge.fenixedu.util;

import java.util.Random;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class RandomStringGenerator extends FenixUtil {
    private final static String chars = new String(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");

    private static Random rand = new Random();

    public static String getRandomStringGenerator(int size) {
        String password = new String("");
        int nrand;

        for (int i = 0; i < size; i++) {
            nrand = Math.abs(rand.nextInt(chars.length()));
            password = password + chars.charAt(nrand);
        }

        return password;
    }
}