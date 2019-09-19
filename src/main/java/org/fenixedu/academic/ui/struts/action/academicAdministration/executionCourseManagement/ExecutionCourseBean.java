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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.interfaces.HasExecutionDegree;
import org.fenixedu.academic.domain.interfaces.HasExecutionSemester;

import com.google.common.collect.Sets;

@SuppressWarnings("serial")
public class ExecutionCourseBean implements Serializable, HasExecutionSemester, HasExecutionDegree {

    private ExecutionDegree executionDegree;
    private Degree degree;
    private CurricularYear curricularYear;
    private ExecutionCourse sourceExecutionCourse;
    private ExecutionCourse destinationExecutionCourse;
    private ExecutionInterval executionInterval;
    private Boolean chooseNotLinked;

    @Override
    public ExecutionInterval getExecutionPeriod() {
        return getExecutionSemester();
    }

    @Override
    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(final ExecutionDegree input) {
        if (input != null && getDegree() != null && getDegree() != input.getDegree()) {
            throw new IllegalStateException();
        }

        this.executionDegree = input;
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(final Degree input) {
        if (input != null && getExecutionDegree() != null && getExecutionDegree().getDegree() != input) {
            throw new IllegalStateException();
        }

        this.degree = input;
    }

    public CurricularYear getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(CurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    public ExecutionInterval getExecutionSemester() {
        return executionInterval;
    }

    public void setExecutionSemester(ExecutionInterval executionInterval) {
        this.executionInterval = executionInterval;
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

    public ExecutionCourseBean() {
    }

    public ExecutionCourseBean(ExecutionCourse executionCourse) {
        setSourceExecutionCourse(executionCourse);
    }

    public Collection<ExecutionCourse> getExecutionCourses() {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        if (this.chooseNotLinked) {
            result = this.getExecutionSemester().getAssociatedExecutionCoursesSet().stream()
                    .filter(ec -> ec.getAssociatedCurricularCoursesSet().isEmpty()).collect(Collectors.toList());
        } else {
            for (final CurricularCourse curricularCourse : getDegreeCurricularPlan()
                    .getCurricularCourses(getExecutionSemester())) {
                if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(getCurricularYear(), getDegreeCurricularPlan(),
                        getExecutionSemester())) {
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

    public String getSourcePresentationName() {
        StringBuilder result = new StringBuilder();

        if (getSourceExecutionCourse() != null) {
            result.append(getCode(getSourceExecutionCourse()));
            result.append(getSourceExecutionCourse().getNameI18N().getContent());

            final Set<DegreeCurricularPlan> plans;
            if (getDegree() != null) {
                plans = Sets.intersection(getDegree().getDegreeCurricularPlansSet(),
                        Sets.newHashSet(getSourceExecutionCourse().getAssociatedDegreeCurricularPlans()));
            } else {
                plans = Sets.newHashSet(getSourceExecutionCourse().getAssociatedDegreeCurricularPlans());
            }

            result.append(getDegreeCurricularPlansPresentationString(plans));
        }

        return result.toString();
    }

    public String getDestinationPresentationName() {
        StringBuilder result = new StringBuilder();

        if (getDestinationExecutionCourse() != null) {
            result.append(getCode(getDestinationExecutionCourse()));
            result.append(getDestinationExecutionCourse().getNameI18N().getContent());

            final Set<DegreeCurricularPlan> plans;
            if (getDegree() != null) {
                plans = Sets.intersection(getDegree().getDegreeCurricularPlansSet(),
                        Sets.newHashSet(getDestinationExecutionCourse().getAssociatedDegreeCurricularPlans()));
            } else {
                plans = Sets.newHashSet(getDestinationExecutionCourse().getAssociatedDegreeCurricularPlans());
            }

            result.append(getDegreeCurricularPlansPresentationString(plans));
        }

        return result.toString();
    }

    public static String getCode(final ExecutionCourse executionCourse) {
        return "[" + executionCourse.getCompetenceCourses().stream().map(cc -> cc.getCode()).distinct()
                .collect(Collectors.joining(", ")) + "] ";
    }

    static private String getDegreeCurricularPlansPresentationString(final Set<DegreeCurricularPlan> input) {
        StringBuilder result = new StringBuilder(" [ ");

        for (Iterator<DegreeCurricularPlan> iterator = input.iterator(); iterator.hasNext();) {
            final DegreeCurricularPlan iter = iterator.next();
            result.append(iter.getName());
            if (iterator.hasNext()) {
                result.append(" , ");
            }
        }

        result.append(" ]");

        return result.toString();
    }

}
