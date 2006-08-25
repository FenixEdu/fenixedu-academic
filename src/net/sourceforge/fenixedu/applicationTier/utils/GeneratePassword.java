/*
 * Created on 30/Oct/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu._development.PropertiesManager;

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
	    String stringClass = PropertiesManager.getProperty("passGenerator");
	    Class clazz = Class.forName(stringClass);
	    return (IGeneratePassword) clazz.newInstance();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }
}
