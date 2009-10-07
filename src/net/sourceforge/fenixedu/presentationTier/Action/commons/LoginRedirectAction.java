package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.PendingRequest;
import net.sourceforge.fenixedu.domain.PendingRequestParameter;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.VersionNotAvailableException;

public class LoginRedirectAction extends Action {

    private String addToUrl(String url, String param, String value) {
	if (url.contains("?")) {
	    if (url.contains("&")) {
		return url + "&" + param + "=" + value;
	    } else if (url.charAt(url.length() - 1) != '?') {
		return url + "&" + param + "=" + value;
	    } else {
		return url + param + "=" + value;
	    }
	} else {
	    return url + "?" + param + "=" + value;
	}
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	final PendingRequest pendingRequest = PendingRequest.fromExternalId(request.getParameter("pendingRequest"));
	try{
	    pendingRequest.getUrl();
	}catch (VersionNotAvailableException e) {
	    response.sendRedirect("/home.do");
	    return null;
	}
	if (pendingRequest.getBuildVersion().equals(PendingRequest.buildVersion)) {
	    String url = pendingRequest.getUrl();

	    final List<PendingRequestParameter> attributes = new ArrayList<PendingRequestParameter>();
	    for (PendingRequestParameter pendingRequestParameter : pendingRequest.getPendingRequestParameter()) {
		if (pendingRequestParameter.getAttribute()) {
		    attributes.add(pendingRequestParameter);
		} else {
		    url = addToUrl(url, pendingRequestParameter.getParameterKey(), pendingRequestParameter.getParameterValue());
		}
	    }
	    request.setAttribute("url", url);
	    request.setAttribute("body_param_list", attributes);

	    request.setAttribute("method", pendingRequest.getPost() ? "post" : "get");

	    pendingRequest.delete();
	    return mapping.findForward("show-redirect-page");
	}else{
	    
	    response.sendRedirect("/home.do");
	    return null;
	}
	
    }
}