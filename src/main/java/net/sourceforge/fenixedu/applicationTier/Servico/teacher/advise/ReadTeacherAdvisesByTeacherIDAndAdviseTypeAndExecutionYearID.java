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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.advise;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
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

            result = teacher.getAdvisesByAdviseTypeAndExecutionYear(adviseType, executionYear);
        } else {
            result = teacher.getAdvisesByAdviseType(adviseType);
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