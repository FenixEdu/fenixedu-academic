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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;

import pt.ist.fenixframework.FenixFramework;

@Forwards({ @Forward(name = "viewRegistrationDetails",
        path = "/academicAdminOffice/student/registration/viewRegistrationDetails.jsp") })
public abstract class StudentRegistrationDA extends FenixDispatchAction {

    protected Registration getAndSetRegistration(final HttpServletRequest request) {
        final String registrationID =
                getFromRequest(request, "registrationID") != null ? getFromRequest(request, "registrationID").toString() : getFromRequest(
                        request, "registrationId").toString();
        final Registration registration = FenixFramework.getDomainObject(registrationID);
        request.setAttribute("registration", registration);
        return registration;
    }

    public ActionForward visualizeRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        getAndSetRegistration(request);
        return mapping.findForward("viewRegistrationDetails");
    }
}
