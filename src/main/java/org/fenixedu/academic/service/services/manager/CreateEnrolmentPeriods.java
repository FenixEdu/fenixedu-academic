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
package org.fenixedu.academic.service.services.manager;

import java.util.Date;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EnrolmentPeriodInClasses;
import org.fenixedu.academic.domain.EnrolmentPeriodInClassesMobility;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCourses;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCoursesFlunkedSeason;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import org.fenixedu.academic.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import org.fenixedu.academic.domain.EnrolmentPeriodInExtraordinarySeasonEvaluations;
import org.fenixedu.academic.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ReingressionPeriod;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.enrolmentPeriods.EnrolmentPeriodType;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CreateEnrolmentPeriods {

    @Atomic
    public static void run(ExecutionSemester executionSemester, DegreeType degreeType, EnrolmentPeriodType enrolmentPeriodType,
            DateTime start, DateTime end, List<DegreeCurricularPlan> dcps) {
        final Date startDate = start.toDate();
        final Date endDate = end.toDate();
        /*
         * Allow pre-bolonha degrees to create reingression periods
         */
        if (!degreeType.isBolonhaType() && enrolmentPeriodType.isReingressionPeriod()) {
            createReingressionPeriodsForPreBolonhaDegrees(executionSemester, degreeType, startDate, endDate, dcps);
        } else if (degreeType.isEmpty()) {
            createEnrolmentPeriodsForEmptyDegree(executionSemester, enrolmentPeriodType, startDate, endDate);
        } else {
            createEnrolmentPeriodsForBolonhaDegrees(executionSemester, degreeType, enrolmentPeriodType, startDate, endDate, dcps);
        }
    }

    private static void createReingressionPeriodsForPreBolonhaDegrees(final ExecutionSemester executionSemester,
            final DegreeType degreeType, final Date startDate, final Date endDate, final List<DegreeCurricularPlan> dcpList) {
        for (final DegreeCurricularPlan degreeCurricularPlan : dcpList) {
            new ReingressionPeriod(degreeCurricularPlan, executionSemester, startDate, endDate);
        }
    }

    private static void createEnrolmentPeriodsForEmptyDegree(ExecutionSemester executionSemester,
            EnrolmentPeriodType enrolmentPeriodType, Date startDate, Date endDate) {
        createPeriod(enrolmentPeriodType, startDate, endDate, executionSemester,
                DegreeCurricularPlan.readEmptyDegreeCurricularPlan());
    }

    private static void createEnrolmentPeriodsForBolonhaDegrees(final ExecutionSemester executionSemester,
            final DegreeType degreeType, final EnrolmentPeriodType enrolmentPeriodType, final Date startDate, final Date endDate,
            final List<DegreeCurricularPlan> dcpList) {

        for (final DegreeCurricularPlan degreeCurricularPlan : dcpList) {

            if (degreeType == null || degreeType == degreeCurricularPlan.getDegree().getDegreeType()) {
                createPeriod(enrolmentPeriodType, startDate, endDate, executionSemester, degreeCurricularPlan);
            }
        }
    }

    private static void createPeriod(EnrolmentPeriodType enrolmentPeriodType, final Date startDate, final Date endDate,
            final ExecutionSemester executionSemester, final DegreeCurricularPlan degreeCurricularPlan) {

        if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_CLASSES.equals(enrolmentPeriodType)) {

            new EnrolmentPeriodInClasses(degreeCurricularPlan, executionSemester, startDate, endDate);

        } else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_CLASSES_MOBILITY.equals(enrolmentPeriodType)) {

            new EnrolmentPeriodInClassesMobility(degreeCurricularPlan, executionSemester, startDate, endDate);

        } else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_CURRICULAR_COURSES.equals(enrolmentPeriodType)) {

            new EnrolmentPeriodInCurricularCourses(degreeCurricularPlan, executionSemester, startDate, endDate);

        } else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_SPECIAL_SEASON_EVALUATIONS.equals(enrolmentPeriodType)) {

            new EnrolmentPeriodInSpecialSeasonEvaluations(degreeCurricularPlan, executionSemester, startDate, endDate);

        } else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_EXTRAORDINARY_SEASON_EVALUATIONS.equals(enrolmentPeriodType)) {

            new EnrolmentPeriodInExtraordinarySeasonEvaluations(degreeCurricularPlan, executionSemester, startDate, endDate);

        } else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_CURRICULAR_COURSES_SPECIAL_SEASON.equals(enrolmentPeriodType)) {

            new EnrolmentPeriodInCurricularCoursesSpecialSeason(degreeCurricularPlan, executionSemester, startDate, endDate);

        } else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_CURRICULAR_COURSES_FLUNKED_SEASON.equals(enrolmentPeriodType)) {

            new EnrolmentPeriodInCurricularCoursesFlunkedSeason(degreeCurricularPlan, executionSemester, startDate, endDate);

        } else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_IMPROVEMENT_OF_APPROVED_ENROLMENT.equals(enrolmentPeriodType)) {

            new EnrolmentPeriodInImprovementOfApprovedEnrolment(degreeCurricularPlan, executionSemester, startDate, endDate);

        } else if (enrolmentPeriodType.isReingressionPeriod()) {

            new ReingressionPeriod(degreeCurricularPlan, executionSemester, startDate, endDate);

        } else {
            throw new Error("error.invalid.enrolment.period.class.name");
        }
    }
}