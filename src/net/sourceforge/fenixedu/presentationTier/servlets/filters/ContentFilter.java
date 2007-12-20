package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.requestWrappers.RequestWrapper;

public class ContentFilter implements Filter {

    private String errorPage;

    private String errorPageLogged;

    private String publicPrefix;

    private static final String SECTION_PATH = "/publico/viewGenericContent.do?method=viewSection";

    private static final String ITEM_PATH = "/publico/viewGenericContent.do?method=viewItem";

    public static String FUNCTIONALITY_PARAMETER = "_f";

    public void init(FilterConfig config) throws ServletException {
	// nothing
    }

    public void destroy() {
	// nothing
    }

    public void doFilter(ServletRequest initialRequest, ServletResponse initialResponse, FilterChain chain) throws IOException,
	    ServletException {
	HttpServletRequest request = (HttpServletRequest) initialRequest;
	HttpServletResponse response = (HttpServletResponse) initialResponse;

	final FilterFunctionalityContext functionalityContext = (FilterFunctionalityContext) getContextAttibute((HttpServletRequest) initialRequest);

	if (functionalityContext == null || functionalityContext.getSelectedContents().isEmpty()
		|| functionalityContext.hasBeenForwarded()) {
	    chain.doFilter(request, response);
	} else {
	    contentsForward((HttpServletRequest) initialRequest, (HttpServletResponse) initialResponse, functionalityContext);
	}

    }

    private void dispatchTo(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
	    final FilterFunctionalityContext functionalityContext, String path) throws ServletException, IOException {
	final RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest, path, functionalityContext);
	requestWrapper
		.setAttribute(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME, functionalityContext.getCurrentContextPath());
	final RequestDispatcher requestDispatcher = requestWrapper.getRequestDispatcher(path);
	functionalityContext.setHasBeenForwarded();
	requestDispatcher.forward(requestWrapper, httpServletResponse);
    }

    private void contentsForward(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
	    final FilterFunctionalityContext functionalityContext) throws ServletException, IOException {

	Content content = functionalityContext.getSelectedContent();
	
	if (content instanceof Section && content.isAvailable(functionalityContext)) {
	    dispatchTo(httpServletRequest, httpServletResponse, functionalityContext, SECTION_PATH);
	
	} else if (content instanceof Item && content.isAvailable(functionalityContext)) {
	    dispatchTo(httpServletRequest, httpServletResponse, functionalityContext, ITEM_PATH);
	
	} else if (content instanceof Functionality && content.isAvailable(functionalityContext)) {
	    Functionality functionality = (Functionality) content;
	    dispatchTo(httpServletRequest, httpServletResponse, functionalityContext, functionality.getPath());
	
	} else {
	    final IUserView userView = AccessControl.getUserView();
	    showUnavailablePage(userView, httpServletRequest, httpServletResponse);
	}

    }

    /**
     * Redirects the client to the page showing that the functionality is not
     * available.
     */
    private void showUnavailablePage(final IUserView userView, final HttpServletRequest request,
	    final HttpServletResponse response) throws IOException, ServletException {
	final String errorPageToDispatch = userView == null || userView.isPublicRequester() ? errorPage : errorPageLogged;
	dispatch(request, response, errorPageToDispatch);
    }

    protected void dispatch(final HttpServletRequest request, final HttpServletResponse response, final String path)
	    throws IOException, ServletException {
	final RequestDispatcher dispatcher = request.getRequestDispatcher(path);

	if (dispatcher == null) {
	    response.sendRedirect(request.getContextPath() + path);
	} else {
	    dispatcher.forward(request, response);
	}
    }

    private FunctionalityContext getContextAttibute(final HttpServletRequest httpServletRequest) {
	return (FunctionalityContext) httpServletRequest.getAttribute(FunctionalityContext.CONTEXT_KEY);
    }

}
