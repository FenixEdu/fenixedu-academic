/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package org.fenixedu.academic.servlet;

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

import org.fenixedu.bennu.struts.servlet.RequestWrapperFilter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@MultipartConfig
@WebServlet(urlPatterns = "*.faces")
public class FenixFacesServlet implements Servlet {

    private final FacesServlet facesServlet;

    public FenixFacesServlet() {
        super();
        facesServlet = new FacesServlet();

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        facesServlet.init(config);

        AddSignatureToViewStateObjects taskToAddSignatures =  new AddSignatureToViewStateObjects();
        try {
            taskToAddSignatures.runTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServletConfig getServletConfig() {
        return facesServlet.getServletConfig();
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        facesServlet.service(RequestWrapperFilter.getFenixHttpServletRequestWrapper((HttpServletRequest) request), response);
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
