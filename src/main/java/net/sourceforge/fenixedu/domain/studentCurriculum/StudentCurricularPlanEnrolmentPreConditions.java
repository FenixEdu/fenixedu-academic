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
package net.sourceforge.fenixedu.domain.studentCurriculum;

import static net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult.createFalse;
import static net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult.createTrue;
import static pt.utl.ist.fenix.tools.util.DateFormatUtil.DEFAULT_DATE_FORMAT;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

public class StudentCurricularPlanEnrolmentPreConditions {

    static public class EnrolmentPreConditionResult {
        private boolean valid = false;
        private String message;
        private String[] args;

        private EnrolmentPreConditionResult valid(final boolean value) {
            this.valid = value;
            return this;
        }

        public boolean isValid() {
            return valid;
        }

        private EnrolmentPreConditionResult message(final String message, final String... args) {
            this.message = message;
            this.args = args;
            return this;
        }

        public String message() {
            return this.message;
        }

        public String[] args() {
            return this.args;
        }

        static public EnrolmentPreConditionResult createTrue() {
            return new EnrolmentPreConditionResult().valid(true);
        }

        static public EnrolmentPreConditionResult createFalse(final String message, final String... args) {
            return new EnrolmentPreConditionResult().valid(false).message(message, args);
        }
    }

    /*
     * Change? If next period is not defined then we should use last? Or previous of given semester?
     */
    static private EnrolmentPreConditionResult outOfPeriodResult(final String periodType, final EnrolmentPeriod nextPeriod) {
        if (nextPeriod != null) {
            return createFalse("message.out.curricular.course.enrolment.period." + periodType, nextPeriod.getStartDateDateTime()
                    .toString(DEFAULT_DATE_FORMAT), nextPeriod.getEndDateDateTime().toString(DEFAULT_DATE_FORMAT));
        } else {
            return createFalse("message.out.curricular.course.enrolment.period." + periodType + ".noDates");
        }
    }

    static public EnrolmentPreConditionResult checkPreConditionsToEnrol(StudentCurricularPlan scp, ExecutionSemester semester) {

        final EnrolmentPreConditionResult result = checkEnrolmentPeriods(scp, semester);
        if (!result.isValid()) {
            return result;
        }

        return checkDebts(scp);
    }

    /**
     * 
     * Check if student has any debts that prevent him to enrol in curricular
     * courses
     * 
     * @param scp
     * @return EnrolmentPreConditionResult
     */
    static EnrolmentPreConditionResult checkDebts(StudentCurricularPlan scp) {

        if (scp.getRegistration().getStudent().isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt()) {
            return createFalse("error.StudentCurricularPlan.cannot.enrol.with.debts.for.previous.execution.years");
        }

        if (scp.getPerson().hasAnyResidencePaymentsInDebtForPreviousYear()) {
            return createFalse("error.StudentCurricularPlan.cannot.enrol.with.residence.debts");
        }

        return createTrue();
    }

    static private boolean hasSpecialSeason(final StudentCurricularPlan scp, final ExecutionSemester semester) {

        if (scp.hasSpecialSeasonFor(semester)) {
            return true;
        }

        final Registration registration = scp.getRegistration();
        return registration.hasSourceRegistration()
                && registration.getSourceRegistration().getLastStudentCurricularPlan().hasSpecialSeasonFor(semester);
    }

    /**
     * Check student enrolment periods
     * 
     * @param scp
     * @param semester
     * @return EnrolmentPreConditionResult
     */
    static EnrolmentPreConditionResult checkEnrolmentPeriods(StudentCurricularPlan scp, ExecutionSemester semester) {

        if (semester.isFirstOfYear() && hasSpecialSeason(scp, semester)) {

            if (scp.getDegreeCurricularPlan().getActualEnrolmentPeriodInCurricularCoursesSpecialSeason() == null) {
                return outOfPeriodResult("specialSeason", scp.getDegreeCurricularPlan()
                        .getNextEnrolmentPeriodInCurricularCoursesSpecialSeason());
            }

        } else if (semester.isFirstOfYear() && hasPrescribed(scp, semester)) {

            if (scp.getDegreeCurricularPlan().getActualEnrolmentPeriodInCurricularCoursesFlunkedSeason() == null) {
                return outOfPeriodResult("flunked", scp.getDegreeCurricularPlan()
                        .getNextEnrolmentPeriodInCurricularCoursesFlunkedSeason());
            }

        } else if (!scp.getDegreeCurricularPlan().hasActualEnrolmentPeriodInCurricularCourses()) {
            return outOfPeriodResult("normal", scp.getDegreeCurricularPlan().getNextEnrolmentPeriod());
        }

        return createTrue();
    }

    static EnrolmentPreConditionResult checkEnrolmentPeriodsForSpecialSeason(StudentCurricularPlan scp, ExecutionSemester semester) {
        if (!scp.getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(semester)) {
            return outOfPeriodResult("specialSeason", scp.getDegreeCurricularPlan()
                    .getNextEnrolmentPeriodInCurricularCoursesSpecialSeason());
        }
        return createTrue();
    }

    /*
     * Student must have flunked state and then registered (in same year), otherwise is not considered to be prescribed
     */
    private static boolean hasPrescribed(StudentCurricularPlan scp, ExecutionSemester semester) {
        for (RegistrationState state : scp.getRegistration().getRegistrationStates(semester.getExecutionYear())) {
            if (state.getExecutionYear().equals(semester.getExecutionYear())
                    && RegistrationStateType.FLUNKED.equals(state.getStateType())) {
                return scp.getRegistration().hasRegisteredActiveState();
            }
        }

        return false;
    }

}
