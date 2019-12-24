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
package org.fenixedu.academic.dto.administrativeOffice.lists;

import java.io.Serializable;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;

public class ExecutionDegreeListBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private Degree degree;

    private DegreeCurricularPlan degreeCurricularPlan;

    private ExecutionDegree executionDegree;

    private ExecutionYear executionYear;

    private CurricularCourse curricularCourse;

    private ExecutionInterval executionInterval;

    private AcademicInterval academicInterval;

    public ExecutionDegreeListBean() {
        super();

        degree = null;
        degreeCurricularPlan = null;
        executionYear = null;
        curricularCourse = null;
        executionDegree = null;

    }

    public ExecutionDegreeListBean(Degree degree) {
        this();

        setDegree(degree);
    }

    public ExecutionDegreeListBean(DegreeCurricularPlan degreeCurricularPlan) {
        this(degreeCurricularPlan == null ? null : degreeCurricularPlan.getDegree());

        setDegreeCurricularPlan(degreeCurricularPlan);
    }

    public ExecutionDegreeListBean(ExecutionDegree executionDegree) {
        this(executionDegree == null ? null : executionDegree.getDegreeCurricularPlan());

        setExecutionDegree(executionDegree);
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    @Deprecated
    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    @Deprecated
    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    @Deprecated
    public ExecutionInterval getExecutionPeriod() {
        return executionInterval;
    }

    @Deprecated
    public void setExecutionPeriod(ExecutionInterval executionInterval) {
        this.executionInterval = executionInterval;
    }

    public AcademicInterval getAcademicInterval() {
        return academicInterval;
    }

    public void setAcademicInterval(AcademicInterval academicInterval) {
        this.academicInterval = academicInterval;
    }
}
