package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import pt.ist.fenixWebFramework.Config.CasConfig;
import pt.ist.fenixWebFramework.security.UserView;

public class CASFilter extends pt.ist.fenixWebFramework.servlets.filters.CASFilter {

    protected void redirectToCAS(final CasConfig casConfig, final HttpServletRequest request, final HttpServletResponse response)
	    throws IOException, ServletException {
	if (UserView.getUser() == null) {
	    String pendingRequest = request.getParameter("pendingRequest");
	    if (pendingRequest == null) {
		pendingRequest = (String) request.getAttribute("pendingRequest");
	    }
	    final String serviceString = encodeUrl(RequestUtils.generateRedirectLink(casConfig.getServiceUrl(), pendingRequest));
	    final String casLoginUrl = casConfig.getCasLoginUrl();
	    final String casLoginString = casLoginUrl + "?service=" + serviceString;
	    response.sendRedirect(casLoginString);
	} else {
	    response.sendRedirect(request.getContextPath() + "/home.do");
	}
    }

}
