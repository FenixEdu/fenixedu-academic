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
package org.fenixedu.academic.service.services.teacher.tutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.dto.teacher.tutor.PerformanceGridTableDTO;
import org.fenixedu.academic.dto.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine;
import org.fenixedu.academic.dto.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine.PerformanceGridLineYearGroup;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Tutorship;
import org.fenixedu.academic.domain.curriculum.EnrollmentState;
import pt.ist.fenixframework.Atomic;

public class CreatePerformanceGridTable {

    protected PerformanceGridTableDTO run(List<Tutorship> tutorships, ExecutionYear studentEntryYear, ExecutionYear monitoringYear) {
        PerformanceGridTableDTO performanceGrid = new PerformanceGridTableDTO(studentEntryYear, monitoringYear);

        for (Tutorship tutorship : tutorships) {
            PerformanceGridLine newLine = performanceGrid.getNewPerformanceGridLine(tutorship);

            calculateEnrolmentsWithLastExecutionPeriod(newLine, tutorship, monitoringYear);
            calculateApprovedRatioBySemester(newLine, monitoringYear);
            calculateAritmeticAverage(newLine);
            calculateStudentPerformanceInfo(newLine);

            performanceGrid.addPerformanceGridLine(newLine);
        }

        return performanceGrid;
    }

    private void calculateEnrolmentsWithLastExecutionPeriod(PerformanceGridLine newLine, Tutorship tutorship,
            ExecutionYear monitoringYear) {

        List<Enrolment> enrolments =
                tutorship.getStudentCurricularPlan().getEnrolmentsWithExecutionYearBeforeOrEqualTo(monitoringYear);
        Map<CurricularCourse, List<Enrolment>> enrolmentsByCurricularCourse = getEnrolmentsByCurricularCourse(enrolments);

        List<Enrolment> enrolmentsWithLastExecutionPeriod = new ArrayList<Enrolment>();
        for (CurricularCourse curricular : enrolmentsByCurricularCourse.keySet()) {
            Enrolment enrolment = Enrolment.getEnrolmentWithLastExecutionPeriod(enrolmentsByCurricularCourse.get(curricular));
            enrolmentsWithLastExecutionPeriod.add(enrolment);
        }

        newLine.setEnrolmentsWithLastExecutionPeriod(enrolmentsWithLastExecutionPeriod);
    }

    private Map<CurricularCourse, List<Enrolment>> getEnrolmentsByCurricularCourse(List<Enrolment> enrolments) {
        Map<CurricularCourse, List<Enrolment>> enrolmentsByCurricularCourse = new HashMap<CurricularCourse, List<Enrolment>>();

        for (Enrolment enrolment : enrolments) {
            CurricularCourse curricular = (CurricularCourse) enrolment.getDegreeModule();
            if (!enrolmentsByCurricularCourse.containsKey(curricular)) {
                List<Enrolment> thisCurricularEnrolments = new ArrayList<Enrolment>();
                thisCurricularEnrolments.add(enrolment);
                enrolmentsByCurricularCourse.put(curricular, thisCurricularEnrolments);
            } else {
                enrolmentsByCurricularCourse.get(curricular).add(enrolment);
            }
        }
        return enrolmentsByCurricularCourse;
    }

    private void calculateApprovedRatioBySemester(PerformanceGridLine newLine, ExecutionYear monitoringYear) {
        int concludedFirstSemester = 0, firstSemesterEnrolments = 0, concludedSecondSemester = 0, secondSemesterEnrolments = 0;

        List<Enrolment> enrolments = getEnrolmentsWithGivenExecutionYear(newLine, monitoringYear);

        for (Enrolment enrolment : enrolments) {
            if (!enrolment.getCurricularCourse().isAnual()) {
                if (enrolment.getCurricularCourse().getOldestDegreeModuleScope().isFirstSemester()) {
                    firstSemesterEnrolments++;
                    if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                        concludedFirstSemester++;
                    }
                } else {
                    secondSemesterEnrolments++;
                    if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                        concludedSecondSemester++;
                    }
                }
            }
        }

        Integer ratioBySemester[] = new Integer[2];
        ratioBySemester[0] = Math.round(((float) concludedFirstSemester / (float) firstSemesterEnrolments) * 100);
        ratioBySemester[1] = Math.round(((float) concludedSecondSemester / (float) secondSemesterEnrolments) * 100);

        newLine.setApprovedRatioBySemester(Arrays.asList(ratioBySemester));
        newLine.setApprovedEnrolmentsNumber(new Integer(concludedFirstSemester + concludedSecondSemester));
    }

    private List<Enrolment> getEnrolmentsWithGivenExecutionYear(PerformanceGridLine newLine, ExecutionYear executionYear) {
        List<Enrolment> enrolments = new ArrayList<Enrolment>();
        for (Enrolment enrolment : newLine.getEnrolmentsWithLastExecutionPeriod()) {
            if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                enrolments.add(enrolment);
            }
        }
        return enrolments;
    }

    private void calculateAritmeticAverage(PerformanceGridLine newLine) {
        int concludedCurricularCourses = 0, gradesAcumulator = 0;
        double aritmeticAverage = 0;

        for (Enrolment enrolment : newLine.getEnrolmentsWithLastExecutionPeriod()) {
            if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED) && enrolment.getFinalGrade() != null) {
                concludedCurricularCourses++;
                gradesAcumulator += enrolment.getFinalGrade();
            }
        }
        aritmeticAverage = ((double) gradesAcumulator / (double) concludedCurricularCourses);

        newLine.setAritmeticAverage((int) (aritmeticAverage * 100) / 100.0);
    }

    public void calculateStudentPerformanceInfo(PerformanceGridLine newLine) {
        int max_years = (int) newLine.getRegistration().getDegreeType().getAcademicPeriod().getWeight();

        PerformanceGridLineYearGroup[] studentPerformanceByYearArray = new PerformanceGridLineYearGroup[max_years];

        for (int i = 0; i < max_years; i++) {
            studentPerformanceByYearArray[i] = newLine.getNewPerformanceGridLineYearGroup();
        }

        for (Enrolment enrolment : newLine.getEnrolmentsWithLastExecutionPeriod()) {
            CurricularCourse curricular = enrolment.getCurricularCourse();
            DegreeModuleScope scope = curricular.getOldestDegreeModuleScope();
            PerformanceGridLineYearGroup bean = studentPerformanceByYearArray[Math.min(scope.getCurricularYear(), max_years) - 1];
            bean.addEnrolmentToSemester(scope, curricular, enrolment);
        }

        newLine.setStudentPerformanceByYear(Arrays.asList(studentPerformanceByYearArray));
    }

    // Service Invokers migrated from Berserk

    private static final CreatePerformanceGridTable serviceInstance = new CreatePerformanceGridTable();

    @Atomic
    public static PerformanceGridTableDTO runCreatePerformanceGridTable(List<Tutorship> tutorships,
            ExecutionYear studentEntryYear, ExecutionYear monitoringYear) throws NotAuthorizedException {
        return serviceInstance.run(tutorships, studentEntryYear, monitoringYear);
    }

}