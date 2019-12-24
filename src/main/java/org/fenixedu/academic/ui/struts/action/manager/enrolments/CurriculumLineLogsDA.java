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
package org.fenixedu.academic.ui.struts.action.manager.enrolments;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.log.CurriculumLineLog;
import org.fenixedu.academic.domain.log.EnrolmentLog;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.curriculumLineLog.SearchCurriculumLineLog;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerStudentsApp;
import org.fenixedu.academic.util.EnrolmentAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.joda.time.Interval;

@StrutsFunctionality(app = ManagerStudentsApp.class, path = "curriculum-logs", titleKey = "title.curriculum.line.logs")
@Mapping(path = "/curriculumLineLogs", module = "manager")
@Forwards({ @Forward(name = "searchCurriculumLineLogs", path = "/manager/viewCurriculumLineLogs.jsp"),
        @Forward(name = "viewCurriculumLineLogStatistics", path = "/manager/viewCurriculumLineLogStatistics.jsp") })
public class CurriculumLineLogsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareViewCurriculumLineLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("bean", new SearchCurriculumLineLog());
        return mapping.findForward("searchCurriculumLineLogs");
    }

    public ActionForward viewCurriculumLineLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final SearchCurriculumLineLog searchCurriculumLineLog = getRenderedObject();
        request.setAttribute("bean", searchCurriculumLineLog);
        Student student = Student.readStudentByNumber(searchCurriculumLineLog.getStudentNumber());

        if (student == null) {
            addActionMessage(request, "exception.student.does.not.exist");
            return mapping.findForward("searchCurriculumLineLogs");
        }

        request.setAttribute("curriculumLineLogs", getCurriculumLineLogs(student, searchCurriculumLineLog.getExecutionPeriod()));
        return mapping.findForward("searchCurriculumLineLogs");
    }

    private Collection<CurriculumLineLog> getCurriculumLineLogs(final Student student,
            final ExecutionInterval executionSemester) {
        final Collection<CurriculumLineLog> res = new HashSet<>();
        for (final Registration registration : student.getRegistrationsSet()) {
            res.addAll(registration.getCurriculumLineLogs(executionSemester));
        }
        return res;
    }

    public static class CurriculumLineLogStatisticsCalculator {

        private static final int INTERVAL_SIZE_IN_MILLIS = 300000;

        final Interval enrolmentPeriod;
        final int[] enrolments;
        final int[] unenrolments;

        public CurriculumLineLogStatisticsCalculator(final ExecutionInterval executionSemester) {
            enrolmentPeriod = findEnrolmentPeriod(executionSemester);
            final long start = enrolmentPeriod.getStart().getMillis();
            final long durationMillis = enrolmentPeriod.toDurationMillis();
            final int numberOfIntervals = (int) (durationMillis / INTERVAL_SIZE_IN_MILLIS) + 1;

            enrolments = new int[numberOfIntervals];
            unenrolments = new int[numberOfIntervals];

            for (final CurriculumLineLog curriculumLineLog : executionSemester.getCurriculumLineLogsSet()) {
                if (curriculumLineLog instanceof EnrolmentLog) {
                    final DateTime dateTime = curriculumLineLog.getDateDateTime();
                    if (enrolmentPeriod.contains(dateTime)) {
                        final long offset = dateTime.getMillis() - start;
                        final int i = (int) offset / INTERVAL_SIZE_IN_MILLIS;

                        if (curriculumLineLog.getAction() == EnrolmentAction.ENROL) {
                            enrolments[i]++;
                        } else {
                            unenrolments[i]++;
                        }
                    }
                }
            }
        }

        public Interval getEnrolmentPeriod() {
            return enrolmentPeriod;
        }

        public int[] getEnrolments() {
            return enrolments;
        }

        public int[] getUnenrolments() {
            return unenrolments;
        }

        private Interval findEnrolmentPeriod(final ExecutionInterval executionSemester) {
            return null;
        }

    }

    public ActionForward viewCurriculumLineLogStatistics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionInterval executionSemester = getDomainObject(request, "executionSemesterId");

        if (executionSemester != null) {
            request.setAttribute("executionSemester", executionSemester);

            final CurriculumLineLogStatisticsCalculator curriculumLineLogStatisticsCalculator =
                    new CurriculumLineLogStatisticsCalculator(executionSemester);
            request.setAttribute("curriculumLineLogStatisticsCalculator", curriculumLineLogStatisticsCalculator);
            return mapping.findForward("viewCurriculumLineLogStatistics");
        }

        return prepareViewCurriculumLineLogs(mapping, form, request, response);
    }

}