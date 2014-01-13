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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@WebServlet(urlPatterns = "*.faces")
public class FenixFacesServlet implements Servlet {

    private static final Logger logger = LoggerFactory.getLogger(FenixFacesServlet.class);

    public static ServletConfig servletConfig = null;

    final FacesServlet facesServlet;

    public FenixFacesServlet() {
        super();
        facesServlet = new FacesServlet();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        facesServlet.init(config);
        servletConfig = config;
    }

    @Override
    public ServletConfig getServletConfig() {
        return facesServlet.getServletConfig();
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        try {
            facesServlet.service(request, response);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            handleException(request, response, e);
            throw e;
        } catch (ServletException e) {
            logger.error(e.getMessage(), e);
            int index = e.getMessage().indexOf("IllegalDataAccessException");
            if (index > -1) {
                String message = e.getMessage().substring(index);
                throw new IllegalDataAccessException(message);
            }
            handleException(request, response, e);
            throw e;
        }
    }

    private void handleException(ServletRequest request, ServletResponse response, Exception e) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpServletRequest.setAttribute(PresentationConstants.EXCEPTION_STACK_TRACE, e.getStackTrace());
    }

    @Override
    public String getServletInfo() {
        return facesServlet.getServletInfo();
    }

    @Override
    public void destroy() {
        facesServlet.destroy();
    }

}
