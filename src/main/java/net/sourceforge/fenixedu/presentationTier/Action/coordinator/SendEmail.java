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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.util.email.CoordinatorSender;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.EmailsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
