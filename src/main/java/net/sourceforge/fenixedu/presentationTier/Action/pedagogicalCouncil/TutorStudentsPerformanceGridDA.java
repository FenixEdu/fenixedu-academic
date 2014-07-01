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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.NumberBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine.PerformanceGridLineYearGroup;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.TutorshipApp;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor.ViewStudentsPerformanceGridDispatchAction;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

@StrutsFunctionality(app = TutorshipApp.class, path = "performance-grids", titleKey = "label.attends.shifts.tutorialperformance",
        bundle = "ApplicationResources")
@Mapping(path = "/tutorStudentsPerformanceGrid", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewStudentsPerformanceGrid", path = "/pedagogicalCouncil/tutorship/showStudentsPerformanceGrid.jsp") })
public class TutorStudentsPerformanceGridDA extends ViewStudentsPerformanceGridDispatchAction {

    @EntryPoint
    public ActionForward prepareTutorSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("tutorateBean", new TutorSearchBean());
        return mapping.findForward("viewStudentsPerformanceGrid");
    }

    public ActionForward viewStudentsPerformanceGrid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TutorSearchBean bean = (TutorSearchBean) getRenderedObject("tutorateBean");
        request.setAttribute("tutorateBean", bean);
        if (bean.getTeacher() != null) {
            Person person = bean.getTeacher().getPerson();
            generateStudentsPerformanceBean(request, person);
            request.setAttribute("tutor", person);
            RenderUtils.invalidateViewState();
            return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
        } else {
            RenderUtils.invalidateViewState();
            return mapping.findForward("viewStudentsPerformanceGrid");
        }
    }

    public ActionForward exportXls(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String tutorId = request.getParameter("tutorOID");
        Person person = (Person) FenixFramework.getDomainObject(tutorId);
        NumberBean numberBean = new NumberBean();
        numberBean.setId(person.getUsername());
        request.setAttribute("tutorateBean", numberBean);
        generateStudentsPerformanceBeanFromRequest(request, person);
        prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
        final PerformanceGridTableDTO performanceGridTable =
                (PerformanceGridTableDTO) request.getAttribute("performanceGridTable");
        SheetData<PerformanceGridLine> builder =
                new SheetData<PerformanceGridLine>(performanceGridTable.getPerformanceGridTableLines()) {
                    @Override
                    protected void makeLine(PerformanceGridLine item) {
                        Registration registration = item.getRegistration();
                        addCell(BundleUtil.getString(Bundle.APPLICATION, "label.studentNumber"), registration.getNumber());
                        addCell(BundleUtil.getString(Bundle.APPLICATION, "label.name"), registration.getPerson().getName());
                        addCell(BundleUtil.getString(Bundle.APPLICATION, "label.entryPhase"),
                                registration.getEntryPhase() != null ? registration.getEntryPhase().ordinal() + 1 : null);
                        addCell(BundleUtil.getString(Bundle.APPLICATION, "label.entryGrade"), registration.getEntryGrade());
                        addCell(BundleUtil.getString(Bundle.APPLICATION, "label.aritmeticAverage"), item.getAritmeticAverage());
                        addCell(BundleUtil.getString(Bundle.APPLICATION, "label.approvedRatio"), (short) 2,
                                BundleUtil.getString(Bundle.APPLICATION, "label.first.semester.short"), (short) 1,
                                item.getApprovedRatioFirstSemester() + " %", (short) 1);
                        addCell(BundleUtil.getString(Bundle.APPLICATION, "label.second.semester.short"),
                                item.getApprovedRatioSecondSemester() + " %");
                        ExecutionYear monitoringYear = performanceGridTable.getMonitoringYear();
                        int yearCount = item.getStudentPerformanceByYear().size();
                        for (int year = 0; year < yearCount; year++) {
                            addCell(MessageFormat.format(
                                    BundleUtil.getString(Bundle.APPLICATION, "label.performanceGrid.year.tutorated"), year + 1,
                                    monitoringYear.getName()), (short) 2, BundleUtil.getString(Bundle.APPLICATION,
                                    "label.first.semester.short"), (short) 1,
                                    addSemesterCell(item, monitoringYear, year, 1, true), (short) 1);
                            addCell(BundleUtil.getString(Bundle.APPLICATION, "label.second.semester.short"),
                                    addSemesterCell(item, monitoringYear, year, 2, true));
                            addCell(MessageFormat
                                    .format(BundleUtil.getString(Bundle.APPLICATION, "label.performanceGrid.year.notTutorated"),
                                            year + 1),
                                    (short) 2, BundleUtil.getString(Bundle.APPLICATION, "label.first.semester.short"), (short) 1,
                                    addSemesterCell(item, monitoringYear, year, 1, false), (short) 1);
                            addCell(BundleUtil.getString(Bundle.APPLICATION, "label.second.semester.short"),
                                    addSemesterCell(item, monitoringYear, year, 2, false));
                        }
                    }

                    private Object addSemesterCell(PerformanceGridLine item, ExecutionYear monitoringYear, int year, int sem,
                            boolean tutorated) {
                        PerformanceGridLineYearGroup yearEnrols = item.getStudentPerformanceByYear().get(year);
                        List<Enrolment> enrols;
                        if (sem == 1) {
                            enrols = yearEnrols.getFirstSemesterEnrolments();
                        } else {
                            enrols = yearEnrols.getSecondSemesterEnrolments();
                        }

                        int notApproved = 0;
                        int approved = 0;
                        int notEvaluated = 0;
                        for (Object object : enrols) {
                            Enrolment enrolment = (Enrolment) object;
                            if (tutorated && !enrolment.getExecutionYear().equals(monitoringYear)) {
                                continue;
                            }
                            if (!tutorated && enrolment.getExecutionYear().equals(monitoringYear)) {
                                continue;
                            }
                            if (enrolment.getEnrollmentState().equals(EnrollmentState.NOT_APROVED)) {
                                notApproved++;
                            } else if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                                approved++;
                            } else if (enrolment.getEnrollmentState().equals(EnrollmentState.ENROLLED)
                                    || enrolment.getEnrollmentState().equals(EnrollmentState.NOT_EVALUATED)) {
                                notEvaluated++;
                            }
                        }
                        return notEvaluated + "/" + approved + "/" + notApproved;
                    }
                };
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=" + person.getUsername() + "-students-performance.xls");
        new SpreadsheetBuilder().addSheet(person.getUsername() + "-students-performance.xls", builder).build(
                WorkbookExportFormat.EXCEL, response.getOutputStream());
        response.flushBuffer();
        return null;
    }

    @Override
    public ActionForward prepareAllStudentsStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String tutorId = request.getParameter("tutorOID");
        Person person = (Person) FenixFramework.getDomainObject(tutorId);
        NumberBean numberBean = new NumberBean();
        numberBean.setId(person.getUsername());
        request.setAttribute("tutorateBean", numberBean);
        StudentsPerformanceInfoBean bean = generateStudentsPerformanceBeanFromRequest(request, person);
        if (!bean.getTutorships().isEmpty()) {

            List<DegreeCurricularPlan> plans =
                    new ArrayList<DegreeCurricularPlan>(bean.getDegree().getDegreeCurricularPlansSet());
            Collections.sort(plans,
                    DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

            List<StudentCurricularPlan> students =
                    plans.iterator().next().getStudentsCurricularPlanGivenEntryYear(bean.getStudentsEntryYear());

            putAllStudentsStatisticsInTheRequest(request, students, bean.getCurrentMonitoringYear());

            request.setAttribute("entryYear", bean.getStudentsEntryYear());
            request.setAttribute("totalEntryStudents", students.size());
        }
        return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
    }
}
