/*
 * Created on Jun 25, 2004
 *  
 */
package ServidorApresentacao.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author Luis Cruz
 *  
 */
public class ForwardFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    String forwardURI;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();

        try {
            forwardURI = filterConfig.getInitParameter("forwartURI");
        } catch (Exception e) {
            System.out.println("Could not get init paramter 'forwartURI'.");
        }
    }

    public void destroy() {
        this.servletContext = null;
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        //String uri = request.getRequestURI();

        String uri = request.getRequestURI();
        String context = request.getContextPath();
        String[] tokens = uri.split("/");
        String executionCourseCode = tokens[tokens.length - 1];

        Object args[] = { executionCourseCode };
        try {
            Integer executionCourseOID = (Integer) ServiceUtils.executeService(null,
                    "ReadExecutionCourseOIDByCodeInLatestPeriod", args);
            if (executionCourseOID != null) {
                StringBuffer executionCourseSiteURI = new StringBuffer();
                if (context.length() > 1) {
                    executionCourseSiteURI.append(context);
                }
                executionCourseSiteURI.append(forwardURI);
                executionCourseSiteURI.append(executionCourseOID);
                response.sendRedirect(executionCourseSiteURI.toString());
            } else {
                response.sendRedirect(context);
            }
        } catch (FenixServiceException e) {
            throw new ServletException(e);
        }

    }

}