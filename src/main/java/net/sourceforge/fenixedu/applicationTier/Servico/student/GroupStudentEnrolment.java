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

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.util.MessageResources;

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
        if (studentGroup.getAttends().contains(studentAttend)) {
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
            if (studentGroup != studentGroupEnrolled && studentGroup.getAttends().contains(studentAttend)) {
                throw new InvalidSituationServiceException();
            }
        }
    }
}