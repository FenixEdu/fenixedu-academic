package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.smartcardio.ATR;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class PendingRequest extends PendingRequest_Base {

    public PendingRequest(HttpServletRequest request) {
	super();
	setGenerationDate(new DateTime());
	if (request.getMethod().equalsIgnoreCase("GET")){
	    setPost(false);
	}else{
	    setPost(true);
	}
	setUrl(request.getContextPath() + request.getServletPath());
	for (Object object : request.getParameterMap().keySet()) {
	    String key = (String) object;
	    addPendingRequestParameter(new PendingRequestParameter(key, request.getParameter(key)));
	}
	
	/*final Map<String, Object> attributeMap = new HashMap<String, Object>();
	final Enumeration enumeration = request.getAttributeNames();
	while (enumeration.hasMoreElements()) {
	    final String attributeName = (String) enumeration.nextElement();
	    final String attribute = (String) request.getAttribute(attributeName);

	    if (shouldBeAdded(attribute)) {
		addPendingRequestParameter(new PendingRequestParameter(attributeName, attribute));
	    }
	}*/
	

    }
    
    private boolean shouldBeAdded(Object attribute) {

	if (attribute instanceof Collection) {
	    Iterator iterator = ((Collection) attribute).iterator();
	    while (iterator.hasNext()) {
		if (!(iterator.next() instanceof Serializable)) {
		    return false;
		}
	    }
	}
	return attribute instanceof Serializable;
    }
    
    public Map<String, Object> getRequestsParams() {
	Map<String,Object> map = new HashMap<String, Object>();
	for (PendingRequestParameter parameter : getPendingRequestParameter()){
	    map.put(parameter.getParameterKey(), parameter.getParameterValue());
	}
	return map;
    }
    
    @Service
    public void delete() {
	for (PendingRequestParameter pendingRequestParameter : getPendingRequestParameter()){
	    pendingRequestParameter.delete();
	}
	removeRootDomainObject();
	deleteDomainObject();
    }

}
