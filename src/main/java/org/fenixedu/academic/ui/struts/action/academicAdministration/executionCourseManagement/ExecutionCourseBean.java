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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.interfaces.HasExecutionDegree;
import org.fenixedu.academic.domain.interfaces.HasExecutionSemester;

public class ExecutionCourseBean implements Serializable, HasExecutionSemester, HasExecutionDegree {

    private ExecutionDegree executionDegree;
    private CurricularYear curricularYear;
    private ExecutionCourse sourceExecutionCourse;
    private ExecutionCourse destinationExecutionCourse;
    private ExecutionSemester executionSemester;
    private Boolean chooseNotLinked;

    @Override
    public ExecutionSemester getExecutionPeriod() {
        return getExecutionSemester();
    }

    @Override
    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public CurricularYear getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(CurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public ExecutionCourse getSourceExecutionCourse() {
        return sourceExecutionCourse;
    }

    public void setSourceExecutionCourse(ExecutionCourse sourceExecutionCourse) {
        this.sourceExecutionCourse = sourceExecutionCourse;
    }

    public ExecutionCourse getDestinationExecutionCourse() {
        return destinationExecutionCourse;
    }

    public void setDestinationExecutionCourse(ExecutionCourse destinationExecutionCourse) {
        this.destinationExecutionCourse = destinationExecutionCourse;
    }

    public Boolean getChooseNotLinked() {
        return this.chooseNotLinked;
    }

    public void setChooseNotLinked(Boolean chooseNotLinked) {
        this.chooseNotLinked = chooseNotLinked;
    }

    public ExecutionCourseBean(ExecutionCourse executionCourse) {
        setSourceExecutionCourse(executionCourse);
    }

    public Collection<ExecutionCourse> getExecutionCourses() {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        if (this.chooseNotLinked) {
            result = this.getExecutionSemester().getExecutionCoursesWithNoCurricularCourses();
        } else {
            for (final CurricularCourse curricularCourse : getDegreeCurricularPlan().getCurricularCourses(getExecutionSemester())) {
                if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(getCurricularYear(),
                        getDegreeCurricularPlan(), getExecutionSemester())) {
                    result.addAll(curricularCourse.getExecutionCoursesByExecutionPeriod(getExecutionSemester()));
                }
            }
        }
        TreeSet<ExecutionCourse> finalResult = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        finalResult.addAll(result);
        return finalResult;

    }

    private DegreeCurricularPlan getDegreeCurricularPlan() {
        return getExecutionDegree().getDegreeCurricularPlan();
    }

    public ExecutionCourseBean() {
    }

}
