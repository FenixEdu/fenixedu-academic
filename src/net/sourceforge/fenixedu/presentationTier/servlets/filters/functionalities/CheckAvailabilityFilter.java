package net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities;

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
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.requestWrappers.RequestWrapper;

/**
 * This filter restricts the access to certain functionalities based on the
 * functionalities model. First, the requested path is mapped into a
 * functionality. Second, the functionality is checked to see if the currently
 * logged person has access to the functionality. If the functionality is not
 * available to the person making the request, the person is redirected to an
 * error page.
 * 
 * <p>
 * <strong>NOTE</strong>: If there is no functionality associated with the
 * current path then the requests proceeds normally without any further
 * verification. This means that this filter is permissive by default. This
 * policy was chosen to allow an incremental introduction of functionalities
 * into the model without disrupting the behavior of existing functionalities.
 * 
 * @see net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy#isAvailable(FunctionalityContext,Person)
 * 
 * @author cfgi
 */
public class CheckAvailabilityFilter implements Filter {

    private String errorPage;

    private String errorPageLogged;

    private String publicPrefix;

    private static final String SECTION_PATH = "/publico/viewGenericContent.do?method=viewSection";
    
    private static final String ITEM_PATH = "/publico/viewGenericContent.do?method=viewItem";
    /**
         * Initializes the filter. There are two init parameters that are used
         * by this filter.
         * 
         * <ul>
         * <li><strong>error.page</strong>: the page were the user will be
         * redirected when a functionality is not available</li>
         * <li><strong>testing.prefix</strong>: the prefix that is being used
         * for testing and that will be removed when redirecting (does not
         * affect the <code>error.page</code> address)</li>
         * </ul>
         */
    public void init(FilterConfig config) throws ServletException {
	this.errorPage = config.getInitParameter("error.page");
	this.errorPageLogged = config.getInitParameter("error.page.logged");
	this.publicPrefix = config.getInitParameter("public.prefix");
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
	    final FilterChain filterChain) throws IOException, ServletException {
	final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
	doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    public void doFilter(final HttpServletRequest httpServletRequest,
	    final HttpServletResponse httpServletResponse, final FilterChain filterChain)
	    throws IOException, ServletException {

	final FilterFunctionalityContext functionalityContext = getContextAttibute(httpServletRequest);
	Content content = functionalityContext.getSelectedContent();
	try {
	    if (content == null || functionalityContext.hasBeenForwarded
		    || isActionRequest(httpServletRequest)) {
		filterChain.doFilter(httpServletRequest, httpServletResponse);
		return;
	    }
	} catch (StackOverflowError stackOverflowError) {
	    System.out.println(stackOverflowError.getMessage() + " for url: "
		    + httpServletRequest.getRequestURI() + " --- " + httpServletRequest.getRequestURL());
	}
	if (content != null && !content.isAvailable(functionalityContext)) {
	    final IUserView userView = AccessControl.getUserView();
	    showUnavailablePage(userView, httpServletRequest, httpServletResponse);
	}
	if (content instanceof Section) {
	    dispatchTo(httpServletRequest, httpServletResponse, functionalityContext,
		    SECTION_PATH);
	} else if (content instanceof Item) {
	    dispatchTo(httpServletRequest, httpServletResponse, functionalityContext,
		    ITEM_PATH);
	} else if (content instanceof Functionality) {
	    Functionality functionality = (Functionality) content;
	    dispatchTo(httpServletRequest, httpServletResponse, functionalityContext, functionality
		    .getPath());
	} else {
	    final IUserView userView = AccessControl.getUserView();
	    showUnavailablePage(userView, httpServletRequest, httpServletResponse);
	}
    }

    private void dispatchTo(final HttpServletRequest httpServletRequest,
	    final HttpServletResponse httpServletResponse,
	    final FilterFunctionalityContext functionalityContext, String path) throws ServletException,
	    IOException {
	final RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest, path,
		functionalityContext);
	requestWrapper.setAttribute(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME,
		functionalityContext.getCurrentContextPath());
	final RequestDispatcher requestDispatcher = requestWrapper.getRequestDispatcher(path);
	functionalityContext.setHasBeenForwarded();
	requestDispatcher.forward(requestWrapper, httpServletResponse);
    }

    private boolean isActionRequest(final HttpServletRequest httpServletRequest) {
	final String requestURI = httpServletRequest.getRequestURI();
	return requestURI.endsWith(".do") || requestURI.endsWith(".faces");
    }

    private FilterFunctionalityContext getContextAttibute(final HttpServletRequest httpServletRequest) {
	return (FilterFunctionalityContext) httpServletRequest
		.getAttribute(FunctionalityContext.CONTEXT_KEY);
    }

    /**
         * Redirects the client to the page showing that the functionality is
         * not available.
         */
    private void showUnavailablePage(final IUserView userView, final HttpServletRequest request,
	    final HttpServletResponse response) throws IOException, ServletException {
	final String servletPath = request.getServletPath().substring(request.getContextPath().length());

	final String errorPageToDispatch = servletPath.startsWith(publicPrefix) || userView == null
		|| userView.isPublicRequester() ? errorPage : errorPageLogged;
	dispatch(request, response, errorPageToDispatch);
    }

    protected void dispatch(final HttpServletRequest request, final HttpServletResponse response,
	    final String path) throws IOException, ServletException {
	final RequestDispatcher dispatcher = request.getRequestDispatcher(path);

	if (dispatcher == null) {
	    response.sendRedirect(request.getContextPath() + path);
	} else {
	    dispatcher.forward(request, response);
	}
    }

    public void destroy() {
    }

}
