/*
 * Created on 12/Nov/2004
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

import org.apache.commons.lang.StringUtils;

import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author Pedro Santos & Rita Carvalho
 */
public class ExamsForwardFilter implements Filter{
	
	ServletContext servletContext;

    FilterConfig filterConfig;

    String forwardExamsList;
    String notFoundURI;
    String app;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
        try {
            forwardExamsList = filterConfig.getInitParameter("forwardExamsList");
            notFoundURI = filterConfig.getInitParameter("notFoundURI");
            app = filterConfig.getInitParameter("app");
        } catch (Exception e) {
            System.out.println("Could not get init parameter 'forwardURI'.");
        }
	}
	
	public void destroy() {
		this.servletContext = null;
        this.filterConfig = null;
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        String uri = request.getRequestURI();
        
        if(!StringUtils.contains(uri, "/exames")){
        	chain.doFilter(request, response);
        	return;
        }
        
        String context = request.getContextPath();
        String newURI = uri.replaceFirst(app, "");
        String[] tokens = newURI.split("/");
        
        StringBuffer forwardURI = new StringBuffer(context);
        
        if(tokens.length >= 2 && !(tokens[1].equalsIgnoreCase("exames"))){
        	forwardURI.append(notFoundURI);
        }
        else{
	        
	        if(tokens.length == 2){
	        	String degreeCode = tokens[tokens.length - 2];
	        	Integer degreeId;
                try {
                    degreeId = getDegreeId(degreeCode);
                } catch (FenixServiceException e) {
                    throw new ServletException(e);
                }
                if(degreeId != null){
		        	forwardURI.append(forwardExamsList);
		        	forwardURI.append("&degreeID=");
		        	forwardURI.append(degreeId);
		        	InfoExecutionDegree infoExecutionDegree;
                    try {
                        infoExecutionDegree = getExecutionDegree(degreeId);
                    } catch (FenixServiceException e1) {
                        throw new ServletException(e1);
                    }
                    if(infoExecutionDegree != null){
                        forwardURI.append("&degreeCurricularPlanID=");
		        		forwardURI.append(infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());
		        		forwardURI.append("&executionDegreeID=");
                        forwardURI.append(infoExecutionDegree.getIdInternal());
                        
		        	} else {
		        		forwardURI.delete(0, forwardURI.length()-1);
		        		forwardURI.append(context);
		        		forwardURI.append(notFoundURI);
		        	}
		        } else {
		        	forwardURI.append(notFoundURI);
		        }
	        } else {
	        	forwardURI.append(notFoundURI);
	        }
	        
        }
        response.sendRedirect(forwardURI.toString());
	}

	

    /**
	 * @param degreeId
	 * @return
	 * @throws IOException
	 * @throws FenixServiceException
	 */
	private InfoExecutionDegree getExecutionDegree(Integer degreeId) throws FenixServiceException {
		Object args[] = { degreeId };
        
        Integer degreeCurricularPlanId = new Integer(0);
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(null,
	            "ReadExecutionDegreeRecentByDegreeId", args);
	    
		return infoExecutionDegree;
	}

	/**
	 * @param degreeCode
	 * @return
	 * @throws IOException
	 * @throws FenixServiceException
	 */
	private Integer getDegreeId(String degreeCode) throws FenixServiceException {
		
        Object args[] = { degreeCode };
        Integer degreeOID = new Integer(0);

        degreeOID = (Integer) ServiceUtils.executeService(null,
                    "ReadDegreeIdInternalByDegreeCode", args);
        return degreeOID;
	}

}
