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
package org.fenixedu.academic.dto.administrativeOffice.studentEnrolment;

import java.io.Serializable;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;

public class StudentOptionalEnrolmentBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private StudentCurricularPlan studentCurricularPlan;
    private ExecutionInterval executionInterval;
    private CurriculumGroup curriculumGroup;
    private Context context;
    private DegreeType degreeType;
    private Degree degree;
    private DegreeCurricularPlan degreeCurricularPlan;
    private CurricularCourse selectedCurricularCourse;
    private CurricularRuleLevel curricularRuleLevel;

    public StudentOptionalEnrolmentBean() {
    }

    public StudentOptionalEnrolmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionInterval executionInterval,
            CurriculumGroup curriculumGroup, Context context) {
        setStudentCurricularPlan(studentCurricularPlan);
        setExecutionPeriod(executionInterval);
        setCurriculumGroup(curriculumGroup);
        setContext(context);
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public ExecutionInterval getExecutionPeriod() {
        return this.executionInterval;
    }

    public void setExecutionPeriod(ExecutionInterval executionInterval) {
        this.executionInterval = executionInterval;
    }

    public CurriculumGroup getCurriculumGroup() {
        return this.curriculumGroup;
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
        this.curriculumGroup = curriculumGroup;
    }

    public Context getContex() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return this.degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public CurricularCourse getSelectedCurricularCourse() {
        return selectedCurricularCourse;
    }

    public void setSelectedCurricularCourse(CurricularCourse selectedCurricularCourse) {
        this.selectedCurricularCourse = selectedCurricularCourse;
    }

    public CurricularRuleLevel getCurricularRuleLevel() {
        return curricularRuleLevel;
    }

    public void setCurricularRuleLevel(CurricularRuleLevel curricularRuleLevel) {
        this.curricularRuleLevel = curricularRuleLevel;
    }

}
