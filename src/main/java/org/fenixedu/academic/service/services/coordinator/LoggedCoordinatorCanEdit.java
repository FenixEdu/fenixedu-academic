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
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LoggedCoordinatorCanEdit {

    @Atomic
    public static Boolean run(String executionDegreeCode, String curricularCourseCode, String username)
            throws FenixServiceException {
        Boolean result = new Boolean(false);

        if (executionDegreeCode == null) {
            throw new FenixServiceException("nullExecutionDegreeCode");
        }
        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourseCode");
        }
        if (username == null) {
            throw new FenixServiceException("nullUsername");
        }

        final Person person = Person.readPersonByUsername(username);
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeCode);
        ExecutionYear executionYear = executionDegree.getExecutionYear();

        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        // if user is coordinator and is the current coordinator and
        // curricular course is not basic
        // coordinator can edit
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);
        result =
                Boolean.valueOf((coordinator != null) && executionYear.isCurrent()
                        && curricularCourse.getBasic().equals(Boolean.FALSE));

        return result;
    }

}