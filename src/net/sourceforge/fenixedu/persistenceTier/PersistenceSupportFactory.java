/*
 * Created on 2005/03/27
 * 
 */
package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author Luis Cruz
 */
import java.lang.reflect.Method;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.log4j.Logger;

public class PersistenceSupportFactory {

    private static final Logger logger = Logger.getLogger(PersistenceSupportFactory.class);

    private static final ISuportePersistente defaultPersistenceSupport;

    static {
        final String defaultPersistenceSupportClassName = PropertiesManager.getProperty("default.persistenceSupport");
        try {
            final Class defaultPersistenceSupportClass = Class.forName(defaultPersistenceSupportClassName);
            final Method getInstanceMethod = defaultPersistenceSupportClass.getMethod("getInstance", (Class[])null);
            defaultPersistenceSupport = (ISuportePersistente) getInstanceMethod.invoke(null, (Object[])null);
        } catch (Exception e) {
            throw new RuntimeException("Unable to determine/obtain a default persistence support", e);
        }

        logger.info("Default PersistenceSupport is set to: " + defaultPersistenceSupportClassName);
    }

    public static ISuportePersistente getDefaultPersistenceSupport() throws ExcepcaoPersistencia {
        return defaultPersistenceSupport;
    }

}