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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 */
//TODO: check usage
public class ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear {

    @Atomic
    public static List run(String executionDegreeId, String executionPeriodId, Integer curricularYearInt)
            throws FenixServiceException {

        if (executionPeriodId == null) {
            throw new FenixServiceException("nullExecutionPeriodId");
        }

        final ExecutionInterval executionInterval = FenixFramework.getDomainObject(executionPeriodId);

        final List<ExecutionCourse> executionCourseList;
        if (executionDegreeId == null && curricularYearInt == null) {
            executionCourseList = executionInterval.getAssociatedExecutionCoursesSet().stream()
                    .filter(ec -> ec.getAssociatedCurricularCoursesSet().isEmpty()).collect(Collectors.toList());
        } else {
            final ExecutionDegree executionDegree = findExecutionDegreeByID(executionInterval, executionDegreeId);
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final CurricularYear curricularYear = CurricularYear.readByYear(curricularYearInt);
            executionCourseList = ExecutionCourse.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
                    executionInterval, degreeCurricularPlan, curricularYear, "%");
        }

        final List infoExecutionCourseList = new ArrayList(executionCourseList.size());
        for (final ExecutionCourse executionCourse : executionCourseList) {
            infoExecutionCourseList.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }

        return infoExecutionCourseList;
    }

    private static ExecutionDegree findExecutionDegreeByID(final ExecutionInterval executionInterval,
            final String executionDegreeId) {
        final ExecutionYear executionYear = executionInterval.getExecutionYear();
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
            if (executionDegree.getExternalId().equals(executionDegreeId)) {
                return executionDegree;
            }
        }
        return null;
    }

}