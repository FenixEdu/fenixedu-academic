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

import java.util.SortedSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.groups.PermissionService;
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
                person.getUser())
                || PermissionService.hasAccess("ACADEMIC_OFFICE_ENROLMENTS", registration.getDegree(), person.getUser())) {
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

    }

    public void execute(Registration registration, ExecutionInterval executionInterval) throws FenixServiceException {
        Person person = Authenticate.getUser().getPerson();

        if (AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS, registration.getDegree(),
                person.getUser())
                || PermissionService.hasAccess("ACADEMIC_OFFICE_ENROLMENTS", registration.getDegree(), person.getUser())) {
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