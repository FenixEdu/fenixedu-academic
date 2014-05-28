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
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.ExecutionCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import pt.ist.fenixframework.Atomic;

public class ComputeExecutionCourseStatistics extends ComputeCourseStatistics {

    public List<ExecutionCourseStatisticsDTO> run(CompetenceCourse competenceCourse, Degree degree,
            ExecutionSemester executionSemester) throws FenixServiceException {

        List<CurricularCourse> curricularCourses = competenceCourse.getAssociatedCurricularCoursesGroupedByDegree().get(degree);

        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();

        // for (ExecutionPeriod executionPeriod :
        // executionYear.getExecutionPeriods()) {
        for (CurricularCourse course : curricularCourses) {
            executionCourses.addAll(course.getExecutionCoursesByExecutionPeriod(executionSemester));
        }
        // }

        List<ExecutionCourseStatisticsDTO> results = new ArrayList<ExecutionCourseStatisticsDTO>();

        for (ExecutionCourse executionCourse : executionCourses) {
            ExecutionCourseStatisticsDTO executionCourseStatistics = new ExecutionCourseStatisticsDTO();
            executionCourseStatistics.setExternalId(competenceCourse.getExternalId());
            executionCourseStatistics.setName(competenceCourse.getNameI18N(executionSemester).getContent());

            executionCourseStatistics.setExecutionPeriod(executionCourse.getExecutionPeriod().getName());
            executionCourseStatistics.setTeachers(getResponsibleTeachersName(executionCourse));
            executionCourseStatistics.setExecutionYear(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
            executionCourseStatistics.setDegrees(getDegrees(executionCourse));

            createCourseStatistics(executionCourseStatistics, executionCourse.getActiveEnrollments());

            results.add(executionCourseStatistics);
        }

        return results;
    }

    private List<String> getResponsibleTeachersName(ExecutionCourse executionCourse) {
        List<String> result = new ArrayList<String>();
        for (Professorship professorship : executionCourse.getProfessorships()) {
            if (professorship.getResponsibleFor().booleanValue()) {
                result.add(professorship.getPerson().getName());
            }
        }

        return result;
    }

    private List<String> getDegrees(ExecutionCourse executionCourse) {
        Set<Degree> degrees = new HashSet<Degree>();
        for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            degrees.add(curricularCourse.getDegreeCurricularPlan().getDegree());
        }

        ExecutionYear executionYear = executionCourse.getExecutionYear();

        List<String> degreeNames = new ArrayList<String>();
        for (Degree degree : degrees) {
            degreeNames.add(degree.getNameFor(executionYear).getContent());
        }

        return degreeNames;
    }

    // Service Invokers migrated from Berserk

    private static final ComputeExecutionCourseStatistics serviceInstance = new ComputeExecutionCourseStatistics();

    @Atomic
    public static List<ExecutionCourseStatisticsDTO> runComputeExecutionCourseStatistics(CompetenceCourse competenceCourse,
            Degree degree, ExecutionSemester executionSemester) throws FenixServiceException {
        return serviceInstance.run(competenceCourse, degree, executionSemester);
    }

}