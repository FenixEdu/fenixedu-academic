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
import java.util.Map;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.dto.department.DegreeCourseStatisticsDTO;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;

public class ComputeDegreeCourseStatistics extends ComputeCourseStatistics {

    public List<DegreeCourseStatisticsDTO> run(CompetenceCourse competenceCourse, ExecutionSemester executionSemester)
            throws FenixServiceException {
        Map<Degree, List<CurricularCourse>> groupedCourses = competenceCourse.getAssociatedCurricularCoursesGroupedByDegree();

        List<DegreeCourseStatisticsDTO> results = new ArrayList<DegreeCourseStatisticsDTO>();

        for (Degree degree : groupedCourses.keySet()) {
            List<Enrolment> enrollments = new ArrayList<Enrolment>();
            List<CurricularCourse> curricularCourses = groupedCourses.get(degree);

            for (CurricularCourse curricularCourse : curricularCourses) {
                enrollments.addAll(curricularCourse.getActiveEnrollments(executionSemester));
            }

            DegreeCourseStatisticsDTO degreeCourseStatistics = new DegreeCourseStatisticsDTO();
            degreeCourseStatistics.setExternalId(degree.getExternalId());
            degreeCourseStatistics.setName(degree.getSigla());
            createCourseStatistics(degreeCourseStatistics, enrollments);

            results.add(degreeCourseStatistics);
        }

        return results;
    }

    // Service Invokers migrated from Berserk

    private static final ComputeDegreeCourseStatistics serviceInstance = new ComputeDegreeCourseStatistics();

    @Atomic
    public static List<DegreeCourseStatisticsDTO> runComputeDegreeCourseStatistics(CompetenceCourse competenceCourse,
            ExecutionSemester executionSemester) throws FenixServiceException {
        return serviceInstance.run(competenceCourse, executionSemester);
    }

}