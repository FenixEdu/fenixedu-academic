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
package org.fenixedu.academic.ui.struts.action.delegate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;

@StrutsApplication(bundle = "DelegateResources", path = "delegate", titleKey = "label.delegatesPortal",
        accessGroup = "role(DELEGATE)", hint = "Delegate")
@Mapping(module = "delegate", path = "/index")
@Forwards(value = { @Forward(name = "success", path = "/delegate/index.jsp") })
public class ReadDelegateDegreeDispatchAction extends FenixAction {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException {
        final Person person = getLoggedPerson(request);
        if (person.getStudent() != null) {
            final Registration lastActiveRegistration = person.getStudent().getLastActiveRegistration();
            if (lastActiveRegistration != null) {
                final Degree degree = lastActiveRegistration.getDegree();
                final ExecutionDegree executionDegree = degree.getMostRecentDegreeCurricularPlan().getMostRecentExecutionDegree();
                final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                request.setAttribute(PresentationConstants.MASTER_DEGREE, infoExecutionDegree);
            }
        }
        return mapping.findForward("success");
    }
}
