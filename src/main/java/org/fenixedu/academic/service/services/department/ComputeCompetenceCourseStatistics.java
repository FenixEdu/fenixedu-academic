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
/*
 * 
 *  
 */
package org.fenixedu.academic.service.services.department;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.dto.department.CompetenceCourseStatisticsDTO;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;

/**
 * @author pcma
 */
public class ComputeCompetenceCourseStatistics extends ComputeCourseStatistics {

    public List<CompetenceCourseStatisticsDTO> run(Department department, ExecutionSemester executionSemester)
            throws FenixServiceException {
        final List<CompetenceCourseStatisticsDTO> results = new ArrayList<CompetenceCourseStatisticsDTO>();

        final Set<CompetenceCourse> competenceCourses = new HashSet<CompetenceCourse>();

        department.addAllBolonhaCompetenceCourses(competenceCourses, executionSemester);

        for (CompetenceCourse competenceCourse : competenceCourses) {
            final List<Enrolment> enrollments = competenceCourse.getActiveEnrollments(executionSemester);
            if (enrollments.size() > 0) {
                CompetenceCourseStatisticsDTO competenceCourseStatistics = new CompetenceCourseStatisticsDTO();
                competenceCourseStatistics.setExternalId(competenceCourse.getExternalId());
                competenceCourseStatistics.setName(competenceCourse.getNameI18N(executionSemester).getContent());
                createCourseStatistics(competenceCourseStatistics, enrollments);
                results.add(competenceCourseStatistics);
            }
        }

        Collections.sort(results, CompetenceCourseStatisticsDTO.COURSE_STATISTICS_COMPARATOR_BY_NAME);

        return results;
    }

    // Service Invokers migrated from Berserk

    private static final ComputeCompetenceCourseStatistics serviceInstance = new ComputeCompetenceCourseStatistics();

    @Atomic
    public static List<CompetenceCourseStatisticsDTO> runComputeCompetenceCourseStatistics(Department department,
            ExecutionSemester executionSemester) throws FenixServiceException {
        return serviceInstance.run(department, executionSemester);
    }

}