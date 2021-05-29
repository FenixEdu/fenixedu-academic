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
package org.fenixedu.academic.ui.struts.action.student.enrollment;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Student;

import java.io.Serializable;

public class ExtraordinarySeasonStudentEnrollmentBean implements Serializable {

    private Student student;
    private StudentCurricularPlan scp;
    private ExecutionSemester executionSemester;

    public ExtraordinarySeasonStudentEnrollmentBean() {
        super();
    }

    public ExtraordinarySeasonStudentEnrollmentBean(Student student) {
        this();
        setStudent(student);
    }

    public ExtraordinarySeasonStudentEnrollmentBean(Student student, StudentCurricularPlan scp) {
        this();
        setStudent(student);
        setScp(scp);
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentCurricularPlan getScp() {
        return scp;
    }

    public void setScp(StudentCurricularPlan scp) {
        this.scp = scp;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

}
