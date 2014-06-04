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
 * Created on 08/Mars/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author joaosa & rmalo
 * 
 */

public class InsertStudentsInGrouping {

    protected Boolean run(final String executionCourseCode, final String groupPropertiesCode, final String[] selected)
            throws FenixServiceException {

        if (selected == null) {
            return Boolean.TRUE;
        }

        final Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        final List<ExecutionCourse> executionCourses = groupProperties.getExecutionCourses();
        StringBuilder sbStudentNumbers = new StringBuilder("");
        sbStudentNumbers.setLength(0);
        // studentCodes list has +1 entry if "select all" was selected
        int totalStudentsProcessed = 0;

        for (final String number : selected) {
            if (number.equals("Todos os Alunos")) {
            } else {
                Registration registration = FenixFramework.getDomainObject(number);
                if (!studentHasSomeAttendsInGrouping(registration, groupProperties)) {
                    final Attends attends = findAttends(registration, executionCourses);
                    if (attends != null) {
                        if (sbStudentNumbers.length() != 0) {
                            sbStudentNumbers.append(", " + registration.getNumber().toString());
                        } else {
                            sbStudentNumbers.append(registration.getNumber().toString());
                        }
                        totalStudentsProcessed++;
                        groupProperties.addAttends(attends);
                    }
                }
            }
        }

        if (totalStudentsProcessed > 0) {
            List<ExecutionCourse> ecs = groupProperties.getExecutionCourses();
            for (ExecutionCourse ec : ecs) {
                GroupsAndShiftsManagementLog.createLog(ec, Bundle.MESSAGING,
                        "log.executionCourse.groupAndShifts.grouping.attends.added", Integer.toString(totalStudentsProcessed),
                        sbStudentNumbers.toString(), groupProperties.getName(), ec.getNome(), ec.getDegreePresentationString());
            }
        }

        return Boolean.TRUE;
    }

    public static boolean studentHasSomeAttendsInGrouping(final Registration registration, final Grouping groupProperties) {
        for (final Attends attends : groupProperties.getAttendsSet()) {
            final Registration otherRegistration = attends.getRegistration();
            if (registration == otherRegistration) {
                return true;
            }
        }
        return false;
    }

    public static Attends findAttends(final Registration registration, final List<ExecutionCourse> executionCourses) {
        for (final ExecutionCourse executionCourse : executionCourses) {
            final Attends attends = registration.readAttendByExecutionCourse(executionCourse);
            if (attends != null) {
                return attends;
            }
        }
        return null;
    }

    // Service Invokers migrated from Berserk

    private static final InsertStudentsInGrouping serviceInstance = new InsertStudentsInGrouping();

    @Atomic
    public static Boolean runInsertStudentsInGrouping(String executionCourseCode, String groupPropertiesCode, String[] selected)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, groupPropertiesCode, selected);
    }

}