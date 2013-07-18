package net.sourceforge.fenixedu.applicationTier;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceMonitoring {

    private static final Logger logger = LoggerFactory.getLogger(ServiceMonitoring.class);

    public static final void logService(Class<?> service, Object... args) {
        logger.info("User {} ran service {} with arguments: {}", AccessControl.getUserView().getUsername(),
                service.getSimpleName(), args);
    }
}
