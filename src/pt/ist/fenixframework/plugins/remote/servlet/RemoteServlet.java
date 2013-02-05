package pt.ist.fenixframework.plugins.remote.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pt.ist.fenixframework.plugins.remote.UriType;
import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;
import pt.ist.fenixframework.plugins.remote.domain.RemoteSystem;
import pt.ist.fenixframework.plugins.remote.servlet.request.BadRequest;
import pt.ist.fenixframework.plugins.remote.servlet.request.RemoteClassRequest;
import pt.ist.fenixframework.plugins.remote.servlet.request.RemoteHostRequest;
import pt.ist.fenixframework.plugins.remote.servlet.request.RemoteObjectRequest;
import pt.ist.fenixframework.plugins.remote.servlet.request.RemoteRequest;
import pt.ist.fenixframework.plugins.remote.servlet.request.UnauthorizedRequest;

public class RemoteServlet extends HttpServlet {

    private static final String DEFAULT_MAPPING_PATTERN_PREFIX = "/remote/";

    private static String mappingPatternPrefix = DEFAULT_MAPPING_PATTERN_PREFIX;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        final String initParameter = config.getInitParameter("mappingPatternPrefix");
        if (initParameter != null && !initParameter.isEmpty()) {
            mappingPatternPrefix = initParameter;
        } else {
            mappingPatternPrefix = DEFAULT_MAPPING_PATTERN_PREFIX;
        }
    }

    protected static Log log = LogFactory.getLog(RemoteServlet.class);

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        process(request, response);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        process(request, response);
    }

    protected void process(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // Wrap multipart requests with a special wrapper
        request = processMultipart(request);

        if (log.isDebugEnabled()) {
            log.debug("Processing a '" + request.getMethod() + "' for path '" + request.getRequestURI() + "'");
        }

        // Set the content type and no-caching headers if requested
        processNoCache(request, response);

        dispatch(request, response);
    }

    protected HttpServletRequest processMultipart(HttpServletRequest request) {

        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return (request);
        }

        String contentType = request.getContentType();
        if ((contentType != null) && contentType.startsWith("multipart/form-data")) {
            return (new MultipartRequestWrapper(request));
        } else {
            return (request);
        }

    }

    protected void processNoCache(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
        response.setDateHeader("Expires", 1);
    }

    protected void dispatch(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final RemoteRequest remoteRequest = createRemoteRequest(request);
        remoteRequest.sendResponse(response);
    }

    private boolean checkAccessControl(final HttpServletRequest request) {
        final String url = getRemoteHostUrl(request);
        System.out.println("Url: " + url);

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            for (final RemoteHost remoteHost : RemoteSystem.getInstance().getRemoteHostsSet()) {
                for (final Object o : remoteHost.getUrl().toArray()) {
                    if (url.equalsIgnoreCase((String) o) && remoteHost.getAllowInvocationAccess() != null
                            && remoteHost.getAllowInvocationAccess().booleanValue()
                            && username.equalsIgnoreCase(remoteHost.getUsername()) && password.equals(remoteHost.getPassword())) {
                        return true;
                    }
                }
            }
        }

        if (log.isWarnEnabled()) {
            log.warn("Denying access for: " + url + "?username=" + username + "&password="
                    + (password == null ? "null" : "xxxxxx"));
        }
        return false;
    }

    private String getRemoteHostUrl(final HttpServletRequest request) {
        final String xForwardForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardForHeader != null && !xForwardForHeader.isEmpty()) {
            final int urlSeperator = xForwardForHeader.indexOf(',');
            return urlSeperator > 0 ? xForwardForHeader.substring(0, urlSeperator) : xForwardForHeader;
        }
        return request.getRemoteHost();
    }

    protected RemoteRequest createRemoteRequest(final HttpServletRequest request) {
        if (checkAccessControl(request)) {
            final String path = processPath(request);
            if (path == null || path.isEmpty()) {
                return new RemoteHostRequest();
            } else {
                final String[] pathParts = path.split("/");
                if (pathParts.length > 1) {
                    final String type = pathParts[0];
                    try {
                        if (UriType.CLASS.type().equalsIgnoreCase(type)) {
                            try {
                                return new RemoteClassRequest(request, pathParts);
                            } catch (final ClassNotFoundException e) {
                                return new BadRequest(e.toString());
                            }
                        } else if (UriType.OBJECT.type().equalsIgnoreCase(type)) {
                            return new RemoteObjectRequest(request, pathParts);
                        }
                    } catch (final SecurityException e) {
                        return new UnauthorizedRequest();
                    } catch (final NoSuchMethodException e) {
                        return new BadRequest(e.toString());
                    }
                }
                return new RemoteHostRequest();
            }
        } else {
            return new UnauthorizedRequest();
        }
    }

    protected String processPath(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String contextPath = request.getContextPath();
        final int offset = contextPath.length() + mappingPatternPrefix.length();
        return offset >= requestURI.length() ? null : requestURI.substring(offset);
    }

    public static String getMappingPatternPrefix() {
        return mappingPatternPrefix;
    }

}
