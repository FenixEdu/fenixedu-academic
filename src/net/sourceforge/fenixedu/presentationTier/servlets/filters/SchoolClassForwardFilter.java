/*
 * Created on Jun 25, 2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters;

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

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.dto.SchoolClassDTO;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.lang.StringUtils;

/**
 * @author Pedro Santos & Rita Carvalho
 *  
 */
public class SchoolClassForwardFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    String forwardScheduleList;
    String forwardClassSchedule;
    String notFoundURI;
    String app;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
        try {
            forwardScheduleList = filterConfig.getInitParameter("forwardScheduleList");
            forwardClassSchedule = filterConfig.getInitParameter("forwardClassSchedule");
            notFoundURI = filterConfig.getInitParameter("notFoundURI");
            app = filterConfig.getInitParameter("app");
        } catch (Exception e) {
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
        
        if(!StringUtils.contains(uri, "/horarios")){
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

        StringBuilder forwardURI = new StringBuilder(context);
        
        if(tokens.length >=2 && !(tokens[1].equalsIgnoreCase("horarios"))){
        	forwardURI.append(notFoundURI);
        }
        else{
	        
	        if(tokens.length == 2){
	            /* fenix/curso/horarios */
	        	String degreeCode = tokens[tokens.length - 2];
	        	Integer degreeId;
                try {
                    degreeId = getDegreeId(degreeCode);
                } catch (FenixServiceException e) {
                    throw new ServletException(e);
                } catch (FenixFilterException e) {
                    throw new ServletException(e);
                }
                if(degreeId != null){
		        	forwardURI.append(forwardScheduleList);
		        	forwardURI.append(degreeId);
		        } else {
		        	forwardURI.append(notFoundURI);
		        }
	        }
	        
	        else if(tokens.length == 3){
	            String className = tokens[2];
	            String degreeCode = tokens[0];
	        	Integer degreeId;
                try {
                    degreeId = getDegreeId(degreeCode);
                } catch (FenixServiceException e) {
                    throw new ServletException(e);
                } catch (FenixFilterException e) {
                    throw new ServletException(e);
                }
                if (degreeId != null){
                    SchoolClassDTO schoolClassDTO;
                    try {
                        schoolClassDTO = getClass(className);
                    } catch (FenixServiceException e) {
                        throw new ServletException(e);
                    } catch (FenixFilterException e) {
                        throw new ServletException(e);
                    }
                    if (schoolClassDTO != null){
                        forwardURI.append(forwardClassSchedule);
                        forwardURI.append(schoolClassDTO.getSchoolClassId());
                        forwardURI.append("&nameDegreeCurricularPlan=");
                        forwardURI.append(schoolClassDTO.getDegreeCurricularPlanName());
                        forwardURI.append("&degreeInitials=");
                        forwardURI.append(schoolClassDTO.getDegreeInitials());
                        forwardURI.append("&degreeID=");
                        forwardURI.append(schoolClassDTO.getDegreeId());
                        forwardURI.append("&degreeCurricularPlanID=");
                        forwardURI.append(schoolClassDTO.getDegreeCurricularPlanId());
                        forwardURI.append("&className=");
                        forwardURI.append(schoolClassDTO.getSchoolClassName());
                    }
                }
	            
	        } else {
	        	forwardURI.append(notFoundURI);
	        }
        }
        response.sendRedirect(forwardURI.toString());
    }
    
    /**
     * @param className
     * @return
     * @throws FenixServiceException
     */
    private SchoolClassDTO getClass(final String className) throws FenixServiceException, FenixFilterException {
        Object args[] = { className };
        
        SchoolClassDTO schoolClassDTO = (SchoolClassDTO) ServiceUtils.executeService(null,
                "ReadSchoolClassByNameInCurrentExecutionPeriod", args);
        
        return schoolClassDTO;
    }

    /**
	 * @param degreeCode
	 * @return
	 * @throws IOException
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