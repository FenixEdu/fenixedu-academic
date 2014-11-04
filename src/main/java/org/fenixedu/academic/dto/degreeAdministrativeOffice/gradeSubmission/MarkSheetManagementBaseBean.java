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
package org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;

public class MarkSheetManagementBaseBean implements Serializable {

    private Degree degree;
    private DegreeCurricularPlan degreeCurricularPlan;
    private ExecutionSemester executionSemester;
    private CurricularCourseMarksheetManagementBean curricularCourseBean;
    transient private Teacher teacher;
    private String url;

    public CurricularCourseMarksheetManagementBean getCurricularCourseBean() {
        return curricularCourseBean;
    }

    public void setCurricularCourseBean(CurricularCourseMarksheetManagementBean curricularCourseBean) {
        this.curricularCourseBean = curricularCourseBean;
    }

    public CurricularCourse getCurricularCourse() {
        return getCurricularCourseBean().getCurricularCourse();
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public boolean hasDegree() {
        return getDegree() != null;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return this.degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDegreeName() {
        return getDegree().getNameFor(getExecutionPeriod()).getContent();
    }

    public String getDegreeCurricularPlanName() {
        return getDegreeCurricularPlan().getName();
    }

    public String getCurricularCourseName() {
        return getCurricularCourse().getName(getExecutionPeriod());
    }

    public String getCurricularCourseNameAndCode() {
        return getCurricularCourse().getName(getExecutionPeriod()) + " " + getCurricularCourse().getAcronym(getExecutionPeriod());
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }
}
