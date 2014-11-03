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
package net.sourceforge.fenixedu.presentationTier.Action.commons.tutorship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tutor.CreatePerformanceGridTable;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.TutorStatisticsBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public abstract class StudentsPerformanceGridDispatchAction extends FenixDispatchAction {

    protected PerformanceGridTableDTO createPerformanceGridTable(HttpServletRequest request, List<Tutorship> tutors,
            ExecutionYear entryYear, ExecutionYear monitoringYear) {

        PerformanceGridTableDTO performanceGridTable = null;
        try {
            performanceGridTable = CreatePerformanceGridTable.runCreatePerformanceGridTable(tutors, entryYear, monitoringYear);
        } catch (FenixServiceException ex) {
            addActionMessage(request, ex.getMessage(), ex.getArgs());
        }

        return performanceGridTable;
    }

    /*
     * AUXILIARY METHODS
     */
    protected void getStatisticsAndPutInTheRequest(HttpServletRequest request, PerformanceGridTableDTO performanceGridTable) {
        final List<PerformanceGridLine> performanceGridLines = performanceGridTable.getPerformanceGridTableLines();

        Map<Integer, TutorStatisticsBean> statisticsByApprovedEnrolmentsNumber = new HashMap<Integer, TutorStatisticsBean>();
        int maxApprovedEnrolments = 0;

        for (PerformanceGridLine studentGridLine : performanceGridLines) {
            maxApprovedEnrolments = Math.max(maxApprovedEnrolments, studentGridLine.getApprovedEnrolmentsNumber());

            if (statisticsByApprovedEnrolmentsNumber.containsKey(studentGridLine.getApprovedEnrolmentsNumber())) {
                TutorStatisticsBean tutorStatisticsbean =
                        statisticsByApprovedEnrolmentsNumber.get(studentGridLine.getApprovedEnrolmentsNumber());
                tutorStatisticsbean.setStudentsNumber(tutorStatisticsbean.getStudentsNumber() + 1);
            } else {
                TutorStatisticsBean tutorStatisticsBean =
                        new TutorStatisticsBean(1, studentGridLine.getApprovedEnrolmentsNumber(), performanceGridLines.size());
                tutorStatisticsBean.setApprovedEnrolmentsNumber(studentGridLine.getApprovedEnrolmentsNumber());
                statisticsByApprovedEnrolmentsNumber.put(studentGridLine.getApprovedEnrolmentsNumber(), tutorStatisticsBean);
            }
        }

        putStatisticsInTheRequest(request, maxApprovedEnrolments, performanceGridLines.size(),
                statisticsByApprovedEnrolmentsNumber, "tutorStatistics");
    }

    protected void putAllStudentsStatisticsInTheRequest(HttpServletRequest request, List<StudentCurricularPlan> students,
            ExecutionYear currentMonitoringYear) {
        Map<Integer, TutorStatisticsBean> statisticsByApprovedEnrolmentsNumber = new HashMap<Integer, TutorStatisticsBean>();

        int maxApprovedEnrolments = 0;

        for (StudentCurricularPlan scp : students) {
            List<Enrolment> enrolments = scp.getEnrolmentsByExecutionYear(currentMonitoringYear);
            int approvedEnrolments = 0;

            for (Enrolment enrolment : enrolments) {
                if (!enrolment.getCurricularCourse().isAnual() && enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                    approvedEnrolments++;
                }
            }

            maxApprovedEnrolments = Math.max(maxApprovedEnrolments, approvedEnrolments);

            if (statisticsByApprovedEnrolmentsNumber.containsKey(approvedEnrolments)) {
                TutorStatisticsBean tutorStatisticsbean = statisticsByApprovedEnrolmentsNumber.get(approvedEnrolments);
                tutorStatisticsbean.setStudentsNumber(tutorStatisticsbean.getStudentsNumber() + 1);
            } else {
                TutorStatisticsBean tutorStatisticsBean = new TutorStatisticsBean(1, approvedEnrolments, students.size());
                tutorStatisticsBean.setApprovedEnrolmentsNumber(approvedEnrolments);
                statisticsByApprovedEnrolmentsNumber.put(approvedEnrolments, tutorStatisticsBean);
            }
        }

        putStatisticsInTheRequest(request, maxApprovedEnrolments, students.size(), statisticsByApprovedEnrolmentsNumber,
                "allStudentsStatistics");
    }

    private void putStatisticsInTheRequest(HttpServletRequest request, Integer maxApprovedEnrolments, Integer studentsSize,
            Map<Integer, TutorStatisticsBean> statisticsByApprovedEnrolmentsNumber, String attributeId) {

        if (studentsSize != 0) {
            List<TutorStatisticsBean> statistics = new ArrayList<TutorStatisticsBean>();
            for (int i = 0; i <= maxApprovedEnrolments; i++) {
                if (statisticsByApprovedEnrolmentsNumber.containsKey(i)) {
                    statistics.add(statisticsByApprovedEnrolmentsNumber.get(i));
                } else {
                    statistics.add(new TutorStatisticsBean(0, i, studentsSize));
                }
            }

            Collections.sort(statistics);
            request.setAttribute(attributeId, statistics);
        }
    }
}
