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
package org.fenixedu.academic.service.services.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularCourseScope;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.dto.InfoCurricularCourse;
import org.fenixedu.academic.dto.InfoCurricularCourseScope;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadCurricularCourseScopesByExecutionCourseID {

    @Atomic
    public static List<InfoCurricularCourse> run(String executionCourseID) throws FenixServiceException {

        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final Set<CurricularCourseScope> curricularCourseScopes =
                    curricularCourse.findCurricularCourseScopesIntersectingExecutionCourse(executionCourse);

            final InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
            infoCurricularCourse.setInfoScopes(new ArrayList());

            for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                infoCurricularCourse.getInfoScopes().add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
            }
            infoCurricularCourses.add(infoCurricularCourse);
        }

        return infoCurricularCourses;
    }
}