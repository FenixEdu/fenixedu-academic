/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.servlets.jsf;

import java.io.IOException;

import javax.faces.webapp.FacesServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class FenixFacesServlet implements Servlet {

    final FacesServlet facesServlet;

    public FenixFacesServlet() {
        super();
        facesServlet = new FacesServlet(); 
    }

    public void init(ServletConfig config) throws ServletException {
        facesServlet.init(config);
    }

    public ServletConfig getServletConfig() {
        return facesServlet.getServletConfig();
    }

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        try {
            facesServlet.service(request, response);
        } catch (IOException e) {
            e.printStackTrace();
            handleException(request, response, e);
            throw e;            
        } catch (ServletException e) {
            e.printStackTrace();
            int index = e.getMessage().indexOf("IllegalDataAccessException");
            if(index > -1){
                String message = e.getMessage().substring(index);
                throw new IllegalDataAccessException(message);
            }
            handleException(request, response, e);
            throw e;
        }
    }

    private void handleException(ServletRequest request, ServletResponse response, Exception e) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpServletRequest.getSession(false).setAttribute(SessionConstants.EXCEPTION_STACK_TRACE, e.getStackTrace());
    }

    public String getServletInfo() {
        return facesServlet.getServletInfo();
    }

    public void destroy() {
        facesServlet.destroy();
    }


}
