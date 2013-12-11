/*
 * Created on Jun 25, 2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;

/**
 * @author Luis Cruz
 * 
 */
public class CaptureFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    String filename;

    private static int[] fileWriterSynch = new int[0];

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();

        this.filename = filterConfig.getInitParameter("filename");
    }

    @Override
    public void destroy() {
        this.servletContext = null;
        this.filterConfig = null;

        this.filename = null;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // customize to match parameters
        String queryString = constructQueryString(request);
        StringBuilder id = new StringBuilder(request.getRequestURI());
        if (queryString != null) {
            id.append("?");
            id.append(queryString);
        }
        // optionally append i18n sensitivity
        String localeSensitive = this.filterConfig.getInitParameter("locale-sensitive");
        if (localeSensitive != null) {
            StringWriter ldata = new StringWriter();
            Enumeration locales = request.getLocales();
            while (locales.hasMoreElements()) {
                Locale locale = (Locale) locales.nextElement();
                ldata.write(locale.getISO3Language());
            }
            id.append(ldata.toString());
        }

        String username = getUsername(request);

        storeRequest(username, id);

        chain.doFilter(request, response);
    }

    private String getUsername(HttpServletRequest request) {
        User userView = Authenticate.getUser();
        if (userView != null) {
            return userView.getUsername();
        }

        return null;
    }

    private String constructQueryString(HttpServletRequest request) {
        StringBuilder queryString = new StringBuilder();

        String requestQueryString = request.getQueryString();
        if (requestQueryString != null) {
            queryString.append(requestQueryString);
        }

        Enumeration parameterNames = request.getParameterNames();
        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String parameterName = (String) parameterNames.nextElement();
                String[] parameterValues = request.getParameterValues(parameterName);
                for (String parameterValue : parameterValues) {
                    if (queryString.length() != 0) {
                        queryString.append("&");
                    }
                    queryString.append(parameterName);
                    queryString.append("=");
                    queryString.append(parameterValue);
                }
            }
        }

        if (queryString.length() != 0) {
            return queryString.toString();
        }
        return null;

    }

    private void storeRequest(String username, StringBuilder requestString) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(username);
        buffer.append(' ');
        buffer.append(requestString);
        buffer.append('\n');

        try {
            synchronized (fileWriterSynch) {
                FileWriter fileWriter = new FileWriter(filename, true);
                fileWriter.write(buffer.toString());
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}