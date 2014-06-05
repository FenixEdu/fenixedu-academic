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
 * Created on 21/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.util.Bundle;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */

public class EditStudentGroupShift {

    protected Boolean run(String executionCourseCode, String studentGroupCode, String groupPropertiesCode, String newShiftCode)
            throws FenixServiceException {

        Grouping grouping = FenixFramework.getDomainObject(groupPropertiesCode);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        Shift shift = FenixFramework.getDomainObject(newShiftCode);

        // grouping.checkShiftCapacity(shift);

        StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);

        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        String oldShiftName = studentGroup.getShift().getNome();
        studentGroup.editShift(shift);
        List<ExecutionCourse> ecs = grouping.getExecutionCourses();
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, Bundle.MESSAGING,
                    "log.executionCourse.groupAndShifts.grouping.shift.edited", studentGroup.getGroupNumber().toString(),
                    oldShiftName, shift.getNome(), grouping.getName(), ec.getNome(), ec.getDegreePresentationString());
        }

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final EditStudentGroupShift serviceInstance = new EditStudentGroupShift();

    @Atomic
    public static Boolean runEditStudentGroupShift(String executionCourseCode, String studentGroupCode,
            String groupPropertiesCode, String newShiftCode) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, studentGroupCode, groupPropertiesCode, newShiftCode);
    }

}