/*
 * Created on 30/Oct/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.utils;


/**
 * @author Barbosa
 * @author Pica
 */
public class GeneratePassword {

    private static IGeneratePassword instance = new GeneratePasswordBase();

    public static IGeneratePassword getInstance() {
        return instance;
    }

}
