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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.resourceAllocationManager.ContextSelectionBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.RAMApplication.RAMExecutionCoursesApp;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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
        for (final ExecutionCourse executionCourse : filterByAcademicInterval(academicInterval)) {
            final Row row = spreadsheet.addRow();

            row.setCell(executionCourse.getNome());

            final StringBuilder responsibleForStringBuilder = new StringBuilder();
            final StringBuilder responsibleForEmailsStringBuilder = new StringBuilder();
            boolean isFirstResp = true;
            for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                if (professorship.getResponsibleFor().booleanValue()) {
                    if (isFirstResp) {
                        isFirstResp = false;
                    } else {
                        responsibleForStringBuilder.append("; ");
                        responsibleForEmailsStringBuilder.append("; ");
                    }

                    final Teacher teacher = professorship.getTeacher();
                    responsibleForStringBuilder.append(teacher.getPerson().getUsername());

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
                if (attends.getEnrolment() != null) {
                    enrolmentCount++;
                }
            }
            row.setCell(enrolmentCount);

            row.setCell(executionCourse.getAttendsSet().size());
        }
    }
    
    private static Collection<ExecutionCourse> filterByAcademicInterval(AcademicInterval academicInterval) {
        ExecutionInterval executionInterval = ExecutionInterval.getExecutionInterval(academicInterval);

        return executionInterval == null ? Collections.<ExecutionCourse> emptyList() : executionInterval
                .getAssociatedExecutionCoursesSet();
    }

    private Map<Degree, Set<Integer>> constructDegreeOccurenceMap(final AcademicInterval academicInterval,
            final ExecutionCourse executionCourse) {
        final Map<Degree, Set<Integer>> degreeOccurenceMap = new HashMap<Degree, Set<Integer>>();
//        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
//            final List<DegreeModuleScope> degreeModuleScopes =
//                    curricularCourse.getActiveDegreeModuleScopesInAcademicInterval(academicInterval);
//            for (final DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
//                final Degree degree = curricularCourse.getDegreeCurricularPlan().getDegree();
//
//                final Set<Integer> curricularYears;
//                if (degreeOccurenceMap.containsKey(degree)) {
//                    curricularYears = degreeOccurenceMap.get(degree);
//                } else {
//                    curricularYears = new TreeSet<Integer>();
//                    degreeOccurenceMap.put(degree, curricularYears);
//                }
//
//                curricularYears.add(degreeModuleScope.getCurricularYear());
//            }
//        }
        return degreeOccurenceMap;
    }

}