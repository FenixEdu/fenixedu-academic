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

import DataBeans.dto.SchoolClassDTO;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author João Mota
 *  
 */
public class SchoolClassForwardFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    String forwardURI;
  
    String notFoundURI;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
        try {
            forwardURI = filterConfig.getInitParameter("forwardURI");
            notFoundURI = filterConfig.getInitParameter("notFoundURI");
        } catch (Exception e) {
            System.out.println("Could not get init parameter 'forwardURI'.");
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

        String uri = request.getRequestURI();
        String context = request.getContextPath();
        String[] tokens = uri.split("/");
        String schoolClassName = tokens[tokens.length - 1];
        Object args[] = { schoolClassName };
        StringBuffer schoolClassURI = new StringBuffer();
        if (context.length() > 1) {
            schoolClassURI.append(context);
        }
        try {
            SchoolClassDTO schoolClassDTO = (SchoolClassDTO) ServiceUtils.executeService(null,
                    "ReadSchoolClassByNameInCurrentExecutionPeriod", args);
            if (schoolClassDTO != null) {
                schoolClassURI.append(forwardURI);
                schoolClassURI.append("?executionPeriodOID=");
                schoolClassURI.append(schoolClassDTO.getExecutionPeriodId());
                schoolClassURI.append("&classId=");
                schoolClassURI.append(schoolClassDTO.getSchoolClassId());
                schoolClassURI.append("&nameDegreeCurricularPlan=");
                schoolClassURI.append(schoolClassDTO.getDegreeCurricularPlanName());
                schoolClassURI.append("&degreeInitials=");
                schoolClassURI.append(schoolClassDTO.getDegreeInitials());
                schoolClassURI.append("&degreeID=");
                schoolClassURI.append(schoolClassDTO.getDegreeId());
                schoolClassURI.append("&degreeCurricularPlanID=");
                schoolClassURI.append(schoolClassDTO.getDegreeCurricularPlanId());
                schoolClassURI.append("&index=0");
                schoolClassURI.append("&className=");
                schoolClassURI.append(schoolClassDTO.getSchoolClassName());
            } else {                
                schoolClassURI.append(notFoundURI);
            }
            response.sendRedirect(schoolClassURI.toString());
        } catch (FenixServiceException e) {
            schoolClassURI.append(notFoundURI);
            response.sendRedirect(schoolClassURI.toString());
            e.printStackTrace();
        }

    }

}