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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.extraCurricularActivities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExtraCurricularApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AcademicAdminExtraCurricularApp.class, path = "manage",
        titleKey = "label.manage.extraCurricularActivityTypes", accessGroup = "academic(MANAGE_EXTRA_CURRICULAR_ACTIVITIES)")
@Mapping(path = "/manageExtraCurricularActivities", module = "academicAdministration")
@Forwards({ @Forward(name = "list", path = "/academicAdminOffice/extraCurricularActivities/listActivities.jsp"),
        @Forward(name = "create", path = "/academicAdminOffice/extraCurricularActivities/createActivity.jsp"),
        @Forward(name = "edit", path = "/academicAdminOffice/extraCurricularActivities/editActivity.jsp") })
public class ManageExtraCurricularActivities extends FenixDispatchAction {

    @EntryPoint
    public ActionForward list(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("activityTypes", rootDomainObject.getExtraCurricularActivityTypeSet());
        return mapping.findForward("list");
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("create");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ExtraCurricularActivityType type = getDomainObject(request, "activityTypeId");
        if (type.hasAnyExtraCurricularActivity()) {
            addErrorMessage(request, "errors", "error.extraCurricularActivityTypes.unableToEditUsedType", type.getName()
                    .getContent());
            return list(mapping, actionForm, request, response);
        }
        request.setAttribute("extraCurricularActivityType", type);
        return mapping.findForward("edit");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ExtraCurricularActivityType type = getDomainObject(request, "activityTypeId");
        try {
            type.delete();
        } catch (DomainException e) {
            addErrorMessage(request, "errors", e.getKey(), e.getArgs());
        }
        return list(mapping, actionForm, request, response);
    }
}
