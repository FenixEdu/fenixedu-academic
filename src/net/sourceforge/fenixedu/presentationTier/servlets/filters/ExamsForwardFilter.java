/*
 * Created on 12/Nov/2004
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
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.lang.StringUtils;

/**
 * @author Pedro Santos & Rita Carvalho
 */
public class ExamsForwardFilter implements Filter{
	
	ServletContext servletContext;

    FilterConfig filterConfig;

    String forwardExamsList;
    String notFoundURI;
    String app;

	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
        try {
            forwardExamsList = filterConfig.getInitParameter("forwardExamsList");
            notFoundURI = filterConfig.getInitParameter("notFoundURI");
            app = filterConfig.getInitParameter("app");
        } catch (Exception e) {
        }
	}
	
	public void destroy() {
		this.servletContext = null;
        this.filterConfig = null;
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
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
        if (tokens.length > 0 && tokens[0].length() == 0) {
            String[] tokensTemp = new String[tokens.length - 1];
            for (int i = 1; i < tokens.length; i++) {
                tokensTemp[i - 1] = tokens[i];
            }
            tokens = tokensTemp;
        }
        
        StringBuilder forwardURI = new StringBuilder(context);
        
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
                } catch (FenixFilterException e) {
                    throw new ServletException(e);
                }
                if(degreeId != null){
		        	forwardURI.append(forwardExamsList);
		        	forwardURI.append("&degreeID=");
		        	forwardURI.append(degreeId);

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
	 * @param degreeCode
	 * @return
	 * @throws IOException
	 * @throws FenixServiceException
	 */
	private Integer getDegreeId(String degreeCode) throws FenixServiceException, FenixFilterException {
		
        Object args[] = { degreeCode };
        Integer degreeOID = new Integer(0);

        degreeOID = (Integer) ServiceUtils.executeService(null,
                    "ReadDegreeIdInternalByDegreeCode", args);
        return degreeOID;
	}

}
