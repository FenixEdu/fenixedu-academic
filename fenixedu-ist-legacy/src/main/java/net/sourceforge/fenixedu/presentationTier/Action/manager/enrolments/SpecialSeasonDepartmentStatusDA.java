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
package org.fenixedu.academic.ui.struts.action.manager.enrolments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeListingsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = DepartmentAdmOfficeListingsApp.class, path = "special-season-enrolments",
        titleKey = "label.course.specialSeasonEnrolments")
@Mapping(path = "/specialSeason/specialSeasonStatusTracker", module = "departmentAdmOffice")
@Forwards({ @Forward(name = "selectCourse", path = "/departmentAdmOffice/lists/specialSeason/selectCourse.jsp"),
        @Forward(name = "listStudents", path = "/departmentAdmOffice/lists/specialSeason/listStudents.jsp") })
public class SpecialSeasonDepartmentStatusDA extends SpecialSeasonStatusTrackerDA {

    @Override
    public ActionForward selectCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        if (bean == null) {
            bean = new SpecialSeasonStatusTrackerBean();
            bean.setDepartment(getDepartment());
        }
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward("selectCourse");
    }

    @Override
    protected Department getDepartment() {
        return AccessControl.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }

}
