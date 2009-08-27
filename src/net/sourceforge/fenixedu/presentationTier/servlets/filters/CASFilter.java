package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.PendingRequest;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import pt.ist.fenixWebFramework.Config.CasConfig;
import pt.ist.fenixWebFramework.services.Service;

public class CASFilter extends pt.ist.fenixWebFramework.servlets.filters.CASFilter {
    @Service
    private PendingRequest generatePendingRequest(HttpServletRequest request) {
	return new PendingRequest(request);
    }

    protected void redirectToCAS(final CasConfig casConfig, final HttpServletRequest request, final HttpServletResponse response)
	    throws IOException, ServletException {

	final String serviceString = encodeUrl(RequestUtils.generateRedirectLink(casConfig.getServiceUrl(),
		generatePendingRequest(request)));
	final String casLoginUrl = casConfig.getCasLoginUrl();
	final String casLoginString = casLoginUrl + "?service=" + serviceString;
	response.sendRedirect(casLoginString);
    }

}
