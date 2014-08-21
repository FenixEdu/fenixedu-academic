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
/*
 * Created on 2004/04/07
 *
 */
package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ExceptionConfig;
import org.fenixedu.bennu.core.security.Authenticate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Luis Cruz
 * 
 * @deprecated Use Bennu's own Exception Handling mechanisms.
 *             This handler is scheduler to be removed in version 4.0.
 */
@Deprecated
public class FenixContainerExceptionHandler extends FenixExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(FenixContainerExceptionHandler.class);

    @Override
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
            HttpServletRequest request, HttpServletResponse response) throws ServletException {

        if (!FenixConfigurationManager.getConfiguration().useLegacyErrorHandling()) {
            // Re-throw the exception, allowing the container to catch it
            if (ex instanceof ServletException) {
                throw (ServletException) ex;
            } else {
                throw new ServletException(ex);
            }
        }

        logger.error("Request at " + request.getRequestURI() + " threw an exception: ", ex);

        if (Authenticate.isLogged()) {
            request.setAttribute("loggedPersonEmail", Authenticate.getUser().getPerson().getDefaultEmailAddressValue());
        }

        super.execute(ex, ae, mapping, formInstance, request, response);

        return new ActionForward("error-page", "/showErrorPage.do", false, "/");
    }
}