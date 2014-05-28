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
/*
 * Created on 2004/04/21
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 */
public class CheckCandidacyConditionsForFinalDegreeWork {

    @Atomic
    public static Boolean run(User userView, final ExecutionDegree executionDegree) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        Scheduleing scheduleing = executionDegree.getScheduling();

        if (scheduleing == null || scheduleing.getStartOfCandidacyPeriod() == null
                || scheduleing.getEndOfCandidacyPeriod() == null) {
            throw new CandidacyPeriodNotDefinedException();
        }

        if (!isStudentOfScheduling(userView, scheduleing)) {
            throw new CandidacyInOtherExecutionDegreesNotAllowed();
        }

        Calendar now = Calendar.getInstance();
        if (scheduleing.getStartOfCandidacyPeriod().after(now.getTime())
                || scheduleing.getEndOfCandidacyPeriod().before(now.getTime())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String start = simpleDateFormat.format(new Date(scheduleing.getStartOfCandidacyPeriod().getTime()));
            String end = simpleDateFormat.format(new Date(scheduleing.getEndOfCandidacyPeriod().getTime()));
            throw new OutOfCandidacyPeriodException(start + " - " + end);
        }

        Registration registration = findStudent(userView.getPerson());

        if (registration == null) {
            throw new NoDegreeStudentCurricularPlanFoundException();
        }

        // if (scheduleing.getMinimumNumberOfCompletedCourses() == null) {
        // throw new NumberOfNecessaryCompletedCoursesNotSpecifiedException();
        // }
        if (scheduleing.getMinimumCompletedCreditsFirstCycle() == null) {
            throw new NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException();
        }
        if (scheduleing.getMinimumCompletedCreditsSecondCycle() == null) {
            throw new NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException();
        }
        if (scheduleing.getAllowCandaciesOnlyForStudentsWithADissertationEnrolment() != null
                && scheduleing.getAllowCandaciesOnlyForStudentsWithADissertationEnrolment().booleanValue()
                && !registration.hasDissertationEnrolment(executionDegree)) {
            throw new EnrolmentInDissertationIsRequired();
        }
        final Integer maximumCurricularYearToCountCompletedCourses =
                scheduleing.getMaximumCurricularYearToCountCompletedCourses();
        final Integer minimumCompletedCurricularYear = scheduleing.getMinimumCompletedCurricularYear();
        // final Integer minimumNumberOfCompletedCourses =
        // scheduleing.getMinimumNumberOfCompletedCourses();
        final Integer minimumCompletedCreditsFirstCycle = scheduleing.getMinimumCompletedCreditsFirstCycle();
        final Integer minimumCompletedCreditsSecondCycle = scheduleing.getMinimumCompletedCreditsSecondCycle();

        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        final Collection<DegreeModuleScope> degreesActiveCurricularCourseScopes = degreeCurricularPlan.getDegreeModuleScopes();
        final StringBuilder notCompletedCurricularCourses = new StringBuilder();
        final Set<CurricularCourse> notCompletedCurricularCoursesForMinimumCurricularYear = new HashSet<CurricularCourse>();
        final Set<CurricularCourse> completedCurricularCourses = new HashSet<CurricularCourse>();
        // int numberCompletedCurricularCourses = 0;
        for (final DegreeModuleScope degreeModuleScope : degreesActiveCurricularCourseScopes) {
            final CurricularCourse curricularCourse = degreeModuleScope.getCurricularCourse();
            final boolean isCurricularCourseApproved =
                    (studentCurricularPlan.isBoxStructure() && studentCurricularPlan.getRoot().isApproved(curricularCourse))
                            || studentCurricularPlan.isCurricularCourseApproved(curricularCourse);

            final Integer curricularYear = degreeModuleScope.getCurricularYear();

            if (minimumCompletedCurricularYear != null && curricularYear <= minimumCompletedCurricularYear) {
                if (!isCurricularCourseApproved) {
                    notCompletedCurricularCoursesForMinimumCurricularYear.add(curricularCourse);
                }
            }

            if (maximumCurricularYearToCountCompletedCourses == null
                    || curricularYear <= maximumCurricularYearToCountCompletedCourses) {
                if (isCurricularCourseApproved) {
                    completedCurricularCourses.add(degreeModuleScope.getCurricularCourse());
                    // numberCompletedCurricularCourses++;
                } else {
                    if (notCompletedCurricularCourses.length() > 0) {
                        notCompletedCurricularCourses.append(", ");
                    }
                    notCompletedCurricularCourses.append(degreeModuleScope.getCurricularCourse().getName());
                }
            }
        }

