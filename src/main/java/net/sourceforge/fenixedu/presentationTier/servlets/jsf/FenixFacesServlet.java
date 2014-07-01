/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.servlets.filters.RequestWrapperFilter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@MultipartConfig
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
            facesServlet.service(RequestWrapperFilter.getFenixHttpServletRequestWrapper((HttpServletRequest) request), response);
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
