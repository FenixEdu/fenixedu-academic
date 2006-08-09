/*
 * Created on 26/Nov/2004
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

/**
 * @author Pedro Santos & Rita Carvalho
 */
public class DegreeForwardFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    String forwardDegreeDescription;

    String notFoundURI;

    String invalidURI;

    String app;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();

        try {
            forwardDegreeDescription = filterConfig.getInitParameter("forwardDegreeDescription");
            notFoundURI = filterConfig.getInitParameter("notFoundURI");
            invalidURI = filterConfig.getInitParameter("invalidURI");
            app = filterConfig.getInitParameter("app");
        } catch (Exception e) {
        }
    }

    public void destroy() {
        this.servletContext = null;
        this.filterConfig = null;
    }

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        final String uri = request.getRequestURI();
        if (uri.endsWith(".do")) {
            chain.doFilter(request, response);
            return;
        }

        String context = request.getContextPath();
        String newURI = uri.replaceFirst(app, "");
        String[] tokens = newURI.split("/");
        if (tokens.length > 0 && tokens[0].length() == 0) {
            String[] tokensTemp = new String[tokens.length - 1];
            for (int i = 1; i < tokens.length; i++) {
                tokensTemp[i - 1] = tokens[i];
            }
            tokens = tokensTemp;
        }

        try {
            if (tokens.length != 1 || !isDegree(tokens[0])) {
                chain.doFilter(request, response);
                return;
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }

        StringBuilder forwardURI = new StringBuilder(context);

        String degreeCode = tokens[0];
        Integer degreeId;
        try {
            degreeId = getDegreeId(degreeCode);
        } catch (FenixServiceException e) {
            throw new ServletException(e);
        } catch (FenixFilterException e) {
            throw new ServletException(e);
        }

        if (degreeId != null) {
            try {
                InfoExecutionPeriod infoExecutionPeriod = getCurrentExecutionPeriod();
                if (infoExecutionPeriod != null) {
                    Integer executionPeriodId = infoExecutionPeriod.getIdInternal();
                    forwardURI.append(forwardDegreeDescription);
                    forwardURI.append(executionPeriodId);
                    forwardURI.append("&degreeID=");
                    forwardURI.append(degreeId);
                } else {
                    forwardURI.append(notFoundURI);
                }

            } catch (FenixServiceException e1) {
                throw new ServletException(e1);
            } catch (FenixFilterException e) {
                throw new ServletException(e);
            }
        } else {
            forwardURI.append(notFoundURI);
        }
        response.sendRedirect(forwardURI.toString());
    }

    private boolean isDegree(final String string) {
        return Degree.readBySigla(string) != null;
    }

    private InfoExecutionPeriod getCurrentExecutionPeriod() throws FenixServiceException,
            FenixFilterException {

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(
                null, "ReadCurrentExecutionPeriod", null);
        return infoExecutionPeriod;
    }

    /**
     * @param degreeCode
     * @return
     * @throws FenixServiceException
     */
    private Integer getDegreeId(String degreeCode) throws FenixServiceException, FenixFilterException {
        Object args[] = { degreeCode };
        Integer executionCourseOID = new Integer(0);

        executionCourseOID = (Integer) ServiceUtils.executeService(null,
                "ReadDegreeIdInternalByDegreeCode", args);

        return executionCourseOID;
    }

}
