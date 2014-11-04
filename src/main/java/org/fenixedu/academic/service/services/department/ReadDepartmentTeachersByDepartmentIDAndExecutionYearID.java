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
package org.fenixedu.academic.service.services.department;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.service.filter.DepartmentMemberAuthorizationFilter;
import org.fenixedu.academic.service.filter.TeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author naat
 * 
 */
public class ReadDepartmentTeachersByDepartmentIDAndExecutionYearID {

    public List<Teacher> run(String departmentID, String executionYearID) throws FenixServiceException {
        Department department = FenixFramework.getDomainObject(departmentID);

        List<Teacher> teachersFromDepartment = new ArrayList<Teacher>();

        if (executionYearID != null) {
            ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);

            teachersFromDepartment = department.getAllTeachers(executionYear);

        } else {
            teachersFromDepartment = department.getAllTeachers();
        }

        return teachersFromDepartment;
    }

    // Service Invokers migrated from Berserk

    private static final ReadDepartmentTeachersByDepartmentIDAndExecutionYearID serviceInstance =
            new ReadDepartmentTeachersByDepartmentIDAndExecutionYearID();

    @Atomic
    public static List<Teacher> runReadDepartmentTeachersByDepartmentIDAndExecutionYearID(String departmentID,
            String executionYearID) throws FenixServiceException, NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(departmentID, executionYearID);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(departmentID, executionYearID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}