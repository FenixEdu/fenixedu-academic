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
package org.fenixedu.academic.dto.student.enrollment.bolonha;

import java.io.Serializable;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.student.Registration;

public class BolonhaStudentOptionalEnrollmentBean implements Serializable {

    static private final long serialVersionUID = -4707696936211804716L;

    private DegreeType degreeType;

    private Degree degree;

    private DegreeCurricularPlan degreeCurricularPlan;

    private ExecutionInterval executionInterval;

    private CurricularCourse selectedOptionalCurricularCourse;

    private StudentCurricularPlan studentCurricularPlan;

    private IDegreeModuleToEvaluate selectedDegreeModuleToEnrol;

    public BolonhaStudentOptionalEnrollmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionInterval executionInterval, final IDegreeModuleToEvaluate degreeModuleToEnrol) {
        super();

        setExecutionPeriod(executionInterval);
        setSelectedDegreeModuleToEnrol(degreeModuleToEnrol);
        setStudentCurricularPlan(studentCurricularPlan);

    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public boolean hasDegreeType() {
        return getDegreeType() != null;
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

    public ExecutionInterval getExecutionPeriod() {
        return this.executionInterval;
    }

    public void setExecutionPeriod(ExecutionInterval executionInterval) {
        this.executionInterval = executionInterval;
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

    public CurricularCourse getSelectedOptionalCurricularCourse() {
        return this.selectedOptionalCurricularCourse;
    }

    public void setSelectedOptionalCurricularCourse(CurricularCourse selectedOptionalCurricularCourse) {
        this.selectedOptionalCurricularCourse = selectedOptionalCurricularCourse;
    }

    public IDegreeModuleToEvaluate getSelectedDegreeModuleToEnrol() {
        return selectedDegreeModuleToEnrol;
    }

    public void setSelectedDegreeModuleToEnrol(IDegreeModuleToEvaluate selectedDegreeModuleToEnrol) {
        this.selectedDegreeModuleToEnrol = selectedDegreeModuleToEnrol;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }

}
