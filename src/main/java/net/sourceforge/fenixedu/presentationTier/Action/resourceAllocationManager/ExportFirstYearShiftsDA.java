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
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.FirstYearShiftsBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.RAMApplication.RAMFirstYearShiftsApp;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

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
                Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);

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
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION,
                "label.manager.degree.name"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION,
                "label.class"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION,
                "property.executionCourse.name"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION,
                "property.shift"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION,
                "property.shift.type"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION,
                "property.shift.capacity"));
        if (!phase.equals(EntryPhase.FIRST_PHASE)) {
            spreadsheet.setHeader(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION,
                    "property.shift.ocupation"));
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
