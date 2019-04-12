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
package org.fenixedu.academic.dto.commons;

import java.io.Serializable;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;

import pt.ist.fenixframework.FenixFramework;

public class CurricularCourseByExecutionSemesterBean
        implements Serializable, Comparable<CurricularCourseByExecutionSemesterBean> {

    private CurricularCourse curricularCourse;
    private ExecutionSemester executionSemester;

    public CurricularCourseByExecutionSemesterBean() {
    }

    public CurricularCourseByExecutionSemesterBean(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        setCurricularCourse(curricularCourse);
        setExecutionSemester(executionSemester);
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public ExecutionYear getExecutionYear() {
        return executionSemester.getExecutionYear();
    }

    public String getCurricularCourseName() {
        return getCurricularCourse().getName(getExecutionSemester());
    }

    public String getCurricularCourseNameEn() {
        return getCurricularCourse().getNameEn(getExecutionSemester());
    }

    public Double getCurricularCourseEcts() {
        return getCurricularCourse().getEctsCredits(getExecutionSemester());
    }

    public String getDegreeName() {
        return getCurricularCourse().getDegree().getNameFor(getExecutionSemester()).getContent();
    }

    public String getKey() {
        return getCurricularCourse().getExternalId() + ":" + getExecutionSemester().getExternalId();
    }

    public String getAcronym() {
        return getCurricularCourse().getAcronym(getExecutionSemester());
    }

    public DepartmentUnit getDepartmentUnit() {
        return getCurricularCourse().getCompetenceCourse() == null ? null : getCurricularCourse().getCompetenceCourse()
                .getDepartmentUnit(getExecutionSemester());
    }

    public Double getWeight() {
        return getCurricularCourse().getWeight(getExecutionSemester());
    }

    public String getObjectives() {
        return getCurricularCourse().getCompetenceCourse() == null ? null : getCurricularCourse().getCompetenceCourse()
                .getObjectives(getExecutionSemester());
    }

    public String getObjectivesEn() {
        return getCurricularCourse().getCompetenceCourse() == null ? null : getCurricularCourse().getCompetenceCourse()
                .getObjectivesEn(getExecutionSemester());
    }

    public String getProgram() {
        return getCurricularCourse().getCompetenceCourse() == null ? null : getCurricularCourse().getCompetenceCourse()
                .getProgram(getExecutionSemester());
    }

    public String getProgramEn() {
        return getCurricularCourse().getCompetenceCourse() == null ? null : getCurricularCourse().getCompetenceCourse()
                .getProgramEn(getExecutionSemester());
    }

    public String getEvaluationMethod() {
        return getCurricularCourse().getCompetenceCourse() == null ? null : getCurricularCourse().getCompetenceCourse()
                .getEvaluationMethod(getExecutionSemester());
    }

    public String getEvaluationMethodEn() {
        return getCurricularCourse().getCompetenceCourse() == null ? null : getCurricularCourse().getCompetenceCourse()
                .getEvaluationMethodEn(getExecutionSemester());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CurricularCourseByExecutionSemesterBean) ? equals(
                (CurricularCourseByExecutionSemesterBean) obj) : false;
    }

    public boolean equals(CurricularCourseByExecutionSemesterBean obj) {
        return getCurricularCourse() == obj.getCurricularCourse();
    }

    @Override
    public int hashCode() {
        return getCurricularCourse().hashCode();
    }

    @Override
    public int compareTo(CurricularCourseByExecutionSemesterBean other) {
        return other == null ? 1 : CurricularCourse.COMPARATOR_BY_NAME.compare(getCurricularCourse(),
                other.getCurricularCourse());
    }

    static public CurricularCourseByExecutionSemesterBean buildFrom(final String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        final String[] values = key.split(":");
        final CurricularCourse course = FenixFramework.getDomainObject(values[0]);
        final ExecutionSemester semester = FenixFramework.getDomainObject(values[1]);
        return new CurricularCourseByExecutionSemesterBean(course, semester);
    }
}
