/*
 * Created on 30/Oct/2003
 *
 */
package ServidorAplicacao.utils;

import Util.RandomStringGenerator;

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

