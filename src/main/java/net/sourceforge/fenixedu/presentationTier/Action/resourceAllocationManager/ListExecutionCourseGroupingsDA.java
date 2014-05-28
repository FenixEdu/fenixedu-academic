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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.RAMApplication.RAMExecutionCoursesApp;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = RAMExecutionCoursesApp.class, path = "list-groupings",
        titleKey = "link.list.execution.course.groupings")
@Mapping(path = "/listExecutionCourseGroupings", module = "resourceAllocationManager")
@Forwards(@Forward(name = "show-choose-execution-period-page",
        path = "/resourceAllocationManager/listExecutionCourseGroupings_bd.jsp"))
public class ListExecutionCourseGroupingsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN, new ContextSelectionBean());
        return mapping.findForward("show-choose-execution-period-page");
    }

    @EntryPoint
    public ActionForward selectExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ContextSelectionBean contextSelectionBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        if (contextSelectionBean == null) {
            request.setAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN, new ContextSelectionBean());
            return mapping.findForward("show-choose-execution-period-page");
        }

        request.setAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN, contextSelectionBean);
        return mapping.findForward("show-choose-execution-period-page");
    }

    public ActionForward downloadExecutionCourseGroupings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString(request
                        .getParameter(PresentationConstants.ACADEMIC_INTERVAL));

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=executionCourseGroupings_"
                + academicInterval.getPathName().replace(' ', '_') + "_.xls");

        final ServletOutputStream servletOutputStream = response.getOutputStream();
        exportToXls(servletOutputStream, academicInterval);
        servletOutputStream.flush();
        response.flushBuffer();

        return null;
    }

    private void exportToXls(final ServletOutputStream servletOutputStream, final AcademicInterval academicInterval)
            throws IOException {
        final List<Object> headers = getHeaders();
        final Spreadsheet spreadsheet = new Spreadsheet("Execution Course Groupings", headers);
        fillSpreadSheet(spreadsheet, academicInterval);
        spreadsheet.exportToXLSSheet(servletOutputStream);
    }

    private List<Object> getHeaders() {
        final List<Object> headers = new ArrayList<Object>();
        headers.add("Execution Course");
        headers.add("Responsible Fors");
        headers.add("Number of Degrees");
        headers.add("Degrees");
        headers.add("Curricular Years");
        headers.add("Degree Types");
        headers.add("Emails");
        headers.add("Enrolments");
        headers.add("Attends");
        return headers;
    }

    private void fillSpreadSheet(final Spreadsheet spreadsheet, final AcademicInterval academicInterval) {
        for (final ExecutionCourse executionCourse : ExecutionCourse.filterByAcademicInterval(academicInterval)) {
            final Row row = spreadsheet.addRow();

            row.setCell(executionCourse.getNome());

            final StringBuilder responsibleForStringBuilder = new StringBuilder();
            final StringBuilder responsibleForEmailsStringBuilder = new StringBuilder();
            boolean isFirstResp = true;
            for (final Professorship professorship : executionCourse.getProfessorships()) {
                if (professorship.getResponsibleFor().booleanValue()) {
                    if (isFirstResp) {
                        isFirstResp = false;
                    } else {
                        responsibleForStringBuilder.append("; ");
                        responsibleForEmailsStringBuilder.append("; ");
                    }

                    final Teacher teacher = professorship.getTeacher();
                    responsibleForStringBuilder.append(teacher.getPerson().getIstUsername());

                    responsibleForStringBuilder.append(" ");

                    final Person person = teacher.getPerson();
                    responsibleForStringBuilder.append(person.getName());
                    responsibleForEmailsStringBuilder.append(person.getEmail());
                }
            }
            row.setCell(responsibleForStringBuilder.toString());

            final Map<Degree, Set<Integer>> degreeOccurenceMap = constructDegreeOccurenceMap(academicInterval, executionCourse);

            row.setCell(Integer.toString(degreeOccurenceMap.size()));

            final StringBuilder degreeStringBuilder = new StringBuilder();
            boolean isFirst = true;
            for (final Degree degree : degreeOccurenceMap.keySet()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    degreeStringBuilder.append("; ");
                }

                degreeStringBuilder.append(degree.getSigla());
            }
            row.setCell(degreeStringBuilder.toString());

            final StringBuilder degreeCurricularYearStringBuilder = new StringBuilder();
            isFirst = true;
            for (final Entry<Degree, Set<Integer>> entry : degreeOccurenceMap.entrySet()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    degreeCurricularYearStringBuilder.append("; ");
                }

                final Degree degree = entry.getKey();
                degreeCurricularYearStringBuilder.append(degree.getSigla());
                degreeCurricularYearStringBuilder.append('(');
                boolean isFirstYear = true;
                for (final Integer curricularYearInteger : entry.getValue()) {
                    if (isFirstYear) {
                        isFirstYear = false;
                    } else {
                        degreeCurricularYearStringBuilder.append(", ");
                    }
                    degreeCurricularYearStringBuilder.append(curricularYearInteger.toString());
                }
                degreeCurricularYearStringBuilder.append(')');
            }
            row.setCell(degreeCurricularYearStringBuilder.toString());

            final StringBuilder degreeTypeStringBuilder = new StringBuilder();
            final Set<DegreeType> degreeTypes = new TreeSet<DegreeType>();
            for (final Degree degree : degreeOccurenceMap.keySet()) {
                degreeTypes.add(degree.getDegreeType());
            }
            for (final DegreeType degreeType : degreeTypes) {
                if (degreeTypeStringBuilder.length() > 0) {
                    degreeStringBuilder.append(", ");
                }
                degreeTypeStringBuilder.append(degreeType);
            }
            row.setCell(degreeTypeStringBuilder.toString());

            row.setCell(responsibleForEmailsStringBuilder.toString());

            int enrolmentCount = 0;
            for (final Attends attends : executionCourse.getAttendsSet()) {
                if (attends.hasEnrolment()) {
                    enrolmentCount++;
                }
            }
            row.setCell(enrolmentCount);

            row.setCell(executionCourse.getAttendsSet().size());
        }
    }

    private Map<Degree, Set<Integer>> constructDegreeOccurenceMap(final AcademicInterval academicInterval,
            final ExecutionCourse executionCourse) {
        final Map<Degree, Set<Integer>> degreeOccurenceMap = new HashMap<Degree, Set<Integer>>();
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            final List<DegreeModuleScope> degreeModuleScopes =
                    curricularCourse.getActiveDegreeModuleScopesInAcademicInterval(academicInterval);
            for (final DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
                final Degree degree = curricularCourse.getDegreeCurricularPlan().getDegree();

                final Set<Integer> curricularYears;
                if (degreeOccurenceMap.containsKey(degree)) {
                    curricularYears = degreeOccurenceMap.get(degree);
                } else {
                    curricularYears = new TreeSet<Integer>();
                    degreeOccurenceMap.put(degree, curricularYears);
                }

                curricularYears.add(degreeModuleScope.getCurricularYear());
            }
        }
        return degreeOccurenceMap;
    }

}