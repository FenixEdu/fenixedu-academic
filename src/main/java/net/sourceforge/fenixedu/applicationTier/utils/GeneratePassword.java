/*
 * Created on 30/Oct/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import pt.ist.bennu.core.util.ConfigurationManager;

/**
 * @author Barbosa
 * @author Pica
 */
public class GeneratePassword {

    private static IGeneratePassword instance = null;

    public static synchronized IGeneratePassword getInstance() {
        if (instance == null) {
            instance = getSpecificInstance();
        }
        return instance;
    }

    private static IGeneratePassword getSpecificInstance() {
        try {
            String stringClass = ConfigurationManager.getProperty("password.generator");
            Class clazz = Class.forName(stringClass);
            return (IGeneratePassword) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