        if (!notCompletedCurricularCoursesForMinimumCurricularYear.isEmpty()) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final CurricularCourse curricularCourse : notCompletedCurricularCoursesForMinimumCurricularYear) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(curricularCourse.getName());
            }
            final String[] args = { minimumCompletedCurricularYear.toString(), stringBuilder.toString() };
            throw new NotCompletedCurricularYearException(null, args);
        }

        // int numberCompletedCurricularCourses =
        // completedCurricularCourses.size();
        // if (numberCompletedCurricularCourses <
        // minimumNumberOfCompletedCourses) {
        // final int numberMissingCurricularCourses =
        // minimumNumberOfCompletedCourses - numberCompletedCurricularCourses;
        // final String[] args = {
        // Integer.toString(numberMissingCurricularCourses),
        // notCompletedCurricularCourses.toString()};
        // throw new InsufficientCompletedCoursesException(null, args);
        // }

        if (studentCurricularPlan.getSecondCycle() == null) {
            throw new NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException();
        }

        if (registration.getRegistrationAgreement() != RegistrationAgreement.ERASMUS
                && registration.getRegistrationAgreement() != RegistrationAgreement.AFA
                && registration.getRegistrationAgreement() != RegistrationAgreement.MA
                && registration.getRegistrationAgreement() != RegistrationAgreement.TIME) {

            CycleCurriculumGroup firstCycleCurriculumGroup = studentCurricularPlan.getFirstCycle();
            if (firstCycleCurriculumGroup != null) {
                final Double completedCreditsFirstCycle = firstCycleCurriculumGroup.getAprovedEctsCredits();
                if (minimumCompletedCreditsFirstCycle > completedCreditsFirstCycle) {
                    final String[] args = { completedCreditsFirstCycle.toString(), minimumCompletedCreditsFirstCycle.toString() };
                    throw new InsufficientCompletedCreditsInFirstCycleException(null, args);
                }
            } else {
                final Registration sourceRegistration = registration.getSourceRegistration();
                if (sourceRegistration != null) {
                    final StudentCurricularPlan sourceStudentCurricularPlan = registration.getLastStudentCurricularPlan();
                    if (sourceStudentCurricularPlan != null) {
                        firstCycleCurriculumGroup = sourceStudentCurricularPlan.getFirstCycle();
                        if (firstCycleCurriculumGroup != null) {
                            final Double completedCreditsFirstCycle = firstCycleCurriculumGroup.getAprovedEctsCredits();
                            if (minimumCompletedCreditsFirstCycle > completedCreditsFirstCycle) {
                                final String[] args =
                                        { completedCreditsFirstCycle.toString(), minimumCompletedCreditsFirstCycle.toString() };
                                throw new InsufficientCompletedCreditsInFirstCycleException(null, args);
                            }
                        }
                    }
                }
            }

            final Double completedCreditsSecondCycle = studentCurricularPlan.getSecondCycle().getAprovedEctsCredits();
            if (minimumCompletedCreditsSecondCycle > completedCreditsSecondCycle) {
                final String[] args = { completedCreditsSecondCycle.toString(), minimumCompletedCreditsSecondCycle.toString() };
                throw new InsufficientCompletedCreditsInSecondCycleException(null, args);
            }
        }
        return true;
    }

    private static boolean isStudentOfScheduling(final User userView, final Scheduleing scheduleing) {
        for (final ExecutionDegree otherExecutionDegree : scheduleing.getExecutionDegreesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = otherExecutionDegree.getDegreeCurricularPlan();
            final Degree degree = degreeCurricularPlan.getDegree();
            final Student student = userView.getPerson().getStudent();
            for (final Registration registration : student.getRegistrationsSet()) {
                for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                    if (studentCurricularPlan.getDegree() == degree) {
                        return true;
                    } else {
                        final CurriculumGroup curriculumGroup = studentCurricularPlan.getSecondCycle();
                        if (curriculumGroup != null
                                && curriculumGroup.getDegreeCurricularPlanOfDegreeModule().getDegree() == degree) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static Registration findStudent(final Person person) {
        if (person != null) {
            Registration firstCycle = null;
            for (final Registration registration : person.getStudent().getRegistrationsSet()) {
                final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
                if (studentCurricularPlan != null) {
                    if (studentCurricularPlan.getDegreeType().hasCycleTypes(CycleType.SECOND_CYCLE)) {
                        return registration;
                    } else if (studentCurricularPlan.getDegreeType().hasCycleTypes(CycleType.FIRST_CYCLE)) {
                        firstCycle = registration;
                    }
                }
            }
            return firstCycle;
        }
        return null;
    }

    public static class CandidacyInOtherExecutionDegreesNotAllowed extends FenixServiceException {
        public CandidacyInOtherExecutionDegreesNotAllowed() {
            super();
        }
    }

    public static class CandidacyPeriodNotDefinedException extends FenixServiceException {

        public CandidacyPeriodNotDefinedException() {
            super();
        }

        public CandidacyPeriodNotDefinedException(int errorType) {
            super(errorType);
        }

        public CandidacyPeriodNotDefinedException(String s) {
            super(s);
        }

        public CandidacyPeriodNotDefinedException(Throwable cause) {
            super(cause);
        }

        public CandidacyPeriodNotDefinedException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class OutOfCandidacyPeriodException extends FenixServiceException {

        public OutOfCandidacyPeriodException() {
            super();
        }

        public OutOfCandidacyPeriodException(int errorType) {
            super(errorType);
        }

        public OutOfCandidacyPeriodException(String s) {
            super(s);
        }

        public OutOfCandidacyPeriodException(Throwable cause) {
            super(cause);
        }

        public OutOfCandidacyPeriodException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class NotCompletedCurricularYearException extends FenixServiceException {
        public NotCompletedCurricularYearException(String s, String[] args) {
            super(s, args);
        }
    }

    public static class InsufficientCompletedCreditsInFirstCycleException extends FenixServiceException {
        public InsufficientCompletedCreditsInFirstCycleException(String s, String[] args) {
            super(s, args);
        }
    }

    public static class InsufficientCompletedCreditsInSecondCycleException extends FenixServiceException {
        public InsufficientCompletedCreditsInSecondCycleException(String s, String[] args) {
            super(s, args);
        }
    }

    public static class InsufficientCompletedCoursesException extends FenixServiceException {
        public InsufficientCompletedCoursesException(String s, String[] args) {
            super(s, args);
        }
    }

    public static class NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException extends FenixServiceException {

        public NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException() {
            super();
        }

        public NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException(int errorType) {
            super(errorType);
        }

        public NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException(String s) {
            super(s);
        }

        public NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException(Throwable cause) {
            super(cause);
        }

        public NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException extends FenixServiceException {

        public NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException() {
            super();
        }

        public NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException(int errorType) {
            super(errorType);
        }

        public NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException(String s) {
            super(s);
        }

        public NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException(Throwable cause) {
            super(cause);
        }

        public NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class NumberOfNecessaryCompletedCoursesNotSpecifiedException extends FenixServiceException {

        public NumberOfNecessaryCompletedCoursesNotSpecifiedException() {
            super();
        }

        public NumberOfNecessaryCompletedCoursesNotSpecifiedException(int errorType) {
            super(errorType);
        }

        public NumberOfNecessaryCompletedCoursesNotSpecifiedException(String s) {
            super(s);
        }

        public NumberOfNecessaryCompletedCoursesNotSpecifiedException(Throwable cause) {
            super(cause);
        }

        public NumberOfNecessaryCompletedCoursesNotSpecifiedException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class NoDegreeStudentCurricularPlanFoundException extends FenixServiceException {
    }

    public static class EnrolmentInDissertationIsRequired extends FenixServiceException {

        public EnrolmentInDissertationIsRequired() {
            super();
        }

        public EnrolmentInDissertationIsRequired(int errorType) {
            super(errorType);
        }

        public EnrolmentInDissertationIsRequired(String s) {
            super(s);
        }

        public EnrolmentInDissertationIsRequired(Throwable cause) {
            super(cause);
        }

        public EnrolmentInDissertationIsRequired(String message, Throwable cause) {
            super(message, cause);
        }

    }

}