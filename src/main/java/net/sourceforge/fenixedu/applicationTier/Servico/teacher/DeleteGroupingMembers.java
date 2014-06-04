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
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.util.Bundle;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteGroupingMembers {

    protected Boolean run(String executionCourseCode, String groupingCode, List<String> studentUsernames)
            throws FenixServiceException {

        final Grouping grouping = FenixFramework.getDomainObject(groupingCode);
        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        if (!strategy.checkStudentsUserNamesInGrouping(studentUsernames, grouping)) {
            throw new InvalidSituationServiceException();
        }

        StringBuilder sbStudentNumbers = new StringBuilder("");
        sbStudentNumbers.setLength(0);

        for (final String studentUsername : studentUsernames) {
            Attends attend = grouping.getStudentAttend(studentUsername);
            if (sbStudentNumbers.length() != 0) {
                sbStudentNumbers.append(", " + attend.getRegistration().getNumber().toString());
            } else {
                sbStudentNumbers.append(attend.getRegistration().getNumber().toString());
            }
            removeAttendFromStudentGroups(grouping, attend);
            grouping.removeAttends(attend);
        }

        // no students means no log entry -- list may contain invalid values, so
        // its size cannot be used to test
        if (sbStudentNumbers.length() != 0) {
            List<ExecutionCourse> ecs = grouping.getExecutionCourses();
            for (ExecutionCourse ec : ecs) {
                GroupsAndShiftsManagementLog.createLog(ec, Bundle.MESSAGING,
                        "log.executionCourse.groupAndShifts.grouping.memberSet.removed",
                        Integer.toString(studentUsernames.size()), sbStudentNumbers.toString(), grouping.getName(), ec.getNome(),
                        ec.getDegreePresentationString());
            }
        }

        return true;
    }

    private void removeAttendFromStudentGroups(final Grouping grouping, Attends attend) {
        if (attend != null) {
            for (final StudentGroup studentGroup : grouping.getStudentGroupsSet()) {
                studentGroup.removeAttends(attend);
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteGroupingMembers serviceInstance = new DeleteGroupingMembers();

    @Atomic
    public static Boolean runDeleteGroupingMembers(String executionCourseCode, String groupingCode, List<String> studentUsernames)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, groupingCode, studentUsernames);
    }

}