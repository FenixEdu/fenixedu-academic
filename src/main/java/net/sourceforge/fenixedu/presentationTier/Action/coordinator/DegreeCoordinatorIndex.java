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

package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.inquiries.CoordinatorInquiryTemplate;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.CoordinatorApplication.CoordinatorManagementApp;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
        CoordinatorInquiryTemplate coordinatorInquiryTemplate = CoordinatorInquiryTemplate.getCurrentTemplate();
        if (coordinatorInquiryTemplate != null) {
            final ExecutionDegree executionDegree = (ExecutionDegree) request.getAttribute(PresentationConstants.MASTER_DEGREE);
            if (executionDegree != null && executionDegree.getCoordinatorByTeacher(AccessControl.getPerson()) != null) {
                return new ActionForward("/viewInquiriesResults.do?method=prepare");
            }
        }
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