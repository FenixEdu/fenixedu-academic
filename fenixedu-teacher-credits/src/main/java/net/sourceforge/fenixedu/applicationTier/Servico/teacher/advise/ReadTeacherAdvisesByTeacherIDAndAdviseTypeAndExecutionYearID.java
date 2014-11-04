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
/**
 * 
 *
 */
package org.fenixedu.academic.service.services.teacher.advise;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.service.filter.DepartmentMemberAuthorizationFilter;
import org.fenixedu.academic.service.filter.TeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.teacher.Advise;
import org.fenixedu.academic.domain.teacher.AdviseType;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author naat
 * 
 */
public class ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID {

    public List<Advise> run(AdviseType adviseType, String teacherID, String executionYearID) throws FenixServiceException,
            DomainException {
        Teacher teacher = FenixFramework.getDomainObject(teacherID);
        List<Advise> result;

        if (executionYearID != null) {
            ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);

            result = Advise.getAdvisesByAdviseTypeAndExecutionYear(teacher, adviseType, executionYear);
        } else {
            result = teacher.getAdvisesSet().stream().filter(a -> a.getAdviseType() == adviseType).collect(Collectors.toList());
        }

        return result;

    }

    // Service Invokers migrated from Berserk

    private static final ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID serviceInstance =
            new ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID();

    @Atomic
    public static List<Advise> runReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID(AdviseType adviseType,
            String teacherID, String executionYearID) throws FenixServiceException, DomainException, NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(adviseType, teacherID, executionYearID);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(adviseType, teacherID, executionYearID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}