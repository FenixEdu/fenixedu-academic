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
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ReadEnroledExecutionCourses {

    @Atomic
    public static List<ExecutionCourse> run(final Registration registration) {
        check(RolePredicates.STUDENT_PREDICATE);

        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();

        for (final Attends attend : registration.getAssociatedAttendsSet()) {
            final ExecutionCourse executionCourse = attend.getExecutionCourse();

            if (executionCourse.getExecutionPeriod() == executionSemester) {
                final List<Grouping> groupings = executionCourse.getGroupings();

                if (checkPeriodEnrollment(groupings) && checkStudentInAttendsSet(groupings, registration)) {
                    result.add(executionCourse);
                }
            }
        }
        return result;
    }

    private static boolean checkPeriodEnrollment(final Grouping grouping) {
        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);
        return strategy.checkEnrolmentDate(grouping, Calendar.getInstance());
    }

    private static boolean checkPeriodEnrollment(final List<Grouping> allGroupProperties) {
        for (final Grouping grouping : allGroupProperties) {
            if (checkPeriodEnrollment(grouping)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkStudentInAttendsSet(final List<Grouping> allGroupProperties, final Registration registration) {
        for (final Grouping grouping : allGroupProperties) {
            if (grouping.getStudentAttend(registration) != null) {
                return true;
            }
        }
        return false;
    }

}