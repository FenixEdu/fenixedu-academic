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
package org.fenixedu.academic.ui.struts.action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.util.email.CoordinatorSender;
import org.fenixedu.academic.domain.util.email.Sender;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.messaging.EmailsDA;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "coordinator", path = "/sendEmail", functionality = DegreeCoordinatorIndex.class)
public class SendEmail extends FenixAction {

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        String parameter = request.getParameter("degreeCurricularPlanID");

        if (parameter == null) {
            return null;
        }

        try {
            return FenixFramework.getDomainObject(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Degree degree = getDegreeCurricularPlan(request).getDegree();
        Sender sender = CoordinatorSender.newInstance(degree);
        return EmailsDA.sendEmail(request, sender);
    }
}
