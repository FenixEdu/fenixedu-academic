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
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesFlunkedSeason;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ReingressionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.enrolmentPeriods.EnrolmentPeriodType;

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

        } else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_CURRICULAR_COURSES.equals(enrolmentPeriodType)) {

            new EnrolmentPeriodInCurricularCourses(degreeCurricularPlan, executionSemester, startDate, endDate);

        } else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_SPECIAL_SEASON_EVALUATIONS.equals(enrolmentPeriodType)) {

            new EnrolmentPeriodInSpecialSeasonEvaluations(degreeCurricularPlan, executionSemester, startDate, endDate);

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