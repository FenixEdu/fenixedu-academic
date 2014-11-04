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
package org.fenixedu.academic.service.services.teacher;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.service.filter.DepartmentMemberAuthorizationFilter;
import org.fenixedu.academic.service.filter.TeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author naat
 */
public class ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType {

    public List<ExecutionCourse> run(String teacherID, String executionYearID, DegreeType degreeType)
            throws FenixServiceException {

        Teacher teacher = FenixFramework.getDomainObject(teacherID);

        List<ExecutionCourse> lecturedExecutionCourses;

        if (executionYearID == null) {
            lecturedExecutionCourses = teacher.getAllLecturedExecutionCourses();

        } else {
            ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);
            lecturedExecutionCourses = teacher.getLecturedExecutionCoursesByExecutionYear(executionYear);
        }

        List<ExecutionCourse> result;

        if (degreeType == DegreeType.DEGREE) {
            result = filterExecutionCourses(lecturedExecutionCourses, false);
        } else {
            // master degree
            result = filterExecutionCourses(lecturedExecutionCourses, true);
        }

        return result;

    }

    private List<ExecutionCourse> filterExecutionCourses(List<ExecutionCourse> executionCourses, boolean masterDegreeOnly) {
        List<ExecutionCourse> masterDegreeExecutionCourses = new ArrayList<ExecutionCourse>();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.isMasterDegreeDFAOrDEAOnly() == masterDegreeOnly) {
                masterDegreeExecutionCourses.add(executionCourse);
            }
        }

        return masterDegreeExecutionCourses;
    }

    // Service Invokers migrated from Berserk

    private static final ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType serviceInstance =
            new ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType();

    @Atomic
    public static List<ExecutionCourse> runReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType(
            String teacherID, String executionYearID, DegreeType degreeType) throws FenixServiceException, NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(teacherID, executionYearID, degreeType);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(teacherID, executionYearID, degreeType);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}