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
package org.fenixedu.academic.ui.struts.action.coordinator.inquiries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.inquiries.ViewQucAuditProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/auditResult", module = "coordinator", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "viewProcessDetails", path = "/pedagogicalCouncil/inquiries/viewProcessDetailsNoAction.jsp") })
public class ViewQucAuditProcessCoordinatorDA extends ViewQucAuditProcessDA {

    @Override
    public ActionForward viewProcessDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeCurricularPlan dcp = FenixFramework.getDomainObject(request.getParameter("degreeCurricularPlanOID"));
        request.setAttribute("degreeCurricularPlanID", dcp.getExternalId().toString());
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.viewProcessDetails(mapping, form, request, response);
    }
}
