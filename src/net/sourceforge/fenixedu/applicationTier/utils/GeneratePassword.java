/*
 * Created on 30/Oct/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu.util.RandomStringGenerator;

/**
 * @author Barbosa
 * @author Pica
 */
public class GeneratePassword {

    public static String generatePassword() {
        String password = RandomStringGenerator.getRandomStringGenerator(8);
        return password;
    }
}

