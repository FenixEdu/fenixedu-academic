package net.sourceforge.fenixedu.util;

import java.util.Random;

public class RandomPassword extends FenixUtil {
    private final static String chars = new String(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");

    /**
     * Gera uma password aleatoria com size caracteres
     */
    public static String getRandomPassword(int size) {
        String password = new String("");
        Random rand = new Random();
        int nrand;

        for (int i = 0; i < size; i++) {
            nrand = Math.abs(rand.nextInt(chars.length()));
            password = password + chars.charAt(nrand);
        }

        return password;
    }

}