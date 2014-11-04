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
package org.fenixedu.academic.service.services.publico;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoExecutionPeriod;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 */
public class ReadExecutionCourse {

    @Atomic
    public static Object run(InfoExecutionPeriod infoExecutionPeriod, String code) {
        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());
        ExecutionCourse iExecCourse = executionSemester.getExecutionCourseByInitials(code);

        if (iExecCourse != null) {
            return InfoExecutionCourse.newInfoFromDomain(iExecCourse);
        }
        return null;
    }

}