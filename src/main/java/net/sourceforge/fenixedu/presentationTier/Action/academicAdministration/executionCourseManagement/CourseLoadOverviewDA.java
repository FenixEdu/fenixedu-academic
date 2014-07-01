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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExecutionsApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

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
        response.setHeader(
                "Content-disposition",
                "attachment; filename="
                        + BundleUtil.getString(Bundle.ACADEMIC,
                                "label.course.load.inconsistency.filename") + ".xls");

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
