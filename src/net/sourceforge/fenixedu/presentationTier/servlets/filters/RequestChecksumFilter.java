package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

import org.apache.commons.fileupload.FileUpload;

import pt.ist.fenixWebFramework.FenixWebFramework;

public class RequestChecksumFilter extends pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter implements Filter {


    protected boolean shoudFilterReques(final HttpServletRequest httpServletRequest) {
	final String uri = httpServletRequest.getRequestURI().substring(FenixWebFramework.getConfig().getAppContext().length() + 1);
	if (uri.indexOf("domainbrowser/") >= 0) {
	    return false;
	}
	if (uri.indexOf("images/") >= 0) {
	    return false;
	}
        if (uri.indexOf("javaScript/") >= 0) {
            return false;
        }
	if (uri.indexOf("script/") >= 0) {
	    return false;
	}
	if (uri.indexOf("ajax/") >= 0) {
	    return false;
	}
	if (uri.indexOf("redirect.do") >= 0) {
	    return false;
	}
	if (uri.indexOf("home.do") >= 0) {
	    return false;
	}
	if (uri.indexOf("/student/fillInquiries.do") >= 0) {
	    return false;
	}
	if (FileUpload.isMultipartContent(httpServletRequest)) {
	    return false;
	}
	final FilterFunctionalityContext filterFunctionalityContext = getContextAttibute(httpServletRequest);
	if (filterFunctionalityContext != null) {
	    final Container container = filterFunctionalityContext.getSelectedTopLevelContainer();
	    if (container != null && container.getAvailabilityPolicy() == null) {
		return false;
	    }
	}
	if(uri.indexOf("notAuthorized.do") >= 0){
	    return false;
	}
	
	return RequestUtils.isPrivateURI(httpServletRequest);
    }

    private FilterFunctionalityContext getContextAttibute(final HttpServletRequest httpServletRequest) {
	return (FilterFunctionalityContext) httpServletRequest.getAttribute(FunctionalityContext.CONTEXT_KEY);
    }

}