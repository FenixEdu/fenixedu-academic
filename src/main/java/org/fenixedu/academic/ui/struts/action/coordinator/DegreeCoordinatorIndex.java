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
 * Created on 27 of March de 2003
 *
 *
 * Autores : -Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *
 * modified by Fernanda Quit�rio
 *
 */

package org.fenixedu.academic.ui.struts.action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.coordinator.CoordinatorApplication.CoordinatorManagementApp;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@StrutsFunctionality(app = CoordinatorManagementApp.class, path = "degree", titleKey = "coordinator")
@Mapping(path = "/coordinatorIndex", module = "coordinator")
@Forwards(@Forward(name = "Success", path = "/coordinator/welcomeScreen.jsp"))
public class DegreeCoordinatorIndex extends FenixAction {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException {
        setCoordinatorContext(request);
        return mapping.findForward("Success");
    }

    public static void setCoordinatorContext(final HttpServletRequest request) {
        String degreeCurricularPlanOID = findDegreeCurricularPlanID(request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanOID);
        if (degreeCurricularPlan != null) {
            request.setAttribute(PresentationConstants.MASTER_DEGREE, degreeCurricularPlan.getMostRecentExecutionDegree());
            request.setAttribute("isCoordinator",
                    degreeCurricularPlan.getDegree().isCoordinatorInSomeExecutionYear(AccessControl.getPerson()));
            request.setAttribute("isScientificCommissionMember", degreeCurricularPlan.getDegree()
                    .isMemberOfAnyScientificCommission(AccessControl.getPerson()));
        }
    }

    public static String findDegreeCurricularPlanID(HttpServletRequest request) {
        String paramValue = request.getParameter("degreeCurricularPlanID");
        if (!Strings.isNullOrEmpty(paramValue)) {
            return paramValue;
        }
        String attribute = (String) request.getAttribute("degreeCurricularPlanID");
        if (!Strings.isNullOrEmpty(attribute)) {
            return attribute;
        }
        return null;
    }

}