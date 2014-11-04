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
/*
 * 
 *  
 */
package org.fenixedu.academic.service.services.department;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.curriculum.EnrollmentState;
import org.fenixedu.academic.domain.curriculum.GradeFactory;
import org.fenixedu.academic.domain.curriculum.IGrade;
import org.fenixedu.academic.dto.department.CourseStatisticsDTO;

/**
 * @author pcma
 */

public abstract class ComputeCourseStatistics {
    public ComputeCourseStatistics() {
        super();
    }

    protected void createCourseStatistics(CourseStatisticsDTO courseStatistics, List<Enrolment> enrolments) {
        int firstEnrolledCount = 0;
        int firstApprovedCount = 0;
        List<IGrade> firstApprovedGrades = new ArrayList<IGrade>();

        int restEnrolledCount = 0;
        int restApprovedCount = 0;
        List<IGrade> restApprovedGrades = new ArrayList<IGrade>();

        int totalEnrolledCount = 0;
        int totalApprovedCount = 0;
        List<IGrade> totalApprovedGrades = new ArrayList<IGrade>();

        for (Enrolment enrolment : enrolments) {

            totalEnrolledCount++;

            EnrolmentEvaluation evaluation = getBestEnrollmentEvaluation(enrolment);
            if (enrolment.isFirstTime()) {
                firstEnrolledCount++;

                if (evaluation != null && evaluation.getEnrollmentStateByGrade() == EnrollmentState.APROVED) {
                    firstApprovedCount++;
                    IGrade grade = evaluation.getGradeWrapper();
                    firstApprovedGrades.add(grade);
                    totalApprovedGrades.add(grade);
                    totalApprovedCount++;
                }
            } else {
                restEnrolledCount++;
                if (evaluation != null && evaluation.getEnrollmentStateByGrade() == EnrollmentState.APROVED) {
                    restApprovedCount++;
                    IGrade grade = evaluation.getGradeWrapper();
                    restApprovedGrades.add(grade);
                    totalApprovedGrades.add(grade);
                    totalApprovedCount++;
                }
            }
        }

        IGrade firstApprovedAverage = calculateApprovedAverage(firstApprovedGrades);
        IGrade restApprovedAverage = calculateApprovedAverage(restApprovedGrades);
        IGrade totalApprovedAverage = calculateApprovedAverage(totalApprovedGrades);

        courseStatistics.setFirstEnrolledCount(firstEnrolledCount);
        courseStatistics.setFirstApprovedCount(firstApprovedCount);
        courseStatistics.setFirstApprovedAverage(firstApprovedAverage);

        courseStatistics.setRestEnrolledCount(restEnrolledCount);
        courseStatistics.setRestApprovedCount(restApprovedCount);
        courseStatistics.setRestApprovedAverage(restApprovedAverage);

        courseStatistics.setTotalEnrolledCount(totalEnrolledCount);
        courseStatistics.setTotalApprovedCount(totalApprovedCount);
        courseStatistics.setTotalApprovedAverage(totalApprovedAverage);

    }

    protected GradeFactory gradeFactory = GradeFactory.getInstance();

    private IGrade calculateApprovedAverage(List<IGrade> approvedGrades) {
        if (approvedGrades.size() == 0) {
            return null;
        }

        // TODO: should query by chain of responsability to know how to compute
        // this should be changed
        return approvedGrades.iterator().next().getGradeType().average(approvedGrades);
    }

    protected EnrolmentEvaluation getBestEnrollmentEvaluation(Enrolment enrollment) {
        EnrolmentEvaluation best = null;

        for (EnrolmentEvaluation evaluation : enrollment.getEvaluationsSet()) {
            if (best == null || best.getGradeWrapper().compareTo(evaluation.getGradeWrapper()) > 0) {
                best = evaluation;
            }
        }
        return best;
    }
}
