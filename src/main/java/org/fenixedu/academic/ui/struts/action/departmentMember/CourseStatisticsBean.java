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
package org.fenixedu.academic.ui.struts.action.departmentMember;

import java.io.Serializable;
import java.util.List;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.dto.department.CompetenceCourseStatisticsDTO;
import org.fenixedu.academic.dto.department.DegreeCourseStatisticsDTO;
import org.fenixedu.academic.dto.department.ExecutionCourseStatisticsDTO;
import org.fenixedu.academic.service.services.department.ComputeCompetenceCourseStatistics;
import org.fenixedu.academic.service.services.department.ComputeDegreeCourseStatistics;
import org.fenixedu.academic.service.services.department.ComputeExecutionCourseStatistics;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

public class CourseStatisticsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Department department;

    private CompetenceCourse competenceCourse = null;

    private Degree degree = null;

    private ExecutionSemester executionSemester = null;

    private List<DegreeCourseStatisticsDTO> degreeCourses = null;

    private List<ExecutionCourseStatisticsDTO> executionCourses = null;

    private List<CompetenceCourseStatisticsDTO> competenceCourses = null;

    public CourseStatisticsBean(Department department, ExecutionSemester executionSemester) {
        this.department = department;
        this.executionSemester = executionSemester;
    }

    public CourseStatisticsBean(Department department, CompetenceCourse competenceCourse, ExecutionSemester executionSemester) {
        this.department = department;
        this.competenceCourse = competenceCourse;
        this.executionSemester = executionSemester;
    }

    public CourseStatisticsBean(Department department, CompetenceCourse competenceCourse, Degree degree,
            ExecutionSemester executionSemester) {
        this.department = department;
        this.degree = degree;
        this.competenceCourse = competenceCourse;
        this.executionSemester = executionSemester;
    }

    public ExecutionSemester getExecutionSemester() {
        if (executionSemester == null) {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        }
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) throws FenixServiceException {
        this.executionSemester = executionSemester;
    }

    private void loadCompetenceCourses() throws FenixServiceException {
        competenceCourses =
                ComputeCompetenceCourseStatistics.runComputeCompetenceCourseStatistics(department, getExecutionSemester());
    }

    public List<CompetenceCourseStatisticsDTO> getCompetenceCourses() throws FenixServiceException {
        if (competenceCourses == null) {
            loadCompetenceCourses();
        }

        return competenceCourses;
    }

    private void loadDegreeCourses() throws FenixServiceException {
        degreeCourses = ComputeDegreeCourseStatistics.runComputeDegreeCourseStatistics(competenceCourse, getExecutionSemester());
    }

    public List<DegreeCourseStatisticsDTO> getDegreeCourses() throws FenixServiceException {
        if (degreeCourses == null) {
            loadDegreeCourses();
        }

        return degreeCourses;
    }

    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse;
    }

    public Degree getDegree() {
        return degree;
    }

    private void loadExecutionCourses() throws FenixServiceException {
        executionCourses =
                ComputeExecutionCourseStatistics.runComputeExecutionCourseStatistics(getCompetenceCourse(), getDegree(),
                        getExecutionSemester());
    }

    public List<ExecutionCourseStatisticsDTO> getExecutionCourses() throws FenixServiceException {
        if (executionCourses == null) {
            loadExecutionCourses();
        }

        return executionCourses;
    }

    public Department getDepartment() {
        return department;
    }

    public String getCompetenceCourseName() {
        return competenceCourse.getNameI18N(getExecutionSemester()).getContent();
    }
}
