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
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.resourceAllocationManager.FirstYearShiftsBean;
import org.fenixedu.academic.ui.struts.action.base.FenixContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.RAMApplication.RAMFirstYearShiftsApp;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

@StrutsFunctionality(app = RAMFirstYearShiftsApp.class, path = "export-shifts", titleKey = "link.firstYearShifts.export")
@Mapping(module = "resourceAllocationManager", path = "/exportFirstYearShifts")
@Forwards(@Forward(name = "chooseExport", path = "/resourceAllocationManager/exportFirstYearShifts_bd.jsp"))
public class ExportFirstYearShiftsDA extends FenixContextDispatchAction {

    @EntryPoint
    public ActionForward chooseExport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        FirstYearShiftsBean bean = (FirstYearShiftsBean) getRenderedObject();
        if (bean == null) {
            bean = new FirstYearShiftsBean();
        }
        request.setAttribute("first_year_shifts_export", bean);

        return mapping.findForward("chooseExport");
    }

    public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        final FirstYearShiftsBean bean = getRenderedObject();
        if (bean == null) {
            return chooseExport(mapping, form, request, response);
        }

        final AcademicInterval executionYear = bean.getExecutionYear().getAcademicInterval();
        final EntryPhase phase = bean.getEntryPhase();
        final List<Degree> degrees =
                Degree.readAllMatching(DegreeType.oneOf(DegreeType::isBolonhaDegree, DegreeType::isIntegratedMasterDegree));

        if (executionYear == null) {
            return chooseExport(mapping, form, request, response);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=occupationMap_"
                + executionYear.getPresentationName().replace('/', '_') + " " + phase.getLocalizedName() + ".xls");

        final Spreadsheet spreadsheet = new Spreadsheet("Shifts");
        addHeader(spreadsheet, phase);

        for (Degree degree : degrees) {
            for (final DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {

                final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByAcademicInterval(executionYear);

                if (executionDegree != null) {
                    for (final SchoolClass schoolClass : executionDegree.getSchoolClassesSet()) {
                        if (schoolClass.getAnoCurricular().equals(Integer.valueOf(1))
                                && schoolClass.getExecutionPeriod().isFirstOfYear()) {

                            for (final Shift shift : schoolClass.getAssociatedShiftsSet()) {

                                final ExecutionCourse executionCourse = shift.getExecutionCourse();
                                final String ecName = executionCourse.getNome();

                                addRow(spreadsheet, degreeCurricularPlan, executionCourse, schoolClass, shift, phase);
                            }
                        }
                    }
                }
            }
        }

        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.exportToXLSSheet(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    private void addHeader(final Spreadsheet spreadsheet, final EntryPhase phase) {
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "label.manager.degree.name"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "label.class"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "property.executionCourse.nameI18N.content"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "property.shift"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "property.shift.type"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "property.shift.capacity"));
        if (!phase.equals(EntryPhase.FIRST_PHASE)) {
            spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "property.shift.ocupation"));
        }

    }

    private void addRow(final Spreadsheet spreadsheet, final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionCourse executionCourse, final SchoolClass schoolClass, final Shift shift, final EntryPhase phase) {
        final Row row = spreadsheet.addRow();

        row.setCell(degreeCurricularPlan.getName());
        row.setCell(schoolClass.getNome());
        row.setCell(executionCourse.getNome());
        row.setCell(shift.getNome());
        row.setCell(shift.getShiftTypesPrettyPrint());

        int capacity = shift.getLotacao() - shift.getStudentsSet().size();
        if (phase.equals(EntryPhase.FIRST_PHASE)) {
            row.setCell(String.valueOf(capacity < 0 ? capacity * -1 : capacity));
        } else {
            row.setCell(String.valueOf(capacity));
        }

        if (!phase.equals(EntryPhase.FIRST_PHASE)) {
            row.setCell(String.valueOf(shift.getStudentsSet().size()));
        }
    }
}
