package net.sourceforge.fenixedu.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.PathAccessControlFilter;

import org.apache.log4j.Logger;

public class HostAccessControl {
    private static final Logger logger = Logger.getLogger(PathAccessControlFilter.class);
    
    private static final String HOST_CONTROL_PROPERTIES = "/.hostAccessControl.properties";
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
        
        try {
            Properties properties = new Properties();
            PropertiesManager.loadProperties(properties, HOST_CONTROL_PROPERTIES );
            
            for (Object key : properties.keySet()) {
                String keyName = (String) key;
                
                if (keyName.startsWith(HOST_CONTROL_NAME_PREFIX)) {
                    String name = keyName.substring(HOST_CONTROL_NAME_PREFIX.length());
                    String[] hostList = properties.get(key).toString().split(",");
                    
                    for (int i = 0; i < hostList.length; i++) {
                        String host = hostList[i].trim();
                    
                        if (host.length() > 0) {
                            addEntry(name, host);
                        }
                    }
                    
                    // just for a cleaner message
                    logger.debug("access for '" + name + "' limited to " + this.configuration.get(name));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to read the host access control properties '" + HOST_CONTROL_PROPERTIES + "'", e);
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

            for (int i = 0; i < addresses.length; i++) {
                hostList.add(addresses[i]);
            }
        } catch (UnknownHostException e) {
            logger.warn("could not find host '" + host + "', host ignored.");
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
                logger.warn(name + " denied[" + remoteAddress + "]: allowed hosts not defined");
                return false;
            }

            for (InetAddress allowedHost : hostList) {
                if (remoteAddress.equals(allowedHost)) {
                    logger.debug(name + " allowed[" + remoteAddress + "]: matches group " + hostList);
                    
                    return true;
                }
            }
            
            logger.warn(name + " denied["+ remoteAddress + "]: is not member of " + hostList);
        } catch (UnknownHostException e) {
            logger.warn(name + " denied["+ address + "]: could not find host");
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
