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
 * Created on 2004/08/24
 *
 */
package org.fenixedu.academic.service.filter.enrollment;

import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeCurricularPlanEquivalencePlan;
import org.fenixedu.academic.domain.EnrolmentPeriod;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author Luis Cruz
 * 
 */
public class ClassEnrollmentAuthorizationFilter {

    public static final ClassEnrollmentAuthorizationFilter instance = new ClassEnrollmentAuthorizationFilter();

    private static ConcurrentLinkedQueue<ClassEnrollmentCondition> conditions = new ConcurrentLinkedQueue<>();

    @FunctionalInterface
    public static interface ClassEnrollmentCondition {
        public void verify(Registration registration) throws DomainException;
    }

    public static void registerCondition(ClassEnrollmentCondition condition) {
        conditions.add(condition);
    }

    public void execute(Registration registration) throws FenixServiceException {
        Person person = Authenticate.getUser().getPerson();

        if (AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS, registration.getDegree(),
                person.getUser())) {
            return;
        }

        if (RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(person.getUser())) {
            person = registration.getPerson();
        }

        for (ClassEnrollmentCondition condition : conditions) {
            condition.verify(registration);
        }

        final SortedSet<StudentCurricularPlan> activeStudentCurricularPlans =
                person.getActiveStudentCurricularPlansSortedByDegreeTypeAndDegreeName();

        if (activeStudentCurricularPlans.isEmpty()) {
            throw new NoActiveStudentCurricularPlanOfCorrectTypeException();
        }

        boolean hasOneOpen = false;
        FenixServiceException toThrow = null;
        for (final StudentCurricularPlan studentCurricularPlan : activeStudentCurricularPlans) {
            final FenixServiceException exception =
                    verify(studentCurricularPlan, ExecutionSemester.readActualExecutionSemester());
            hasOneOpen = hasOneOpen || exception == null;
            toThrow = exception == null ? toThrow : exception;
        }
        if (!hasOneOpen) {
            throw toThrow;
        }
    }

    public void execute(Registration registration, ExecutionSemester executionSemester) throws FenixServiceException {
        Person person = Authenticate.getUser().getPerson();

        if (AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS, registration.getDegree(),
                person.getUser())) {
            return;
        }

        if (RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(person.getUser())) {
            person = registration.getPerson();
        }

        for (ClassEnrollmentCondition condition : conditions) {
            condition.verify(registration);
        }

        final SortedSet<StudentCurricularPlan> activeStudentCurricularPlans =
                person.getActiveStudentCurricularPlansSortedByDegreeTypeAndDegreeName();

        if (activeStudentCurricularPlans.isEmpty()) {
            throw new NoActiveStudentCurricularPlanOfCorrectTypeException();
        }

        boolean hasOneOpen = false;
        FenixServiceException toThrow = null;
        for (final StudentCurricularPlan studentCurricularPlan : activeStudentCurricularPlans) {
            final FenixServiceException exception = verify(studentCurricularPlan, executionSemester);
            hasOneOpen = hasOneOpen || exception == null;
            toThrow = exception == null ? toThrow : exception;
        }
        if (!hasOneOpen) {
            throw toThrow;
        }
    }

    private FenixServiceException verify(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        FenixServiceException result = verifyEnrolmentPeriod(degreeCurricularPlan, executionSemester, studentCurricularPlan);
        if (result == null) {
            return null;
        }
        for (final DegreeCurricularPlanEquivalencePlan equivalencePlan : degreeCurricularPlan.getTargetEquivalencePlansSet()) {
            final DegreeCurricularPlan otherDegreeCurricularPlan = equivalencePlan.getDegreeCurricularPlan();
            result = verifyEnrolmentPeriod(degreeCurricularPlan, executionSemester, studentCurricularPlan);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    private FenixServiceException verifyEnrolmentPeriod(DegreeCurricularPlan dcp, ExecutionSemester executionSemester,
            StudentCurricularPlan scp) {
        Optional<EnrolmentPeriod> enrolmentPeriodInClasses = null;
        if (scp.getRegistration().getRegistrationProtocol().isMobilityAgreement()) {
            enrolmentPeriodInClasses = dcp.getClassesEnrollmentPeriodMobility(executionSemester);
        } else {
            enrolmentPeriodInClasses = dcp.getClassesEnrollmentPeriod(executionSemester);
        }
        if (!enrolmentPeriodInClasses.isPresent() || enrolmentPeriodInClasses.get().getStartDateDateTime() == null
                || enrolmentPeriodInClasses.get().getEndDateDateTime() == null) {
            return new CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan();
        }

        if (!enrolmentPeriodInClasses.get().isValid()) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(enrolmentPeriodInClasses.get().getStartDateDateTime().toString("dd/MM/yyyy HH:mm"));
            buffer.append(" - ");
            buffer.append(enrolmentPeriodInClasses.get().getEndDateDateTime().toString("dd/MM/yyyy HH:mm"));
            buffer.append(" (").append(enrolmentPeriodInClasses.get().getExecutionPeriod().getExecutionYear().getName())
                    .append(")");
            return new OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(buffer.toString());
        }
        return null;
    }

    public static class NoActiveStudentCurricularPlanOfCorrectTypeException extends FenixServiceException {
    }

    public static class CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan extends FenixServiceException {
        public CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan() {
            super("error.enrolmentPeriodNotDefined");
        }
    }

    public static class OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan extends FenixServiceException {
        public OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan() {
            super();
        }

        public OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(String message, Throwable cause) {
            super(message, cause);
        }

        public OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(Throwable cause) {
            super(cause);
        }

        public OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(String message) {
            super("error.enrollment.period.closed", new String[] { message });
        }
    }

}