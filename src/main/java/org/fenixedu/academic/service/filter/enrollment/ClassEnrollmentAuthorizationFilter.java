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
 * Created on 2004/08/24
 *
 */
package org.fenixedu.academic.service.filter.enrollment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeCurricularPlanEquivalencePlan;
import org.fenixedu.academic.domain.EnrolmentPeriodInClasses;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

/**
 * @author Luis Cruz
 * 
 */
public class ClassEnrollmentAuthorizationFilter {

    public static final ClassEnrollmentAuthorizationFilter instance = new ClassEnrollmentAuthorizationFilter();

    private static SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static String comparableDateFormatString = "yyyyMMddHHmm";

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

        if (person.hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
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
            final FenixServiceException exception = verify(studentCurricularPlan);
            hasOneOpen = hasOneOpen || exception == null;
            toThrow = exception == null ? toThrow : exception;
        }
        if (!hasOneOpen) {
            throw toThrow;
        }
    }

    private FenixServiceException verify(StudentCurricularPlan studentCurricularPlan) {
        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        FenixServiceException result = verify(degreeCurricularPlan);
        if (result == null) {
            return null;
        }
        for (final DegreeCurricularPlanEquivalencePlan equivalencePlan : degreeCurricularPlan.getTargetEquivalencePlansSet()) {
            final DegreeCurricularPlan otherDegreeCurricularPlan = equivalencePlan.getDegreeCurricularPlan();
            result = verify(otherDegreeCurricularPlan);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    private FenixServiceException verify(DegreeCurricularPlan degreeCurricularPlan) {
        final EnrolmentPeriodInClasses enrolmentPeriodInClasses = degreeCurricularPlan.getCurrentClassesEnrollmentPeriod();
        if (enrolmentPeriodInClasses == null || enrolmentPeriodInClasses.getStartDateDateTime() == null
                || enrolmentPeriodInClasses.getEndDateDateTime() == null) {
            return new CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan();
        }

        Date now = new Date();
        Date startDate = enrolmentPeriodInClasses.getStartDate();
        Date endDate = enrolmentPeriodInClasses.getEndDate();

        if (DateFormatUtil.compareDates(comparableDateFormatString, startDate, now) > 0
                || DateFormatUtil.compareDates(comparableDateFormatString, endDate, now) < 0) {
            String startDateString = outputDateFormat.format(startDate);
            String endDateString = outputDateFormat.format(endDate);

            StringBuilder buffer = new StringBuilder();
            buffer.append(startDateString);
            buffer.append(" - ");
            buffer.append(endDateString);
            buffer.append(" (").append(enrolmentPeriodInClasses.getExecutionPeriod().getExecutionYear().getName()).append(")");
            return new OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan(buffer.toString());
        }
        return null;
    }

    public class NoActiveStudentCurricularPlanOfCorrectTypeException extends FenixServiceException {
    }

    public class CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan extends FenixServiceException {
        public CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan() {
            super("error.enrolmentPeriodNotDefined");
        }
    }

    public class OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan extends FenixServiceException {
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
            super(message);
        }
    }

}