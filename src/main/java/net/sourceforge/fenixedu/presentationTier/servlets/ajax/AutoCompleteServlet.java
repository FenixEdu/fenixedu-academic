package net.sourceforge.fenixedu.presentationTier.servlets.ajax;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import pt.ist.fenixWebFramework.security.UserView;

public class AutoCompleteServlet extends pt.ist.fenixWebFramework.servlets.ajax.AutoCompleteServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected Collection getSearchResult(Map<String, String> argsMap, String value, int maxCount) {
        String serviceName = argsMap.get("serviceName");
        Class type = getConcreteType(argsMap.get("class"));
        Map<String, String> serviceArgs = getServiceArgsMap(argsMap);

        return executeService(getUserView(), serviceName, type, value, maxCount, serviceArgs);
    }

    private Collection executeService(IUserView userView, String serviceName, Class type, String value, int maxCount,
            Map<String, String> arguments) {
        try {
            return (Collection) ServiceUtils.executeService(serviceName, new Object[] { type, value, maxCount, arguments });
        } catch (Exception e) {
            throw new RuntimeException("Error executing service", e);
        }
    }

    private Class getConcreteType(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("could not find type " + className, e);
        }
    }

    private IUserView getUserView() {
        return UserView.getUser();
    }

    private Map<String, String> getServiceArgsMap(Map<String, String> argsMap) {
        Map<String, String> serviceArgsMap = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : argsMap.entrySet()) {
            if ("serviceName".equals(entry.getKey())) {
                continue;
            }

            if ("class".equals(entry.getKey())) {
                continue;
            }

            serviceArgsMap.put(entry.getKey(), entry.getValue());
        }

        return serviceArgsMap;
    }
}
