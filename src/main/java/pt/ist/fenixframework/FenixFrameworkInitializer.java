package pt.ist.fenixframework;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.slf4j.Logger;

import pt.ist.fenixWebFramework.FenixWebFramework;

public class FenixFrameworkInitializer {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(FenixFrameworkInitializer.class);

    static {
        LOG.info("Initializing fenix.");
        FenixWebFramework.bootStrap(PropertiesManager.getFenixFrameworkConfig());
    }

}
