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
 * Created on 26/Fev/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.EmptyRequiredFieldServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * @author João Mota
 */
public class FenixErrorExceptionHandler extends ExceptionHandler {

    public FenixErrorExceptionHandler() {
        super();
    }

    @Override
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
            HttpServletRequest request, HttpServletResponse response) throws ServletException {

        super.execute(ex, ae, mapping, formInstance, request, response);

        ActionError error = null;
        String property = null;

        // Figure out the error
        ActionForward forward = mapping.getInputForward();
        if (ex instanceof FenixActionException) {
            FenixActionException fenixActionException = (FenixActionException) ex;
            error = ((FenixActionException) ex).getError();
            property = ((FenixActionException) ex).getProperty();
            forward =
                    fenixActionException.getActionForward() != null ? fenixActionException.getActionForward() : mapping
                            .getInputForward();
        } else if (ex instanceof EmptyRequiredFieldServiceException) {
            error = new ActionError(ex.getMessage());
            property = error.getKey();
        } else {
            String[] args = null;
            if (ex instanceof FenixServiceException) {
                final FenixServiceException fenixServiceException = (FenixServiceException) ex;
                args = fenixServiceException.getArgs();
            }
            if (args == null) {
                error = new ActionError(ae.getKey(), ex.getMessage());
            } else {
                error = new ActionError(ae.getKey(), args);
            }
            property = error.getKey();
        }
        if (ae.getPath() != null && ae.getPath().length() > 0) {
            forward = new ActionForward(ae.getPath());
        }
        request.setAttribute(Globals.EXCEPTION_KEY, ex);
        super.storeException(request, property, error, forward, ae.getScope());
        return forward;
    }

}