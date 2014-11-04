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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenTest;

import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.Atomic;

public class TeacherEditWrittenTestRooms {

    protected void run(ExecutionCourse executionCourse, Teacher teacher, WrittenTest writtenTest, List<Space> rooms) {
        writtenTest.teacherEditRooms(teacher, executionCourse.getExecutionPeriod(), rooms);
    }

    // Service Invokers migrated from Berserk

    private static final TeacherEditWrittenTestRooms serviceInstance = new TeacherEditWrittenTestRooms();

    @Atomic
    public static void runTeacherEditWrittenTestRooms(ExecutionCourse executionCourse, Teacher teacher, WrittenTest writtenTest,
            List<Space> rooms) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourse);
        serviceInstance.run(executionCourse, teacher, writtenTest, rooms);
    }

}