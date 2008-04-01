package net.sourceforge.fenixedu.domain.log.requests;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.ArrayUtils;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RequestMapping extends RequestMapping_Base {

    public RequestMapping() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    private RequestMapping(String path, String... parameters) {
	this();
	setPath(path);
	setParameters(StringUtils.join(parameters, '&'));
    }

    public static RequestMapping createOrRetrieveRequestMapping(String path, String... parameters) {

	for (RequestMapping requestMapping : RootDomainObject.getInstance().getRequestMappings()) {
	    if (requestMapping.getPath().equals(path)) {
		if (ArrayUtils.haveArraysSameElements(requestMapping.getParameters().split("&"), parameters)) {
		    return requestMapping;
		}
	    }
	}

	return new RequestMapping(path, parameters);
    }

}
