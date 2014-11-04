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
package org.fenixedu.academic.service.services.degree.execution;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExecutionCoursesByExecutionDegreeService {

    public static class NonExistingExecutionDegree extends FenixServiceException {
        public NonExistingExecutionDegree() {
            super();
        }
    }

    @Atomic
    public static List run(String executionDegreeId, String executionPeriodId) throws FenixServiceException {

        final ExecutionSemester executionSemester;
        if (StringUtils.isEmpty(executionPeriodId)) {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        } else {
            executionSemester = FenixFramework.getDomainObject(executionPeriodId);
        }

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
        if (executionDegree == null) {
            throw new NonExistingExecutionDegree();
        }

        Set<ExecutionCourse> executionCourseList =
                executionDegree.getDegreeCurricularPlan().getExecutionCoursesByExecutionPeriod(executionSemester);

        List infoExecutionCourseList = (List) CollectionUtils.collect(executionCourseList, new Transformer() {

            @Override
            public Object transform(Object input) {
                ExecutionCourse executionCourse = (ExecutionCourse) input;
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                return infoExecutionCourse;
            }
        });

        return infoExecutionCourseList;

    }
}