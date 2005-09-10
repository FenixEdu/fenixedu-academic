package net.sourceforge.fenixedu.stm;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;


public class ServiceInfo {
    final String username;
    final String serviceName;
    final Object[] arguments;

    ServiceInfo(String username, String serviceName, Object[] arguments) {
	this.username = username;
	this.serviceName = serviceName;
	this.arguments = arguments;
    }

    public boolean shouldLog() {
	return ((username != null) && USERS_TO_MONITOR.contains(username.toUpperCase()))
	    || SERVICES_TO_MONITOR.contains(serviceName.toUpperCase());
    }

    public String getArgumentsAsString() {
        StringBuilder argumentsInString = new StringBuilder();
        
        for (Object argument : arguments) {
            if (argument != null) {
                try {
                    argumentsInString.append(argument.toString());
                } catch (NullPointerException e) {
                    argumentsInString.append(argument.getClass().getName());
                }
                argumentsInString.append("; ");
            }
        }
        
        return argumentsInString.toString();
    }


    // STATICs start here

    private static final ThreadLocal<ServiceInfo> CURRENT_SERVICE = new ThreadLocal<ServiceInfo>();
    private static Set<String> SERVICES_TO_MONITOR;
    private static Set<String> USERS_TO_MONITOR;

    static {
	initServicesAndUsers();
    }

    public static void setCurrentServiceInfo(String username, String serviceName, Object[] args) {
	CURRENT_SERVICE.set(new ServiceInfo(username, serviceName, args));
    }

    public static ServiceInfo getCurrentServiceInfo() {
	return CURRENT_SERVICE.get();
    }

    private static void initServicesAndUsers() {
        SERVICES_TO_MONITOR = readSubjectsToMonitor("servicesToMonitor");
        USERS_TO_MONITOR = readSubjectsToMonitor("usersToMonitor");
    }

    private static Set<String> readSubjectsToMonitor(String fileName) {
        ResourceBundle rb = ResourceBundle.getBundle(fileName);
        
        Set<String> valuesSet = new HashSet<String>();
        for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements(); ) {
            valuesSet.add(keys.nextElement().toUpperCase());
        }
        
        return valuesSet;
    }
}
