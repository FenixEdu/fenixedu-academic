package net.sourceforge.fenixedu.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostAccessControl {
    private static final Logger logger = LoggerFactory.getLogger(HostAccessControl.class);

    private static final String HOST_CONTROL_NAME_PREFIX = "host.control.name.";

    private static HostAccessControl intance = new HostAccessControl();

    public static HostAccessControl getInstance() {
        return HostAccessControl.intance;
    }

    private Map<String, List<InetAddress>> configuration;

    public HostAccessControl() {
        super();

        setupConfiguration();
    }

    private void setupConfiguration() {
        this.configuration = new HashMap<String, List<InetAddress>>();

        Properties properties = PropertiesManager.getProperties();
        logger.info("setup configuration for host access control");
        for (Object key : properties.keySet()) {
            String keyName = (String) key;

            if (keyName.startsWith(HOST_CONTROL_NAME_PREFIX)) {
                String name = StringUtils.remove(keyName, HOST_CONTROL_NAME_PREFIX);
                String[] hostList = properties.get(key).toString().split(",");

                for (String element : hostList) {
                    String host = element.trim();

                    if (host.length() > 0) {
                        logger.info("{}={}", name, host);
                        addEntry(name, host);
                    }
                }
                // just for a cleaner message
                if (LogLevel.DEBUG) {
                    logger.debug("access for {} limited to {}", name, this.configuration.get(name));
                }
            }
        }
    }

    private void addEntry(String name, String host) {
        List<InetAddress> hostList = this.configuration.get(name);

        if (hostList == null) {
            hostList = new ArrayList<InetAddress>();

            this.configuration.put(name, hostList);
        }

        try {
            InetAddress[] addresses = InetAddress.getAllByName(host);

            for (InetAddress addresse : addresses) {
                hostList.add(addresse);
            }
        } catch (UnknownHostException e) {
            if (LogLevel.WARN) {
                logger.warn("could not find host {}, host ignored.", host);
            }
        }
    }

    public static boolean isAllowed(String name, ServletRequest request) {
        return getInstance().isAllowed(name, getRemoteAddress(request));
    }

    public static boolean isAllowed(Class type, ServletRequest request) {
        return isAllowed(type.getName(), request);
    }

    public static boolean isAllowed(Object object, ServletRequest request) {
        return isAllowed(object.getClass(), request);
    }

    private boolean isAllowed(String name, String address) {
        try {
            InetAddress remoteAddress = InetAddress.getByName(address);

            List<InetAddress> hostList = this.configuration.get(name);
            if (hostList == null) {
                if (LogLevel.WARN) {
                    logger.warn(name + " denied[" + remoteAddress + "]: allowed hosts not defined");
                }
                return false;
            }

            for (InetAddress allowedHost : hostList) {
                if (remoteAddress.equals(allowedHost)) {
                    if (LogLevel.DEBUG) {
                        logger.debug(name + " allowed[" + remoteAddress + "]: matches group " + hostList);
                    }

                    return true;
                }
            }

            if (LogLevel.WARN) {
                logger.warn(name + " denied[" + remoteAddress + "]: is not member of " + hostList);
            }
        } catch (UnknownHostException e) {
            if (LogLevel.WARN) {
                logger.warn(name + " denied[" + address + "]: could not find host");
            }
        }

        return false;
    }

    public static String getRemoteAddress(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            final String xForwardFor = httpServletRequest.getHeader("x-forwarded-for");
            if (xForwardFor != null && xForwardFor.length() > 0) {
                return xForwardFor;
            }
        }
        return request.getRemoteAddr();
    }

}
