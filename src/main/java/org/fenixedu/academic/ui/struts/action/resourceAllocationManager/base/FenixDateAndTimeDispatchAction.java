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
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager.base;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.ui.struts.action.base.FenixContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;

public abstract class FenixDateAndTimeDispatchAction extends FenixContextDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Calendar examDateAndTime = Calendar.getInstance();
        Long dateAndTime = null;
        try {
            dateAndTime = new Long(request.getParameter(PresentationConstants.EXAM_DATEANDTIME));
        } catch (NumberFormatException ex) {
            examDateAndTime = (Calendar) request.getAttribute(PresentationConstants.EXAM_DATEANDTIME);
            if (examDateAndTime != null) {
                request.setAttribute(PresentationConstants.EXAM_DATEANDTIME, examDateAndTime);
            } else {
                request.removeAttribute(PresentationConstants.EXAM_DATEANDTIME);
                request.setAttribute(PresentationConstants.EXAM_DATEANDTIME, null);
            }
        }

        if (dateAndTime != null) {
            examDateAndTime.setTimeInMillis(dateAndTime.longValue());
            request.setAttribute(PresentationConstants.EXAM_DATEANDTIME, examDateAndTime);
        }

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        return actionForward;
    }

}
