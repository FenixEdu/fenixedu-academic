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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExecutionsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.StyledExcelSpreadsheet;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = AcademicAdminExecutionsApp.class, path = "course-load-overview",
        titleKey = "label.courseLoadOverview.viewInconsistencies", bundle = "AcademicAdminOffice",
        accessGroup = "academic(VIEW_SCHEDULING_OVERSIGHT)")
@Mapping(path = "/courseLoadOverview", module = "academicAdministration")
@Forwards({ @Forward(name = "viewInconsistencies", path = "/academicAdministration/courseLoadOverview/viewInconsistencies.jsp") })
public class CourseLoadOverviewDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward viewInconsistencies(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        CourseLoadOverviewBean bean = getRenderedObject("courseLoadOverviewBean");
        if (bean == null) {
            bean = new CourseLoadOverviewBean();
        }
        request.setAttribute("courseLoadOverviewBean", bean);

        RenderUtils.invalidateViewState();

        return mapping.findForward("viewInconsistencies");
    }

    public ActionForward downloadInconsistencies(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterOid");
        final CourseLoadOverviewBean bean = new CourseLoadOverviewBean(executionSemester);
        final StyledExcelSpreadsheet spreadsheet = bean.getInconsistencySpreadsheet();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition",
                "attachment; filename=" + BundleUtil.getString(Bundle.ACADEMIC, "label.course.load.inconsistency.filename")
                        + ".xls");

        try {
            final ServletOutputStream writer = response.getOutputStream();
            spreadsheet.getWorkbook().write(writer);
            writer.close();
        } catch (final IOException e) {
            throw new Error(e);
        }

        return null;
    }

}
