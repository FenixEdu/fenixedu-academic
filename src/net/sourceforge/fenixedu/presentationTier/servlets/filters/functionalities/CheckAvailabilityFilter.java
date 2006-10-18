package net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.Module;

import org.apache.log4j.Logger;

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
 * policy was choosen to allow an incremental introduction of functionalities
 * into the model without disrupting the behaviour of existing functionalities.
 * 
 * @see net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy#isAvailable(FunctionalityContext,Person)
 * 
 * @author cfgi
 */
public class CheckAvailabilityFilter implements Filter {

    private static Pattern ACTION_PATTERN  = Pattern.compile("[^.]*\\.(jsp|do|faces).*");
    private static Pattern SERVLET_PATTERN = Pattern.compile("[^.?]*(\\?.*)?");
    
    private static final Logger logger = Logger.getLogger(CheckAvailabilityFilter.class);
    
    private String errorPage;
    private String testingPrefix;

    public String getErrorPage() {
        return this.errorPage;
    }

    public String getTestingPrefix() {
        return this.testingPrefix;
    }

    /**
     * Initializes the filter. There are two init parameters that are used by
     * this filter.
     * 
     * <ul>
     * <li><strong>error.page</strong>: the page were the user will be
     * redirected when a functionality is not available</li>
     * <li><strong>testing.prefix</strong>: the prefix that is being used for
     * testing and that will be removed when redirecting (does not affect the
     * <code>error.page</code> address)</li>
     * </ul>
     */
    public void init(FilterConfig config) throws ServletException {
        this.errorPage = config.getInitParameter("error.page");
        this.testingPrefix = config.getInitParameter("testing.prefix");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        IUserView userView = AccessControl.getUserView();
        Functionality functionality = getFunctionality(servletRequest);
        FunctionalityContext context = getContext(servletRequest, userView, functionality);

        if (functionality == null || functionality.isAvailable(context)) {
            if (functionality != null) {
                logger.debug(String.format("%s[%d]%s", functionality.getName().getContent(), functionality.getIdInternal(), functionality.getMatchPath()));
                if (servletRequest.getQueryString() != null) {
                    logger.debug("    " + servletRequest.getQueryString());
                }
                
                setupRequest(servletRequest, context);
            }
            else {
                if (isExecutableResource(servletRequest.getServletPath())) {
                    logger.debug("not mappped: " + servletRequest.getServletPath() + (servletRequest.getQueryString() != null ? "?" + servletRequest.getQueryString() : ""));
                }
            }

            if (hasTestingPrefix(servletRequest)) {
                removeTestingPrefix(servletRequest, servletResponse);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            showUnavailablePage(servletRequest, servletResponse);
        }
    }

    /**
     * Find the functionality that matches the servlet path requested.
     * 
     * @return the functionality that matches the servlet path or
     *         <code>null</code> if we don't have a match
     */
    private Functionality getFunctionality(HttpServletRequest servletRequest) {
        String requestedPath = servletRequest.getServletPath();

        if (! isExecutableResource(requestedPath)) {
            return null;
        }
        
        if (hasTestingPrefix(servletRequest)) {
            requestedPath = requestedPath.substring(getTestingPrefix().length());
        }
        
        // HACK: dotIstPortal.do is a redirection and does not map directly to a functionality
        if (requestedPath.matches("/dotIstPortal.do")) {
            requestedPath = servletRequest.getParameter("prefix") + servletRequest.getParameter("page");
        }

        Collection<Functionality> nonRelativeFunctionalities = new ArrayList<Functionality>();
        for (Functionality functionality : RootDomainObject.getInstance().getFunctionalities()) {
            if (! functionality.isRelative()) {
                nonRelativeFunctionalities.add(functionality);
            }
        }
        
        Functionality matching = getMatchingFunctionality(requestedPath, nonRelativeFunctionalities);
        if (matching != null) {
            return matching;
        }
        
        matching = getMatchingFunctionality(requestedPath, Functionality.getTopLevelFunctionalities());
        if (matching != null) {
            return matching;
        }
        
        matching = getMatchingSubFunctionality(requestedPath, Module.getTopLevelModules());
        if (matching != null) {
            return matching;
        }

        return null;
    }

    private boolean matchesFunctionality(String path, Functionality functionality) {
        if (! functionality.isPrincipal()) {
            return false;
        }
        
        String matchPath = functionality.getMatchPath();
        
        if (matchPath != null && matchPath.equals(path)) {
            return true;
        }
        
        return false;
    }
    
    private Functionality getMatchingFunctionality(String path, Collection<Functionality> functionalities) {
        for (Functionality functionality : functionalities) {
            if (matchesFunctionality(path, functionality)) {
                return functionality;
            }
        }
        
        return null;
    }
    
    private Functionality getMatchingSubFunctionality(String path, Collection<Module> modules) {
        for (Module module : modules) {
            String prefix = module.getPublicPrefix();
            
            if (! path.startsWith(prefix)) {
                continue;
            }
            
            Functionality matching = getMatchingFunctionality(path, module.getFunctionalities());
            if (matching != null) {
                return matching;
            }
            
            matching = getMatchingSubFunctionality(path, module.getModules());
            if (matching != null) {
                return matching;
            }
        }
        
        return null;
    }
    
    /**
     * Verifies if the requested path is an executable resource and not a simple resource like 
     * and image or a stylesheet.
     * 
     * @param requestedPath the requested path
     * 
     * @return <code>true</code> if the path is a executable resource like and action
     */
    private boolean isExecutableResource(String requestedPath) {
        return ACTION_PATTERN.matcher(requestedPath).matches() || SERVLET_PATTERN.matcher(requestedPath).matches();
    }

    /**
     * Creates a functionality context from the given arguments. In the created
     * context the selected functionality is the given functionality, the
     * selected module is the module that contains the functionality. The user
     * view and user for the context are also obtained from the given user view.
     * 
     * @param request
     *            the current requests
     * @param functionality
     *            the functionality selected
     * 
     * @return a new context from the given requests and functionality
     */
    private FunctionalityContext getContext(HttpServletRequest request, IUserView userView,
            Functionality functionality) {
        return new FilterFunctionalityContext(request, userView, functionality);
    }

    private void setupRequest(HttpServletRequest servletRequest, FunctionalityContext context) {
        servletRequest.setAttribute(FunctionalityContext.CONTEXT_KEY, context);
    }

    public boolean hasTestingPrefix(HttpServletRequest request) {
        String path = request.getServletPath();

        return getTestingPrefix() != null && path.startsWith(getTestingPrefix());
    }

    /**
     * Removes the prefix used for testing and redirects the client to the new
     * address. All request parameters are appended to the URL so this technique
     * does not work very well for POSTs with long or binary parameter values.
     */
    private void removeTestingPrefix(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        String path = request.getServletPath().substring(getTestingPrefix().length());
        StringBuilder builder = new StringBuilder(path);

        Map parameters = request.getParameterMap();
        if (!parameters.isEmpty()) {
            builder.append("?");
        }

        Iterator iterator = parameters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Entry) iterator.next();
            
            String[] values = (String[]) entry.getValue();
            for (int i = 0; i < values.length; i++) {
                builder.append(entry.getKey() + "=" + values[i]);
                
                if (i < values.length - 1) { // is not last
                    builder.append("&");
                }
            }

            if (iterator.hasNext()) {
                builder.append("&");
            }
        }

        dispatch(request, response, builder.toString());
    }

    /**
     * Redirects the client to the page showing that the functionality is not
     * available.
     */
    private void showUnavailablePage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dispatch(request, response, getErrorPage());
    }

    protected void dispatch(HttpServletRequest request, HttpServletResponse response, String path) throws IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);

        if (dispatcher == null) {
            response.sendRedirect(request.getContextPath() + path);
        }
        else {
            dispatcher.forward(request, response);
        }
    }

    public void destroy() {
        // do nothing
    }

}
