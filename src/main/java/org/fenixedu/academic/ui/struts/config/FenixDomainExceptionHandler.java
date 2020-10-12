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
package org.fenixedu.academic.ui.struts.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.config.ExceptionConfig;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class FenixDomainExceptionHandler extends FenixExceptionHandler {

    @Override
    public ActionForward execute(Exception ex, ExceptionConfig exceptionConfig, ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws ServletException {

        ActionForward forward = mapping.getInputForward();

        if (ex instanceof DomainException) {
            super.execute(ex, exceptionConfig, mapping, actionForm, request, response);

            DomainException domainException = (DomainException) ex;
            String property = domainException.getKey();
            ActionMessage error = new ActionMessage(domainException.getLocalizedMessage(), false);
            super.storeException(request, property, error, forward, exceptionConfig.getScope());
        }
        return forward;
    }

}
