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
 * Created on 27/Ago/2003
 *
 */

package org.fenixedu.academic.service.services.student;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Collection;

import org.apache.struts.util.MessageResources;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.ServiceMonitoring;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidSituationServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import org.fenixedu.academic.util.Bundle;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */
public class GroupStudentEnrolment {

    private static final MessageResources messages = MessageResources.getMessageResources(Bundle.GLOBAL);

    @Atomic
    public static Boolean run(String studentGroupCode, String username) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        ServiceMonitoring.logService(GroupStudentEnrolment.class, studentGroupCode, username);

        final StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);
        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }
        final Registration registration = Registration.readByUsername(username);
        if (registration == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Grouping grouping = studentGroup.getGrouping();
        final Attends studentAttend = grouping.getStudentAttend(registration);
        if (studentAttend == null) {
            throw new NotAuthorizedException();
        }
        if (studentGroup.getAttendsSet().contains(studentAttend)) {
            throw new InvalidSituationServiceException();
        }

        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        boolean result = strategy.checkPossibleToEnrolInExistingGroup(grouping, studentGroup);
        if (!result) {
            throw new InvalidArgumentsServiceException();
        }

        checkIfStudentIsNotEnrolledInOtherGroups(grouping.getStudentGroupsSet(), studentGroup, studentAttend);

        studentGroup.addAttends(studentAttend);

        informStudents(studentGroup, registration, grouping);

        return Boolean.TRUE;
    }

    private static void informStudents(final StudentGroup studentGroup, final Registration registration, final Grouping grouping) {

        final StringBuilder executionCourseNames = new StringBuilder();
        for (final ExecutionCourse executionCourse : grouping.getExecutionCourses()) {
            if (executionCourseNames.length() > 0) {
                executionCourseNames.append(", ");
            }
            executionCourseNames.append(executionCourse.getNome());
        }
    }

    private static void checkIfStudentIsNotEnrolledInOtherGroups(final Collection<StudentGroup> studentGroups,
            final StudentGroup studentGroupEnrolled, final Attends studentAttend) throws InvalidSituationServiceException {

        for (final StudentGroup studentGroup : studentGroups) {
            if (studentGroup != studentGroupEnrolled && studentGroup.getAttendsSet().contains(studentAttend)) {
                throw new InvalidSituationServiceException();
            }
        }
    }
}