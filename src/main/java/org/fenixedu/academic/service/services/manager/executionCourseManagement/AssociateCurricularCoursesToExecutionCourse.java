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
package org.fenixedu.academic.service.services.manager.executionCourseManagement;

import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/*
 * 
 * @author Fernanda Quitério 29/Dez/2003
 */
public class AssociateCurricularCoursesToExecutionCourse {

    @Atomic
    public static void run(String executionCourseId, List<String> curricularCourseIds) throws FenixServiceException {
        if (executionCourseId == null) {
            throw new FenixServiceException("nullExecutionCourseId");
        }

        if (curricularCourseIds != null && !curricularCourseIds.isEmpty()) {
            ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);

            if (executionCourse == null) {
                throw new DomainException("message.nonExisting.executionCourse");
            }

            Iterator<String> iter = curricularCourseIds.iterator();
            while (iter.hasNext()) {
                String curricularCourseId = iter.next();

                CurricularCourse curricularCourse = FenixFramework.getDomainObject(curricularCourseId);
                if (curricularCourse == null) {
                    throw new DomainException("message.nonExistingDegreeCurricularPlan");
                }
                if (!curricularCourse.getAssociatedExecutionCoursesSet().contains(executionCourse)) {
                    curricularCourse.addAssociatedExecutionCourses(executionCourse);
                }
            }
        } else {
            throw new DomainException("error.selection.noCurricularCourse");
        }
    }
}