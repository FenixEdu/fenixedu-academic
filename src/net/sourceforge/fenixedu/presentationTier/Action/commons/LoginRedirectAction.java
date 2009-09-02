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

public class LoginRedirectAction extends Action {

    private String addToUrl(String url, String param, String value){
	if (url.contains("?")){
	    if (url.contains("&")){
		return url + "&" + param + "=" + value;
	    }else if (url.charAt(url.length() - 1) != '?'){
		return url + "&" + param + "=" + value;
	    }else{
		return url + param + "=" + value;
	    }
	}else{
	    return url + "?" + param + "=" + value;
	}
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	PendingRequest pendingRequest = PendingRequest.fromExternalId(request.getParameter("pendingRequest"));

	
	String url = pendingRequest.getUrl();
	if (pendingRequest.getPost()) {
	    List<PendingRequestParameter> list = new ArrayList<PendingRequestParameter>();
	    for (PendingRequestParameter pendingRequestParameter : pendingRequest.getPendingRequestParameter()){
		if (pendingRequestParameter.getAttribute()){
		    list.add(pendingRequestParameter);
		}else{
		    url = addToUrl(url, pendingRequestParameter.getParameterKey(), pendingRequestParameter.getParameterValue());
		}
	    }
	    request.setAttribute("method", "post");
	    request.setAttribute("url", url);
	    request.setAttribute("body_param_list", list);
	    
	} else {
	    request.setAttribute("method", "get");
	    request.setAttribute("url", url);
	    request.setAttribute("body_param_list", pendingRequest.getPendingRequestParameter());
	}

	
	pendingRequest.delete();
	return mapping.findForward("show-redirect-page");

    }
}