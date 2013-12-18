package net.sourceforge.fenixedu.domain.log.requests;

import net.sourceforge.fenixedu.util.ArrayUtils;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RequestMapping extends RequestMapping_Base {

    public RequestMapping() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    private RequestMapping(String path, String... parameters) {
        this();
        setPath(path);
        setParameters(StringUtils.join(parameters, '&'));
    }

    public static RequestMapping createOrRetrieveRequestMapping(String path, String... parameters) {

        for (RequestMapping requestMapping : Bennu.getInstance().getRequestMappingsSet()) {
            if (requestMapping.getPath().equals(path)) {
                if (ArrayUtils.haveArraysSameElements(requestMapping.getParameters().split("&"), parameters)) {
                    return requestMapping;
                }
            }
        }

        return new RequestMapping(path, parameters);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.RequestLog> getLogs() {
        return getLogsSet();
    }

    @Deprecated
    public boolean hasAnyLogs() {
        return !getLogsSet().isEmpty();
    }

    @Deprecated
    public boolean hasParameters() {
        return getParameters() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPath() {
        return getPath() != null;
    }

}
